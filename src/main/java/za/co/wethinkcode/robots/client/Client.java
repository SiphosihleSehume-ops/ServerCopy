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
        int port = 5003;
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