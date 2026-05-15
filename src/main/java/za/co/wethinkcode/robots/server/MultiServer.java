package za.co.wethinkcode.robots.server;

import za.co.wethinkcode.flow.Recorder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import za.co.wethinkcode.robots.protocols.*;
import za.co.wethinkcode.robots.protocols.config.ConfigLoader;


import java.io.*;
import java.net.Socket;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * MultiServer — accepts robot clients and runs a shared World.
 *
 * FIX: start() no longer contains its own accept loop.
 *       main() owns the loop; start() is now just a setup/test hook.
 *
 * ADDED: A background thread reads operator commands from stdin.
 *         Type "dump" to print world state to the server console.
 *         Type "stop" to shut down.
 */
public class MultiServer {
    private static final int PORT       = 9001;
    private static final int MAX_CLIENTS = 10;

    private static volatile boolean RUN = true;
    private static World WORLD;
    private static ServerSocket SERVER_SOCKET;

    public static void main(String[] cmdArgs) throws IOException {
        WORLD         = new World(ConfigLoader.load());
        SERVER_SOCKET = new ServerSocket(PORT, MAX_CLIENTS);

        System.out.println("Server started on port " + PORT);

        // Start the operator console thread (stdin → dump / stop)
        Thread console = new Thread(MultiServer::runConsole, "operator-console");
        console.setDaemon(true);
        console.start();

        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);

        while (RUN) {
            try {
                Socket clientSocket = SERVER_SOCKET.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                executor.execute(new ClientHandler(clientSocket, WORLD));
                if (!RUN) stop();
            } catch (IOException e) {
                if (RUN) System.out.println("Accept error: " + e.getMessage());
                // if !RUN the server was stopped deliberately
            }
        }

        executor.shutdown();
        System.out.println("Server stopped.");
    }

    /**
     * Runs on a background thread and processes operator stdin commands:
     *   dump  — prints current world state
     *   stop  — shuts the server down
     */
    private static void runConsole() {
        System.out.println("Console Operator commands: dump | stop");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                switch (line.trim().toLowerCase()) {
                    case "dump" -> {
                        synchronized (WORLD) {
                            System.out.println(WORLD.dump());
                        }
                    }
                    case "stop" -> {
                        stop();
                        return;
                    }
                    default -> System.out.println("Console Unknown operator command: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Console stdin closed: " + e.getMessage());
        }
    }

    /**
     * Setup hook — used by tests to inject a pre-built world and socket.
     * Does NOT start an accept loop (that's main()'s job).
     */
    public static void start(ServerSocket serverSocket, World world) {
        SERVER_SOCKET = serverSocket;
        WORLD         = world;
        RUN           = true;
    }

    public static void stop() {
        RUN = false;
        try {
            if (SERVER_SOCKET != null && !SERVER_SOCKET.isClosed()) {
                SERVER_SOCKET.close(); // unblocks accept()
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Flow Monitoring
    static {
        new Recorder().logRun();
    }
}
