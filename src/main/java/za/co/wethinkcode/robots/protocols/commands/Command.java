package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Request;
import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.server.World;

import java.util.Map;

public abstract class Command {

//     public abstract Response execute(Robot targetRobot);

          private Request request = new Request();
          private final String launch = "Launch";

          private final Map<String, String> data = Map.of(
                  "key1", "value1",
                  "key2", "value2"
          );

          public Response execute(Robot target) {

               if (!request.getCommand().equals("Launch")) {
                    return Response.error("I am not programmed to do that.");
               }
               Map<String, Object> state = Map.of(
                       "position", target.getCurrentPosition(),
                       "direction", target.getCurrentDirection(),
                       "shields", target.getShields(),
                       "shots", target.getShots(),
                       "status", target.getStatus()
               );

               if (launch.equalsIgnoreCase("LAUNCH")){
                    return Response.ok(data, state);
               }
               else {
                    return Response.error("I am not programmed to do that.");
               }
          }
     }

}

