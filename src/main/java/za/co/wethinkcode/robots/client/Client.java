package za.co.wethinkcode.robots.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.wethinkcode.robots.protocols.Response;

import java.io.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        //Unique: Declare and initialize `localhost`
//        execution service
        String host = "localhost";
        int port = 9001;
        ObjectMapper mapper = new ObjectMapper();

        try (//Instantiate client socket
             Socket clientSocket = new Socket(host, port);
             //Build `output` pipeline object
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             //Build `input` pipeline object
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             //Build `input through the CMD line`
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ){
            System.out.println("Client " + clientSocket.getInetAddress() + " connected successfully!");
            System.out.println("Type JSON and press enter: ");

            //Sending data to server
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                //Send data to server
                out.println(userInput);


                //Wait for server responsive
                String response = in.readLine();

                Response json = mapper.readValue(response, Response.class);
                String prettyResponse = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                System.out.println("Server responded:\n " + prettyResponse);

                if ("quit".equalsIgnoreCase(userInput)) break;
            }
        }

    }
}

//this client uses a dedicated Tcp Socket to create a dedicated 'pipe'
//the STREAMS: We wrap the 'pipe' in (for Hearing) readers and writers(for speaking)
//This is a Synchronous client it follows a strict SEND -> Wait -> Recieve pattern
// it waits and doesn't do much else while it waits for the server to respond


//input/output reading and writing data

//import java.net.*;
//// Importing classes needed for reading user input from keyboard
//import java.util.*;
//// Importing classes needed for reading/writing data through the socket
//import java.io.*;
//// Jackson ObjectMapper helps us convert Java objects to JSON and JSON back to Java objects
//import com.fasterxml.jackson.databind.ObjectMapper;
//import za.co.wethinkcode.robots.protocols.Request;
//
//public class RobotClient {
//
//    // Server IP address
//    // localhost means the server is running on the same computer
//    private static String IP = "localhost";
//
//    // Port number must match the server port
//    private static int PORT = 9090;
//
//    public static void main(String[] args) {
//
//        // try-with-resources automatically closes everything safely
//        try (
//                // Create a socket connection to the server
//                Socket socket = new Socket(IP, PORT);
//
//                // Used to read messages coming FROM the server
//                BufferedReader serverReader = new BufferedReader(
//                        new InputStreamReader(socket.getInputStream())
//                );
//
//                // Used to send messages TO the server
//                PrintWriter serverWriter = new PrintWriter(
//                        socket.getOutputStream(), true
//                );
//
//                // Used to read user input from keyboard
//                Scanner scanner = new Scanner(System.in)
//        ) {
//
//            // ObjectMapper converts Java objects into JSON
//            // and JSON back into Java objects
//            ObjectMapper mapper = new ObjectMapper();
//
//            System.out.println("Connected to Robot World Server!\n");
//
//            // This controls whether the client keeps running
//            boolean running = true;
//
//            // Main loop keeps program running until user chooses quit
//            while (running) {
//
//                System.out.println("Choose a command:");
//                System.out.println("~ launch");
//                System.out.println("~ look");
//                System.out.println("~ state");
//                System.out.println("~ quit");
//                System.out.print("Enter command: ");
//
//                // Read command from user
//                String command = scanner.nextLine().toLowerCase();
//
//                // Ask for robot name
//                System.out.print("Enter robot name: ");
//                String robotName = scanner.nextLine();
//
//                // Request object that will be sent to server
//                Request request;
//
//                // Decide which command to create
//                switch (command) {
//
//                    case "launch":
//
//                        // Ask user for robot make
//                        System.out.print("Enter robot make (example: sniper): ");
//                        String make = scanner.nextLine();
//
//                        // Create launch request
//                        request = new Request(
//                                robotName,              // robot name
//                                "launch",               // command
//                                new List<String> {make, "5", "5"} // arguments
//                        );
//                        break;
//
//                    case "look":
//
//                        // Create look request (no arguments needed)
//                        request = new Request(
//                                robotName,
//                                "look",
//                                new String[]{}
//                        );
//                        break;
//
//                    case "state":
//
//                        // Create state request (no arguments needed)
//                        request = new Request(
//                                robotName,
//                                "state",
//                                new String[]{}
//                        );
//                        break;
//
//                    case "quit":
//
//                        // Create quit request
//                        request = new Request(
//                                robotName,
//                                "quit",
//                                new String[]{}
//                        );
//
//                        // Stop the loop after sending quit
//                        running = false;
//                        break;
//
//                    default:
//                        System.out.println("Invalid command. Try again.\n");
//                        continue;
//                }
//
//                // Convert Java Request object into JSON string
//                String jsonRequest = mapper.writeValueAsString(request);
//
//                System.out.println("\nSending JSON to server:");
//                System.out.println(jsonRequest);
//
//                // Send JSON request to server
//                serverWriter.println(jsonRequest);
//
//                // Read JSON response from server
//                String jsonResponse = serverReader.readLine();
//
//                System.out.println("\nResponse from server:");
//                System.out.println(jsonResponse + "\n");
//            }
//
//            System.out.println("Client closed successfully.");
//
//        } catch (Exception e) {
//
//            // If something goes wrong, show the error
//            System.out.println("Error: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}
