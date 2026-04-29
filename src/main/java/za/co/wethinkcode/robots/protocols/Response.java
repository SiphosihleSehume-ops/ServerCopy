package za.co.wethinkcode.robots.protocols;

import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.robot.RobotStatus;
import za.co.wethinkcode.robots.server.WorldState;

import java.util.Map;


public class Response {
//    private World world = new World(Config config);
    private String result;
    private Map<String, Object> data; //Current robot state
    private Map<String, Object> worldState;

    public Response(String result, Map<String, Object> data) {
        this.result = result;
        this.data = data; //Object returns message if the Request is not passed in correctly
    }

    public Response(String result, Map<String, Object> data, Map<String, Object> state) {
        this.result = result; // "OK" if result successful
        this.data = data;
        this.worldState = state;
    }

    public Response(String message) {
        this.result = message;
    }

    public Response() {}

    //Factory methods
    public static Response ok(Map<String, Object> data, Map<String, Object> target) {
        Response response = new Response();
        response.setResult("OK");
        response.setStatus(data);
        response.setRobotState(target);
        return response;
    }

    public static Response error(String message) {
        Response response = new Response();
        response.setResult("ERROR");
        response.setStatus(Map.of("message", "Forward")); //Replace with Map<> object if needed
        // state remains null for errors per your spec
        return response;
    }



    //Getters
    public String getResult() {
        return result;
    }

    public Map<String, Object> getStatus() {
        return data;
    }

    public Map<String, Object> getRobotState() {
        return worldState;
    }

    //Setters
    public void setResult(String result) {
        this.result = result;
    }

    public void setStatus(Map<String, Object> status) {
        this.data = status;
    }

    public void setRobotState(Map<String, Object> state) {this.worldState = state; }

}
