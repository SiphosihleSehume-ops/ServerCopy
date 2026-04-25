package za.co.wethinkcode.robots.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.wethinkcode.robots.protocols.Response;

import java.io.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        int port = 5001;
        String host = "localhost";
        ObjectMapper mapper = new ObjectMapper();
        Scanner scanner = new Scanner(System.in);

        try (Socket clientSocket = new Socket(host, port);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            //Let the client know that they connected successfully to the server
            System.out.println("Client " + clientSocket.getInetAddress() + " connected successfully!");
            System.out.println("Client connected. Type JSON then Enter.");

            String jsonLine;
            while ((jsonLine = scanner.nextLine()) != null) {
                //Serialize request
                String request = mapper.writeValueAsString(jsonLine);
                out.println(request); //Sends information to the Server ? .write()

                String response = in.readLine(); //Keeps the Client window open for incoming responses
                //Serialize (OBJECT -> JSON str)
                Response json = mapper.readValue(response,  Response.class);
                String prettyResponse = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                System.out.println("Received response:\n " + prettyResponse);

                if ("quit".equalsIgnoreCase(jsonLine)) break;
            }
        }
    }
}