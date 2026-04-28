package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.server.World;

import java.util.Map;

public class LaunchCommand extends Command {
    private World world;

    @Override
    public Response execute(Robot target) {
        if (world.isFull()) {
            return Response.error("No more space in this world");
        }

        Map<String, Object> data = world.state(target);

        return Response.ok(data, target.state());
    }
}