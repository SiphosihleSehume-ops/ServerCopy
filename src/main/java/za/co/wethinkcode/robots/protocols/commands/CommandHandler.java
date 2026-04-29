package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Request;

public class CommandHandler {
    public static Command create(Request request) {
        String cmdName = request.getCommand().toLowerCase();

        return switch (cmdName) {
            case "launch" -> new LaunchCommand(request.getArguments());
//            case "look" -> new LookCommand();
            default -> throw new IllegalArgumentException("Unsupported command: " + cmdName);
        };
    }
}