package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.robot.RobotType;
import za.co.wethinkcode.robots.server.World;

import java.util.List;
import java.util.Map;

public class LaunchCommand extends Command {

    public LaunchCommand(String name, List<Object> args) {
        super(name, args);
    }

    @Override
    public Response execute(World world) {
        // Validate Robot name existence in World
        if (world.robotNameTaken(robotName())) {
            return Response.error("Robot name taken");
        }

        Robot target = world.findRobotByName(robotName());

        // Validate whether Robot has been launched already
        if (target != null) {
            return Response.error("Robot already Launched");
        }

        // Instantiating a new Robot object
        Robot newRobot = new Robot(robotName(),
                RobotType.fromString((String) arguments()
                        .get(0))
        );

        world.addRobot(newRobot);

        Map<String, Object> data = Map.of(
                "position", newRobot.currentPosition(),
                "visibility", world.visibility(),
                "reload", world.reloadTime(),
                "repair", world.repairTime(),
                "shields", world.shieldMax()
        );
        return Response.ok(data, newRobot.state());
    }
}