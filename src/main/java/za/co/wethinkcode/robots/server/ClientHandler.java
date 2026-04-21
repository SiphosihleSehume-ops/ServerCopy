package za.co.wethinkcode.robots.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.wethinkcode.robots.protocols.Request;
import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.protocols.commands.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable{
    //Implement ObjectMapper
    private ObjectMapper mapper = new ObjectMapper(); //Thread safe and reuse possible
    //Owns Read/Write pipelines
    private final Socket clientSocket;

    //Create a CommandHandler Object
    CommandHandler commandHandler;
    World world;

    //Initialize `commandHandler` and `world` objects
    public ClientHandler(Socket socket, CommandHandler commandHandler, World world) throws IOException{
        this.clientSocket = socket;
        this.commandHandler = commandHandler;
        this.world = world;
    }

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
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

                //Parse the JSON using Jackson.
                //Jackson: JSON as String -> Request Object

                //Deserialization
                // jsonLine = {"robot": "Bender", "command": "move", "arguments": ["5"]}
                Request request = mapper.readValue(jsonLine, Request.class);

                System.out.println("From " + clientSocket.getInetAddress() + ":" + request.getCommand());

                //Response == Handling Command (Execute)
                Response response = commandHandler.execute(request, world);

                //Jackson Response object -> JSON String
                //Serialization
                String jsonResponse;
                jsonResponse = mapper.writeValueAsString(response);
                out.println(jsonResponse);

            }
        } catch (IOException e) {
            System.err.print("Server error " + e.getMessage());
        }
    }
//Core loop: Receive -> Deserialize -> Executes -> Serialize -> Send

}
