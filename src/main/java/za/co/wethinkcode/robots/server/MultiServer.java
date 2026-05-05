package za.co.wethinkcode.robots.server;

import za.co.wethinkcode.flow.Recorder;
import za.co.wethinkcode.robots.protocols.commands.Command;
import za.co.wethinkcode.robots.protocols.commands.DumpCommand;
import za.co.wethinkcode.robots.protocols.commands.QuitCommand;
import za.co.wethinkcode.robots.protocols.config.ConfigLoader;


import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.IOException;
import java.net.*;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/*
* Handle multiple clients (
* */

public class MultiServer {
    private static final int PORT = 9001;
    private static final int MAX_CLIENTS = 2;
    private static World WORLD;
    private static boolean RUN = true;


    public static Command handleServerCommands(String input){
        return  switch (input.toLowerCase()) {
            case "quit" -> new QuitCommand();
            case "dump" -> new DumpCommand();
            default -> throw new IllegalArgumentException("Unsupported command: " + input);
        };
    }

    public static void main(String[] cmdArgs) throws IOException {


        WORLD = new World(ConfigLoader.load());
        WORLD.createRandomObstacles();

        //We instantiate a ServerSocket object; "Turns on Server" at a specific entrance gate (port)
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server listening on port " + PORT);

        //Objects so .execute() does not return `null`
//        Command command = new LaunchCommand(request.getArguments());
//        Robot targetRobot = new Robot(request.getRobotName(), botType);


        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);

//        int nrClients = 0;
        while (RUN) {

// Handling multiple clients one by one

            // blocks until client connects; accept() is a blocking call; server will finish with one client, loop back
            // the top, and wait for next. It's a "sequential" server
            try {
                Socket clientSocket = serverSocket.accept();
                //Crucial; line for logging and debugging; `getInetAddress` pulls the IP Address
                //Java automatically calls the `.toString()` method on Ip Address object
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                Scanner input = new Scanner(System.in);
                String text = input.nextLine();
                if (!text.isEmpty()){
                    Command command = handleServerCommands(text);
                    RUN = command.executeServer(WORLD);
                }

                if (!RUN){
                    executor.shutdownNow();
                }
                //Send Client Request to Server
                executor.execute(new ClientHandler(clientSocket,WORLD ));



            }

            catch(Exception e){
                System.out.println("Connection error: " + e.getMessage());
            }
        }

    }

    // The following initialization is REQUIRED for `flow` monitoring.
    // DO NOT REMOVE OR MODIFY THIS CODE.
    static {
        new Recorder().logRun();
    }
    //pool.execute(new ClientHandler(clientSocket));

}