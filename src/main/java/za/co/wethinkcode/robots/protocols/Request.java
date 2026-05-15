package za.co.wethinkcode.robots.protocols;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;

/**
 * Represents a request message from a client
 *
 * @param robot the name of the robot sending the command
 * @param command the name of the command to execute.
 * @param arguments the arguments for the command.
 */

public record Request(
        @JsonProperty("robot")
        String robot,
        @JsonProperty("command")
        String command,
        @JsonProperty("arguments")
        List<Object> arguments)
{
}
