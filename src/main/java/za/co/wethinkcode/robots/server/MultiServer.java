package za.co.wethinkcode.robots.server;

import za.co.wethinkcode.flow.Recorder;
import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.protocols.commands.CommandHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/*
* Handle multiple clients (
* */
public class MultiServer {

    public static void main(String[] args) throws IOException {
        int PORT = 9090;
        final int MAX_CLIENTS = 50;

        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);

        //Since this will be a MultiServer, we need not to create our pipelines
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Robot is running on port " + PORT);

            //Begin loop
            while (true) {
                //The Client Socket here is the Server Socket listening for incoming requests
                Socket clientSocket = serverSocket.accept();
                System.out.println("New Robot has been connected " + clientSocket.getInetAddress());

                executor.execute(new ClientHandler(clientSocket));

            }

        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
        }



        throw new UnsupportedOperationException( "TODO" );
    }

    // The following initialisation is REQUIRED for `flow` monitoring.
    // DO NOT REMOVE OR MODIFY THIS CODE.
    static {
        new Recorder().logRun();
    }
    //pool.execute(new ClientHanbdler(clientSocket));

}

//Sockets should try fitting in with this logic
//Receive raw JSON String (Should be renamed to ServerHandling)
