package za.co.wethinkcode.robots.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.wethinkcode.robots.protocols.Request;
import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.protocols.commands.Command;
import za.co.wethinkcode.robots.protocols.commands.CommandHandler;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.protocols.config.Config;

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
    Config config = new Config(10, 10, 10, 10, 10, 10); //Default values for testing
    final World world = new World(config);

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

                    //Resolve which command to run
                    Command command = CommandHandler.create(request);

                    //Create a new Robot for Launch
                    Robot bot = new Robot(request.getRobotName());
                    ///Sync. Ensures taht only one `ClientHandler` can touch the Robot at a time, keeping your simulation's
                    ///consistent
                    Response response;
                    synchronized (world) {
                        //Response == Handling Command (Execute)
                        response = command.execute(targetRobot, world);
                        String jsonResponse;
                        //Jackson Response object -> JSON String
                        //Serialization
                        jsonResponse = mapper.writeValueAsString(response);
                        out.println(mapper.writeValueAsString(jsonResponse)); //RETURN:mapper.writeValueAsString(response);
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

// Inside your ClientHandler run() loop:
//String jsonLine = in.readLine();
//Request request = mapper.readValue(jsonLine, Request.class);
//
//// 1. Resolve which command to run
//Command command = CommandFactory.create(request);
//
//// 2. Identify/Create the robot (For launch, you might create a new one)
//Robot robot = new Robot(request.getRobotName());
//
//// 3. Execute against the SHARED world
//synchronized (sharedWorld) {
//Response response = command.execute(robot, sharedWorld);
//    out.println(mapper.writeValueAsString(response));
//        }



//public class ClientHandler implements Runnable{
//    //Implement ObjectMapper
//    private final ObjectMapper mapper = new ObjectMapper(); //Thread safe and reuse possible
//    //Owns Read/Write pipelines
//    private final Socket clientSocket;
//
//    //Create a CommandHandler Object
//    Command command;
//    final Robot targetRobot;
//
//    //Initialize `commandHandler` and `world` objects
//    public ClientHandler(Socket socket, Command command, Robot robot) throws IOException{
//        this.clientSocket = socket;
//        this.command = command;
//        this.targetRobot = robot;
//    } //20.20.20.165
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
//                try {
//                    //Parse the JSON using Jackson.
//                    //Jackson: JSON as String -> Request Object
//
//                    //Deserialization
//                    // jsonLine = {"robot": "Bender", "command": "move", "arguments": ["5"]}
//                    Request request = mapper.readValue(jsonLine, Request.class);
//
//                    System.out.println("From " + clientSocket.getInetAddress() + ": " + request.getCommand());
//
//                    //Handling Concurrency
//                    ///Sync. Ensures taht only one `ClientHandler` can touch the Robot at a time, keeping your simulation's
//                    ///consistent
//                    Response response;
//                    synchronized (targetRobot) {
//                        //Response == Handling Command (Execute)
//                        response = command.execute(targetRobot);
//                        String jsonResponse;
//                        //Jackson Response object -> JSON String
//                        //Serialization
//                        jsonResponse = mapper.writeValueAsString(response);
//                        out.println(jsonResponse); //RETURN:mapper.writeValueAsString(response);
//                    }
//                } catch (Exception e) {
//                    out.print(mapper.writeValueAsString(Response.error(" " + e.getMessage())));
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Client " + clientSocket.getInetAddress() + " disconnected.");
//        } finally {
//            try {
//                clientSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//}

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
