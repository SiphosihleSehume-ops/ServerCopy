package za.co.wethinkcode.robots.protocols;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;


/**
 * Represents an immutable response message sent to a client.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private String result;
    private Map<String, Object> data;
    private Map<String, Object> state;


    /**
     * Creates a response with all fields explicitly set.
     *
     * @param result the result of the command, either {@code "OK"} or {@code "ERROR"}
     * @param data   a map of command-specific key-value pairs, or {@code null} if not applicable
     * @param state  a map representing the robot's current state, or {@code null} if not applicable
     */
    public Response(
            String result,
            Map<String, Object> data,
            Map<String, Object> state) {
        this.result = result;
        this.data = data;
        this.state = state;
    }

    /**
     * @return the result of the command,
     * either {@code "OK"} or {@code "ERROR"}
     */
    @JsonProperty("result")
    public String result() {
        return result;
    }

    /**
     * @return a map of command-specific response data,
     * or {@code null} if not applicable
     */
    @JsonProperty("data")
    public Map<String, Object> data() {
        return data;
    }

    /**
     * @return a map representing the robot's current state,
     * or {@code null} if not applicable.
     */
    @JsonProperty("state")
    public Map<String, Object> state() {
        return state;
    }

    /**
     * Creates a successful response with both data and state.
     *
     * @param data  command-specific key-value pairs
     * @param state the robot's current state
     * @return a new {@code Response} with result {@code "OK"}
     */
    public static Response ok(
            Map<String, Object> data,
            Map<String, Object> state
    ) {
        return new Response("OK", data, state);
    }

    /**
     * Creates a successful state-only response for the {@code state} command.
     *
     * @param state the robot's current state
     * @return a new {@code Response} with only the state field populated
     */
    public static Response ok(Map<String, Object> state) {
        return new Response(null, null, state);
    }

    /**
     * Creates an error response with a message.
     *
     * @param message a reason for the error
     * @return a new {@code Response} with result {@code "ERROR"}
     */
    public static Response error(String message) {
        Map<String, Object> data = Map.of(
                "message", message
        );
        return new Response("ERROR", data, null);
    }

}