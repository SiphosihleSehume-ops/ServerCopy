package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.server.World;
import java.util.Map;

public class DumpCommand extends Command{

    @Override
    public Response execute(Robot robot, World world) {
        int robotCount = world.getRobotPositions().size();
        int obstacleCount = world.getRobotPositions().size();

        Map<String, Object> obstacle = Map.of(
                "Obstacle Count: ", obstacleCount
        );

        Map<String, Object> botCount = Map.of(
                "Robot Count: ", robotCount
        );

        return Response.ok(obstacle, botCount);
    }

}

//public class DumpCommand extends Command {
//
//    @Override
//    public Response execute(Robot target, World world) {
//
//        Map<String, Object> data = Map.of(
//                "robots", world.getAllRobots(),
//                "obstacles", world.getObstacles(),
//                "size", world.getSize()
//        );
//
//        return Response.ok(data, target != null ? target.state() : null);
//    }
//}
