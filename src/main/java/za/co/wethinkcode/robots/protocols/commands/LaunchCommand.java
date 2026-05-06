package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.server.World;
import java.util.List;
import java.util.Map;

public class LaunchCommand extends Command {
    private final String robotName;
    private final List<String> arguments;

    public LaunchCommand(String robotName, List<String> arguments) {
        this.robotName = robotName;
        this.arguments = arguments;
    }

    @Override
    public Response execute(Robot target, World world) {

        if (target != null) {
            return Response.error("Robot already launched");
        }

        Robot newRobot = new Robot(robotName, arguments.get(0));
        world.addRobot(newRobot);

        Map<String, Object> data = Map.of(
                "position", newRobot.getCurrentPosition().toString(),
                "visibility", world.visibility()
        );

        return Response.ok(data, newRobot.state());
    }
}

//Revised code logic:
//public class CommandHandler {
//
//    public static Command create(Request request) {
//        String cmd = request.getCommand().toLowerCase();
//
//        return switch (cmd) {
//            case "launch" -> new LaunchCommand(
//                    request.getRobotName(),
//                    request.getArguments()
//            );
//            case "look" -> new LookCommand();
//            case "move" -> new MoveCommand(request.getArguments());
//            case "state" -> new StateCommand();
//            case "dump" -> new DumpCommand();
//            default -> throw new IllegalArgumentException("Unsupported command: " + cmd);
//        };
//    }
//}

//
//public class LaunchCommand extends Command {
//    private final List<String> arguments;
////    private Position pos = new Position(5, 4);
//
//    public LaunchCommand(List<String> arguments) {
//        this.arguments = arguments;
//    }
//
//    @Override
//    public Response execute(Robot target, World world) {
//        // Added World as a parameter
//
//        // Logic to add robot to the world
//       world.addRobot(target);
//       System.out.println(world.robotCount());
//
//        // Get world state and robot state for the DTO response
//        Map<String, Object> data = Map.of(
//                "position", target.getCurrentPosition().toString(),
//                "visibility", world.visibility()
//        );
//
//        return Response.ok(data, target.state());
//    }



