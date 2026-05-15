package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Request;


import java.util.List;

/**
 * <p>Serves as the Translator. Takes in a raw String like
 * "Launch" and decides which objects to build.</p>
 *
 * @return a new {@link Command} object based on the command name
 */

public class CommandHandler {

    public static Command create(Request request) {
        String cmd = request.command().toLowerCase();
        String name = request.robot();
        List<Object> args = request.arguments();

        return switch (cmd) {
            case "launch" -> new LaunchCommand(name, args);
//            case "look" -> new LookCommand();
//            case "dump" -> new DumpCommand();
//            case "state" -> new StateCommand(name);
//            case "forward" -> new ForwardCommand(name, args);
//            case "left" -> new LeftCommand(name);
//            case "right" -> new RightCommand(name);
//            case "fire" -> new FireCommand(name);
//            case "robots" -> new RobotsCommand();


            default -> throw new IllegalArgumentException("Unsupported command: " + cmd);
        };
    }
}
