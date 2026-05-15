package za.co.wethinkcode.robots.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.wethinkcode.robots.protocols.Request;
import za.co.wethinkcode.robots.protocols.Response;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * RobotClient — terminal client for the robot game.
 *
 * Usage:
 *   java RobotClient [host] [port]
 *   (defaults: localhost 9001)
 *
 * The player types commands at the prompt. Each line is parsed into a
 * Request DTO, serialized to JSON, and sent to the server.
 * The server's JSON response is pretty-printed back to the terminal.
 *
 * Command syntax
 *
 *   launch <name> <type>      — join the world  e.g.  launch HAL SNIPER
 *   forward <steps>           — move forward    e.g.  forward 3
 *   back <steps>              — move back       e.g.  back 2
 *   left                      — turn left
 *   right                     — turn right
 *   look                      — scan surroundings
 *   fire                      — fire in current direction
 *   state                     — print your robot's state
 *   reload                    — reload ammo
 *   repair                    — repair shields
 *   quit / exit               — disconnect
 *
 *
 */
public class Client {

    private static final String DEFAULT_HOST = "localhost";
    private static final int    DEFAULT_PORT = 9001;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // The robot name is remembered after a successful launch so subsequent
    // commands automatically carry it without the player re-typing it.
    private static String robotName = null;

    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : DEFAULT_HOST;
        int    port = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;

        try (Socket socket = new Socket(host, port);
             PrintWriter  out    = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in   = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to " + host + ":" + port);
            printHelp();

            String line;
            while (true) {
                System.out.print(prompt());
                line = stdin.readLine();
                if (line == null) break;          // stdin closed (piped input)
                line = line.trim();
                if (line.isEmpty()) continue;

                // Local exit
                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye.");
                    break;
                }

                if (line.equalsIgnoreCase("help")) {
                    printHelp();
                    continue;
                }

                // Parse the raw string into a Request
                Request request;
                try {
                    request = parse(line);
                } catch (IllegalArgumentException e) {
                    System.out.println("Client: " + e.getMessage());
                    continue;
                }

                // Serialize → send → receive → pretty-print
                String json = MAPPER.writeValueAsString(request);
                out.println(json); //Send to Server

                //Receive data from the Server
                String responseJson = in.readLine();
                if (responseJson == null) {
                    System.out.println("Server closed the connection.");
                    break;
                }

                // Pretty-print the JSON response
                Object pretty = MAPPER.readValue(responseJson, Object.class);
                System.out.println(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(pretty));

                // Remember the robot name after launch so we don't need to type it again
                if ("launch".equalsIgnoreCase(request.command()) && robotName == null) {
                    robotName = request.robot();
                }
            }
        }
    }

    // Parser

    /**
     * Converts a raw terminal line into a {@link Request}.
     *
     * Supported formats:
     *   launch <name> <type>
     *   forward <steps>
     *   back <steps>
     *   left | right | look | fire | state | reload | repair
     */
    private static Request parse(String line) {
        String[] packets = line.split("\\s+");
        String cmd = packets[0].toLowerCase();

        return switch (cmd) {
            case "launch" -> {
                if (packets.length < 3) {
                    throw new IllegalArgumentException("Usage: launch <name> <type>");
                }
                String name = packets[1];
                String type = packets[2].toUpperCase();
                yield new Request(name, "launch", List.of(type));
            }

            case "forward", "back" -> {
                if (packets.length < 2) {
                    throw new IllegalArgumentException("Usage: " + cmd + " <steps>");
                }
                int steps;
                try {
                    steps = Integer.parseInt(packets[1]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("<steps> must be a number");
                }
                yield new Request(requireName(), cmd, List.of(steps));
            }

            case "left", "right", "look", "fire",
                 "state", "reload", "repair" -> {
                yield new Request(requireName(), cmd, List.of());
            }

            default -> throw new IllegalArgumentException(
                    "Unknown command '" + cmd + "'. Type 'help' for a list.");
        };
    }
    // Helper Command For Client
    /** Returns the stored robot name, or throws if no launch has happened yet. */
    private static String requireName() {
        if (robotName == null) {
            throw new IllegalArgumentException(
                    "You must launch a robot first: launch <name> <type>");
        }
        return robotName;
    }

    //For as long as there is no Robot Launched, display '>' else `robotName >`
    private static String prompt() {
        return robotName == null ? "> " : robotName + "> ";
    }

    private static void printHelp() {
        System.out.println("""
                launch <name> <type>  join the world    
                   types: SHOOTER SNIPER TANK SCOUT      
                
                 forward <n>           move forward      
                 back <n>              move back         
                 left                  turn left         
                 right                 turn right        
                 look                  scan surroundings 
                 fire                  shoot!            
                 state                 your robot status 
                 reload                reload ammo       
                 repair                repair shields    
                 quit / exit           disconnect 
               
                """);
    }
}