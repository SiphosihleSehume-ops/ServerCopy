package za.co.wethinkcode.robots.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        int port = 9090;
        String host = "localhost";
        ObjectMapper mapper = new ObjectMapper();

        try (Socket clientSocket = new Socket(host, port);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            //Let the client know that they connected successfully to the server
            System.out.println("Client " + clientSocket.getInetAddress() + "connected successfully!");
            System.out.println("Client connected. Type JSON then Enter.");

            String jsonLine;
            while ((jsonLine = in.readLine()) != null) {
                out.println(jsonLine); //Sends information to the Server


                String response = in.readLine(); //Keeps the Client window open for incoming requests
                System.out.println("Received response: " + response);

                if ("quit".equalsIgnoreCase(jsonLine));
            }
        }
    }
}