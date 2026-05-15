package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.server.World;
import java.util.List;

/**
 * The base class for all Robot commands.
 * It enforces that every command must implement 'execute'.
 */
public abstract class Command{
    private String robotName;
    private List<Object> arguments;

    public Command(String robotName) {
        this.robotName = robotName;
    }

    public Command(String robotName, List<Object> arguments) {
        this.robotName = robotName;
        this.arguments = arguments;
    }

    public Command() {}

    public String robotName() {
        return robotName;
    }

    public List<Object> arguments() {
        return arguments;
    }

    /**
     * Executes the specific logic for a command.
     * @param world The shared world where the action takes place.
     * @return A Response DTO to be serialized and sent back to the client.
     */
    public abstract Response execute(World world);

}