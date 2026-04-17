package za.co.wethinkcode.robots.server;

import za.co.wethinkcode.flow.Recorder;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
//
//int MAX_POOL = 100;
//ExecutorService pool = Executors.newCachedThreadPool(MAX_POOL);

/*
* Handle multiple clients (
* */
public class MultiServer {

    public static void main(String[] args) throws IOException {
        int PORT = 9090;
        final int MAX_CLIENTS = 50;
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);

        try (Socket serverSocket = new Socket(PORT);
            ) {

        } catch (IOException e) {

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
