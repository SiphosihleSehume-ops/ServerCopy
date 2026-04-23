package za.co.wethinkcode.robots.server;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    private final ObjectMapper mapper = new ObjectMapper(); //Thread safe and reuse possible
    //Owns Read/Write pipelines
    private final Socket clientSocket;

    //Create a CommandHandler Object
    CommandHandler commandHandler;
    Robot targetRobot;

    //Initialize `commandHandler` and `world` objects
    public ClientHandler(Socket socket, CommandHandler commandHandler, Robot robot) throws IOException{
        this.clientSocket = socket;
        this.commandHandler = commandHandler;
        this.targetRobot = robot;
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
                Response response = commandHandler.execute(targetRobot);

                //Jackson Response object -> JSON String
                //Serialization
                String jsonResponse;
                jsonResponse = mapper.writeValueAsString(response);
                out.println(jsonResponse); //RETURN:mapper.writeValueAsString(response);

            }
        } catch (JsonProcessingException e) {
            return mapper.writeValueAsString(Response.error("Could not parse arguments"));

        } catch (UnsupportedCommandException e ) {
            return mapper.writeValueAsString(Response.error("Unsupported command"));
        }
    }
//Core loop: Receive -> Deserialize -> Executes -> Serialize -> Send

}
