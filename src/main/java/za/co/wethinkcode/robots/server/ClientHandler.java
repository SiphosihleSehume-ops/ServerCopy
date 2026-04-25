package za.co.wethinkcode.robots.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.wethinkcode.robots.protocols.Request;
import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.protocols.commands.Command;
import za.co.wethinkcode.robots.robot.Robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    //Implement ObjectMapper
    private final ObjectMapper mapper = new ObjectMapper(); //Thread safe and reuse possible
    //Owns Read/Write pipelines
    private final Socket clientSocket;

    //Create a CommandHandler Object
    Command command;
    final Robot targetRobot;

    //Initialize `commandHandler` and `world` objects
    public ClientHandler(Socket socket, Command command, Robot robot) throws IOException{
        this.clientSocket = socket;
        this.command = command;
        this.targetRobot = robot;
    } //20.20.20.165

    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             //flush forces all buffered data to be written to their destination
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            //Take in jsonLine; block until; client engages
            String jsonLine;
            while ((jsonLine = in.readLine()) != null) {
                System.out.println("Received " + jsonLine);
                try {
                    //Parse the JSON using Jackson.
                    //Jackson: JSON as String -> Request Object

                    //Deserialization
                    // jsonLine = {"robot": "Bender", "command": "move", "arguments": ["5"]}
                    Request request = mapper.readValue(jsonLine, Request.class);

                    System.out.println("From " + clientSocket.getInetAddress() + ": " + request.getCommand());

                    //Handling Concurrency
                    ///Sync. Ensures taht only one `ClientHandler` can touch the Robot at a time, keeping your simulation's
                    ///consistent
                    Response response;
                    synchronized (targetRobot) {
                        //Response == Handling Command (Execute)
                        response = command.execute(targetRobot);
                        String jsonResponse;
                        //Jackson Response object -> JSON String
                        //Serialization
                        jsonResponse = mapper.writeValueAsString(response);
                        out.println(jsonResponse); //RETURN:mapper.writeValueAsString(response);
                    }
                } catch (Exception e) {
                    out.print(mapper.writeValueAsString(Response.error(" " + e.getMessage())));
                }
            }
        } catch (IOException e) {
            System.out.println("Client " + clientSocket.getInetAddress() + " disconnected.");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

//Core loop: Receive -> Deserialize -> Executes -> Serialize -> Send

































//package za.co.wethinkcode.robots.server;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import za.co.wethinkcode.robots.protocols.Request;
//import za.co.wethinkcode.robots.protocols.Response;
//import za.co.wethinkcode.robots.protocols.commands.Command;
//import za.co.wethinkcode.robots.robot.Robot;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//public class ClientHandler implements Runnable{
//    //Implement ObjectMapper
//    private final ObjectMapper mapper = new ObjectMapper(); //Thread safe and reuse possible
//    //Owns Read/Write pipelines
//    private final Socket clientSocket;
//
//    //Create a CommandHandler Object
//    Command command;
//    Robot targetRobot;
//
//    //Initialize `commandHandler` and `world` objects
//    public ClientHandler(Socket socket, Command command, Robot robot) throws IOException{
//        this.clientSocket = socket;
//        this.command = command;
//        this.targetRobot = robot;
//    }
//
//    public ClientHandler(Socket socket) {
//        this.clientSocket = socket;
//    }
//
//    @Override
//    public void run() {
//
//        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//             //flush forces all buffered data to be written to their destination
//             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
//
//            //Take in jsonLine; block until; client engages
//            String jsonLine;
//            while ((jsonLine = in.readLine()) != null) {
//                System.out.println("Received " + jsonLine);
//
//                //Parse the JSON using Jackson.
//                //Jackson: JSON as String -> Request Object
//
//                //Deserialization
//                // jsonLine = {"robot": "Bender", "command": "move", "arguments": ["5"]}
//                Request request = mapper.readValue(jsonLine, Request.class);
//
//                System.out.println("From " + clientSocket.getInetAddress() + ":" + request.getCommand());
//
//                //Response == Handling Command (Execute)
//                Response response = command.execute(targetRobot);
//
//                //Jackson Response object -> JSON String
//                //Serialization
//                String jsonResponse;
//                jsonResponse = mapper.writeValueAsString(response);
//                out.println(jsonResponse); //RETURN:mapper.writeValueAsString(response);
//
//            }
//        } catch (IOException e) {
//            System.out.println("Client " + clientSocket.getInetAddress() + " disconnected.");
//    }
////Core loop: Receive -> Deserialize -> Executes -> Serialize -> Send
//}
//
//}
