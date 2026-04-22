import org.junit.jupiter.api.*;
import za.co.wethinkcode.robots.server.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@Nested
@DisplayName("Server Integration Tests")
class ServerTest {
    private ServerSocket serverSocket;
    private static final int TEST_PORT = 5001;
    private ExecutorService executor;
    private List<Socket> clientsSockets;

    @BeforeEach
    public void setUp() throws IOException {
        executor = Executors.newFixedThreadPool(10);
        clientsSockets = Collections.synchronizedList(new ArrayList<>());
    }
    @AfterEach
    public void tearDown() throws IOException {
        executor.shutdownNow();
        for (Socket socket : clientsSockets) {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }catch (IOException ignored) {

            }
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
//    This test ensures multiple clients can connect
//    No data corruption and multithreading is possible

    @Test
    @DisplayName("Multiple clients can connect simultaneously and receive input")
    @Timeout(10)
    void testMultipleClientsConnect() throws IOException, InterruptedException {
        serverSocket = new ServerSocket(TEST_PORT);
        int numClients = 5;
        CountDownLatch allClientsConnected = new CountDownLatch(numClients);
        CountDownLatch allClientsDisconnected = new CountDownLatch(numClients);
        List<String> receivedMessages = Collections.synchronizedList(new ArrayList<>()); //Thread safe list for client responses

        Thread serverAcceptThread = new Thread(() -> {
            for (int i = 1; i <= numClients; i++) {
                try {
                    //an await for client connection
                    Socket connection = serverSocket.accept();
                    clientsSockets.add(connection);
                    ClientHandler server = new ClientHandler(connection); //Client handler
                    executor.submit(server);

                } catch (IOException ignored) {

                }
            }
        });
        serverAcceptThread.start();
// This simulates multiple clients
        for (int i = 1; i< numClients; i++) {
            final int clientId = i;
            executor.submit(() -> {
                try {
                    Socket clientSocket = new Socket("localhost",TEST_PORT);
                    clientsSockets.add(clientSocket);
                    allClientsConnected.countDown();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())
                    );
                    String message = "Message from client " + clientId;
                    out.println(message);

                    String response = in.readLine();
                    receivedMessages.add(response);

                    out.close();
                    in.close();
                } catch (IOException e) {
                    fail("Client " + clientId + " failed " + e.getMessage());
                } finally {
                    allClientsConnected.countDown();
                }

            });
            assertTrue(
                    allClientsConnected.await(5, TimeUnit.SECONDS), "Not all clients" +
                            " connected in time."
            );
            assertTrue(
                    allClientsDisconnected.await(5, TimeUnit.SECONDS), "Not all clients finished" +
                            " in time."
            );
            assertEquals(
                    numClients, receivedMessages.size(), "Should receive messages from each client"

            );
            for (int x = 0; x < numClients; x ++) {
                assertTrue(receivedMessages.contains("Message from " + x), "Should receive messages from " + x);
            }
        }
    }

    @Test
    @DisplayName("Multiple Clients can send input concurrently")
    @Timeout(15)
    void testConcurrentMessaging() throws IOException, InterruptedException {
        serverSocket = new ServerSocket(TEST_PORT);
        int numClients = 3;
        int messagesPerClient = 10;
        CountDownLatch allFinished = new CountDownLatch(numClients);
        AtomicInteger totalMessagesReceived = new AtomicInteger(0);

        Thread startServer = new Thread(() -> {
            for (int i = 0; i < numClients; i++) {
                try {
                    Socket connection = new serverSocket.accept();
                    clientsSockets.add(connection);
                    ClientHandler server = new ClientHandler(connection);
                    executor.submit(server);
                } catch (IOException e) {

                }
            }
        });
        startServer.start();

        for (int clientId = 0; clientId < numClients; clientId++) {
            final int id = clientId;
            executor.submit(() -> {
                try {
                    Socket clientConnection = new Socket("localhost",TEST_PORT);
                    clientsSockets.add(clientConnection);
                    PrintWriter out = new PrintWriter(clientConnection.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));

                    for (int msg = 0; msg < messagesPerClient; msg++) {
                        String message = "Client " + id + "Message " + msg;
                        out.println(message);
                        String response = in.readLine();
                        assertEquals(message,response);
                    }

                    out.close();
                    in.close();
                    clientConnection.close();

                } catch (IOException e ) {
                    fail("Client " + id + " failed: " + e.getMessage());
                } finally {
                    allFinished.countDown();
                }
            });
        }

        assertTrue(allFinished.await(10, TimeUnit.SECONDS),"Not all clients finished in time");
        assertEquals(numClients * messagesPerClient, totalMessagesReceived.get(),
                "Should receive all messages from clients");
    }

    @Test
    @DisplayName("Server handles client disconnections gracefully")
    @Timeout(10)
    void testClientDisconnectMidOperation() throws IOException, InterruptedException {
        serverSocket = new ServerSocket(TEST_PORT);
        CountDownLatch serverReady = new CountDownLatch(1);
        CountDownLatch clientDisconnected = new CountDownLatch(1);

        Thread serverThread = new Thread(() -> {
            try {
                serverReady.countDown();
                Socket connection = serverSocket.accept();
                clientsSockets.add(connection);
                ClientHandler server = new ClientHandler(connection);
                server.run();
                clientDisconnected.countDown();
            } catch (IOException e) {
                clientDisconnected.countDown();
            }
        });
        serverThread.start();

        serverReady.await(2, TimeUnit.SECONDS);
        Socket clientConnection = new Socket("localhost",TEST_PORT);
        clientsSockets.add(clientConnection);
        PrintWriter out = new PrintWriter( clientConnection.getOutputStream(), true);
        out.println("Message before disconnect");

        clientConnection.close();

        assertTrue(clientDisconnected.await(5, TimeUnit.SECONDS),
                "Server should finish handling disconnect");
        assertDoesNotThrow(() -> serverThread.join(2000), "Server should not throw this exception");


    }

    @Test
    @DisplayName("Server handles partial message gracefully")
    @Timeout(10)
    void testHandlesPartialMessage() throws IOException, InterruptedException {
        serverSocket = new ServerSocket(TEST_PORT);
        CountDownLatch serverReady = new CountDownLatch(1);
        CountDownLatch testComplete = new CountDownLatch(1);
        Thread serverConnection = new Thread(() -> {
            try{
                serverReady.countDown();
                Socket connection = serverSocket.accept();
                clientsSockets.add(connection);
                ClientHandler server = new ClientHandler(connection);
                server.run();

            } catch (IOException e) {

            } finally {
                testComplete.countDown();
            }
        });
        serverConnection.start();
        Socket clientConnection = new Socket("localhost", TEST_PORT);
        clientsSockets.add(clientConnection);
        OutputStream rawOutput = clientConnection.getOutputStream();
        rawOutput.write("Incomplete message".getBytes());
        rawOutput.flush();
        Thread.sleep(1000);
        clientConnection.close();

        assertTrue(testComplete.await(10,TimeUnit.SECONDS), "Server should handle incomplete message");
    }

    @Test
    @DisplayName("Server starts and accepts connections")
    @Timeout(10)
    void testServerStart() throws IOException, InterruptedException {
        final int TESTING_PORT = 5002;
        CountDownLatch startServerConnect = new CountDownLatch(1);
        CountDownLatch testComplete = new CountDownLatch(1);

        Thread serverStart = new Thread(() -> {
            try {
                startServerConnect.countDown();
                ServerSocket mainServerSocket = new ServerSocket(TESTING_PORT);
                Socket connection = mainServerSocket.accept();
                ClientHandler server = new ClientHandler(connection);
                server.run();
                mainServerSocket.close();
            } catch (IOException e) {

            }finally {
                testComplete.countDown();
            }
        });
        serverStart.start();
        startServerConnect.await(2, TimeUnit.SECONDS);
        Socket clientSocket = new Socket("localhost", TESTING_PORT);
        clientsSockets.add(clientSocket);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String testMessage = "This should be correct";
        out.println(testMessage);
        String response = in.readLine();

        assertEquals(testMessage, response, "Server should echo the message");
        out.close();
        in.close();
        clientSocket.close();

        assertTrue(testComplete.await(5, TimeUnit.SECONDS), "Server should complete cleanly.");
    }

    @Test
    @DisplayName("Server handles reconnections")
    @Timeout(10)
    void testServerReconnections() throws IOException, InterruptedException {
        serverSocket = new ServerSocket(TEST_PORT);
        int reconnectionAttempts = 5;
        CountDownLatch allReconnected = new CountDownLatch(reconnectionAttempts);

        Thread serverThread = new Thread(() -> {
            for (int i = 0; i < reconnectionAttempts; i++) {
                try {
                    Socket connection = serverSocket.accept();
                    clientsSockets.add(connection);
                    ClientHandler server = new ClientHandler(connection);
                    executor.submit(server);

                } catch (IOException e) {
                }
            }});
            serverThread.start();
            for (int i = 0; i < reconnectionAttempts; i++) {
                executor.submit(() -> {
                    try {
                        Socket clientSocket = new Socket("localhost", TEST_PORT);
                        clientsSockets.add(clientSocket);

                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                        out.println("Quick message");
                        out.close();
                        clientSocket.close();

                    } catch (IOException e) {
                        fail("Reconnection attempt failed: " + e.getMessage());
                    } finally {
                        allReconnected.countDown();
                    }
                });

            }
            assertTrue(allReconnected.await(10, TimeUnit.SECONDS),
                    "All reconnections attempts should be completed.");
        }

    private record accept() {
    }
}