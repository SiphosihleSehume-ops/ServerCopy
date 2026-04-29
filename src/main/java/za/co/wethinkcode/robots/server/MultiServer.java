package za.co.wethinkcode.robots.server;

import za.co.wethinkcode.flow.Recorder;
import za.co.wethinkcode.robots.protocols.Request;
import za.co.wethinkcode.robots.protocols.commands.Command;
import za.co.wethinkcode.robots.protocols.commands.LaunchCommand;
import za.co.wethinkcode.robots.protocols.config.ConfigLoader;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.robot.RobotType;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/*
* Handle multiple clients (
* */

public class MultiServer {
    public static void main(String[] cmdArgs) throws IOException {

        //Declare and initialize port number
        Request request = new Request();
        RobotType botType = RobotType.SHOOTER;
        int port = 9001;
        int MAX_CLIENTS = 50;
        World world = new World(ConfigLoader.load());
        world.createRandomObstacles();
        //We instantiate a ServerSocket object; "Turns on Server" at a specific entrance gate (port)
        @SuppressWarnings("resource") ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);

        //Objects so .execute() does not return `null`
//        Command command = new LaunchCommand(request.getArguments());
//        Robot targetRobot = new Robot(request.getRobotName(), botType);


        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);

//        int nrClients = 0;
        while (true) {  //Handling multiple clients one by one

            // blocks until client connects; accept() is a blocking call; server will finish with one client, loop back
            // the top, and wait for next. It's a "sequential" server
            try {
                Socket clientSocket = serverSocket.accept();
                //Crucial; line for logging and debugging; `getInetAddress` pulls the IP Address
                //Java automatically calls the `.toString()` method on Ip Address object
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                //Send Client Request to Server
                executor.execute(new ClientHandler(clientSocket, world));


            }
            catch(IOException e){
                System.out.println("Connection error: " + e.getMessage());
            }   }

    }






//public class MultiServer {
//
//    public static void main(String[] args)  {
//        int PORT = 5002;
//        final int MAX_CLIENTS = 50;
//
//        //Objects so .execute() does not return null
//        Request request = new Request();
//        RobotType botType = RobotType.SHOOTER;
//        Command command = new Command();
//        Robot targetRobot = new Robot(request.getRobotName(), botType);
//
//        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);
//
//        //Since this will be a MultiServer, we need not create our pipelines
//        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//            System.out.println("Robot is running on port " + PORT);
//
//            //Begin loop
//            while (true) {
//                //The Client Socket here is the Server Socket listening for incoming requests
//                Socket clientSocket = serverSocket.accept();
//                System.out.println("New Robot has been connected " + clientSocket.getInetAddress());
//
//                //Handing over individual client to ClientHandler
//                executor.execute(new ClientHandler(clientSocket, command, targetRobot));
//
//                executor.shutdown();
//            }
//
//        } catch (IOException e) {
//            System.out.println("Error " + e.getMessage());
//        } catch (UnsupportedOperationException e) {
//            System.out.println("Invalid credentials. Error " + e.getMessage());
//        }
//
////        throw new UnsupportedOperationException(
//    }

    // The following initialization is REQUIRED for `flow` monitoring.
    // DO NOT REMOVE OR MODIFY THIS CODE.
    static {
        new Recorder().logRun();
    }
    //pool.execute(new ClientHandler(clientSocket));

}