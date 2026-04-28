package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Request;
import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.robot.*;
import za.co.wethinkcode.robots.server.World;
import za.co.wethinkcode.robots.server.WorldState;

import java.util.Map;

public class Command {

//     public abstract Response execute(Robot targetRobot);

          private final Request request = new Request();
          private final String launch = "Launch";
          public World worldState;

          private final Map<String, String> data = Map.of(
                  "key1", "value1",
                  "key2", "value2"
          );

          public Response execute(Robot target) {

               if (!request.getCommand().equals("Launch")) {
                    return Response.error("I am not programmed to do that.");
               }

               if (launch.equalsIgnoreCase("LAUNCH")){
                   return Response.ok(worldState.state(target), target.state());
               }
               else {
                    return Response.error("I am not programmed to do that.");
               } //Command is no longer abstract for now: Iteration 1 
          }
     }



