package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Position;
import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.server.World;
import java.util.List;
import java.util.Map;

public class LaunchCommand extends Command {
    private final List<String> arguments;
//    private Position pos = new Position(5, 4);

    public LaunchCommand(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public Response execute(Robot target, World world) {
        // Added World as a parameter

//        if (world.isFull()) {
//            return Response.error("No more space in this world");
//        }



        // Logic to add robot to the world
       world.addRobot(target);
       System.out.println(world.robotCount());

        // Get world state and robot state for the DTO response
        Map<String, Object> data = Map.of(
                "position", target.getCurrentPosition().toString(),
                "visibility", world.visibility()
        );

        return Response.ok(data, target.state());
    }
}
//
//import za.co.wethinkcode.robots.protocols.Response;
//import za.co.wethinkcode.robots.robot.Robot;
//import za.co.wethinkcode.robots.server.World;
//
//import java.util.Map;
//
//public class LaunchCommand extends Command {
//    private World world = new World()
//
//    @Override
//    public Response execute(Robot target) {
//        if (world.isFull()) {
//            return Response.error("No more space in this world");
//        }
//        //Needs to have access to the World
//
//
//        Map<String, Object> data = world.state(target);
//
//        return Response.ok(data, target.state());
//    }
//}