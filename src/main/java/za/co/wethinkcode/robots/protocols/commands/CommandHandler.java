package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Request;

/**
 * <p>Serves as the Translator. Takes in a raw String like
 * "Launch" and decides which objects to build.</p>
 *
 * @return a new {@link Command} object based on the command name
 */

public class CommandHandler {

    public static Command create(Request request) {
        String cmd = request.getCommand().toLowerCase();

        return switch (cmd) {
            case "launch" -> new LaunchCommand(
                    request.getRobotName(),
                    request.getArguments()
            );
//            case "look" -> new LookCommand();
            case "dump" -> new DumpCommand();
//            case "state" -> new StateCommand();
            default -> throw new IllegalArgumentException("Unsupported command: " + cmd);
        };
    }
}





//public class CommandHandler {
//    public static Command create(Request request) {
//        String cmdName = request.getCommand().toLowerCase();
//
//        return switch (cmdName) {
//            case "launch" -> new LaunchCommand(request.getArguments());
////            case "look" -> new LookCommand();
//            default -> throw new IllegalArgumentException("Unsupported command: " + cmdName);
//        };
//    }
//}

