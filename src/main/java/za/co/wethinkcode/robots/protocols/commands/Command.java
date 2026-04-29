//package za.co.wethinkcode.robots.protocols.commands;
//
//import za.co.wethinkcode.robots.protocols.Request;
//import za.co.wethinkcode.robots.protocols.Response;
//import za.co.wethinkcode.robots.robot.*;
//import za.co.wethinkcode.robots.server.World;
//import za.co.wethinkcode.robots.server.WorldState;
//
//import java.util.Map;
//

package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.server.World;

/**
 * The base class for all Robot commands.
 * It enforces that every command must implement 'execute'.
 */
public abstract class Command {

    /**
     * Executes the specific logic for a command.
     * @param target The robot performing the action.
     * @param world The shared world where the action takes place.
     * @return A Response DTO to be serialized and sent back to the client.
     */
    public abstract Response execute(Robot target, World world);
}





//public class Command {
//
////     public abstract Response execute(Robot targetRobot);
//
//          private final Request request = new Request();
//          private final String launch = "Launch";
//          public World worldState;
//
//          private final Map<String, String> data = Map.of(
//                  "key1", "value1",
//                  "key2", "value2"
//          );
//
//          public Response execute(Robot target) {
//
//               if (!request.getCommand().equals("Launch")) {
//                    return Response.error("I am not programmed to do that.");
//               }
//
//               if (launch.equalsIgnoreCase("LAUNCH")){
//                   return Response.ok(worldState.state(target), target.state());
//               }
//               else {
//                    return Response.error("I am not programmed to do that.");
//               } //Command is no longer abstract for now: Iteration 1
//          }
//     }
//
//
//
