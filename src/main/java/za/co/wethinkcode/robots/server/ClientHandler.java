package za.co.wethinkcode.robots.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.wethinkcode.robots.protocols.*;
import za.co.wethinkcode.robots.protocols.commands.*;
import za.co.wethinkcode.robots.robot.Robot;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    //Implement ObjectMapper
    private final ObjectMapper mapper = new ObjectMapper(); //Thread safe and reuse possible
    //Owns Read/Write pipelines
    private final Socket clientSocket;

    //Create a CommandHandler Object
    private Robot targetRobot = null;
    private final World world;

    //Initialize `commandHandler` and `world` objects
    public ClientHandler(Socket socket, World world) throws IOException{
        this.clientSocket = socket;
        this.world = world;
    }

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
                    Request request = mapper.readValue(jsonLine, Request.class);
                    System.out.println(request);

                    //Parse the JSON using Jackson.
                    //Jackson: JSON as String -> Request Object
                    if (request.command() == null || request.command().isBlank()) throw new IllegalArgumentException(
                            "Command cannot be empty!"
                    );

                    System.out.println("From " + clientSocket.getInetAddress() + ": " + request.command());

                    //Resolve which command to run
                    Command command = CommandHandler.create(request);

                    ///
                    Response response;

                    /**
                     * <p>Sync. Ensures that only one `ClientHandler` can touch the Robot at a time, keeping your simulation's
                     * consistent.</p>
                     * */
                    synchronized(world) {
                        response = command.execute(world);
                    }

                    //Special case where launch updates robot
//                    if (command instanceof LaunchCommand && targetRobot == null) {
//                        targetRobot = world.findRobotByName(request.robot());
//                    }

                    String jsonResponse;
                    //Jackson Response object -> JSON String
                    //Serialization
                    jsonResponse = mapper.writeValueAsString(response);
//                    System.out.println(jsonResponse);

                    out.println(jsonResponse); //RETURN:mapper.writeValueAsString(response)


                } catch (Exception e) {
                    out.println(mapper.writeValueAsString(Response.error(" " + e.getMessage())));
                }
//                finally {
//                    // Clean up — remove the robot if the client disconnects
//                    if (targetRobot != null) {
//                        synchronized (world) {
//                            world.removeRobot(targetRobot);
//                        }
//                        System.out.println("Removed robot: " + targetRobot.name());
//                    }
//                    try { clientSocket.close(); } catch (IOException e) { e.printStackTrace(); }
//                }

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



