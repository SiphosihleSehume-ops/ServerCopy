package za.co.wethinkcode.robots.server;

import za.co.wethinkcode.flow.Recorder;
import za.co.wethinkcode.robots.protocols.commands.Command;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/*
* Handle multiple clients (
* */
public class MultiServer {

    public static void main(String[] args) throws IOException {
        int PORT = 5001;
        final int MAX_CLIENTS = 50;

        //Objects so .execute() does not return null
        Command command = new Command();

        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);

        //Since this will be a MultiServer, we need not create our pipelines
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Robot is running on port " + PORT);

            //Begin loop
            while (true) {
                //The Client Socket here is the Server Socket listening for incoming requests
                Socket clientSocket = serverSocket.accept();
                System.out.println("New Robot has been connected " + clientSocket.getInetAddress());

                //Handing over individual client to ClientHandler
                executor.execute(new ClientHandler(clientSocket, command, targetRobot));

                executor.shutdown();
            }

        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
        } catch (UnsupportedOperationException e) {
            System.out.println("Invalid credentials. Error " + e.getMessage());
        }

//        throw new UnsupportedOperationException(
    }

    // The following initialisation is REQUIRED for `flow` monitoring.
    // DO NOT REMOVE OR MODIFY THIS CODE.
    static {
        new Recorder().logRun();
    }
    //pool.execute(new ClientHandler(clientSocket));

}

//Sockets should try fitting in with this logic
//Receive raw JSON String (Should be renamed to ServerHandling)
//
//public class RobotClient {
//    private final ObjectMapper mapper = new ObjectMapper();
//    private final Scanner scanner = new Scanner(System.in);
//
//    public void start() {
//        // 1. Initial Connection Logic (Socket, BufferedReader, etc.)
//
//        while (true) {
//            System.out.print("Enter command (e.g., launch, move 5, look): ");
//            String input = scanner.nextLine();
//
//            if (input.equalsIgnoreCase("quit")) break;
//
//            try {
//                // 2. Wrap the command in a Request object
//                // You'll need a helper method to split "move 5" into ["move", "5"]
//                Request request = parseInput(input);
//
//                // 3. Serialize to JSON
//                String jsonRequest = mapper.writeValueAsString(request);
//                out.println(jsonRequest);
//
//                // 4. Wait for Server
//                String jsonResponse = in.readLine();
//                if (jsonResponse != null) {
//                    Response response = mapper.readValue(jsonResponse, Response.class);
//                    displayResponse(response); // A method to print the Robot's state nicely
//                }
//
//            } catch (JsonProcessingException e) {
//                System.out.println("Local Error: Could not format request.");
//            } catch (IOException e) {
//                System.out.println("Network Error: Connection to server lost.");
//                break;
//            }
//        }
//    }
//}