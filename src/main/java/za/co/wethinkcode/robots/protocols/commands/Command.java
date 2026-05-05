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

    public boolean executeServer(World world){

        return true;
    }
}

/** Ohh The Reason is to avoids having a "God Object":
 * An Object filled wiyth a lot of if/eklif statements which couldfd result
 * in messy code.
 * For Example:
 * if (cmdName.equals("launch")) {
 *             // ... 20 lines of launch logic ...
 *         } else if (cmdName.equals("look")) {
 *            ***
 *         } else if (cmdName.equals("move")) {
 *             ****
 *         }
 */