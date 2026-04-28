package za.co.wethinkcode.robots.server;

import za.co.wethinkcode.flow.Recorder;
import za.co.wethinkcode.robots.protocols.Request;
import za.co.wethinkcode.robots.protocols.commands.Command;
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

    public static void main(String[] args)  {
        int PORT = 5002;
        final int MAX_CLIENTS = 50;

        //Objects so .execute() does not return null
        Request request = new Request();
        RobotType botType = RobotType.SHOOTER;
        Command command = new Command();
        Robot targetRobot = new Robot(request.getRobotName(), botType);

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

    // The following initialization is REQUIRED for `flow` monitoring.
    // DO NOT REMOVE OR MODIFY THIS CODE.
    static {
        new Recorder().logRun();
    }
    //pool.execute(new ClientHandler(clientSocket));

}