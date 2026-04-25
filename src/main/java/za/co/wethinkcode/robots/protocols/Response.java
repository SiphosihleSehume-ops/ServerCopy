package za.co.wethinkcode.robots.protocols;

import za.co.wethinkcode.robots.robot.RobotStatus;

import java.util.Map;


public class Response {
//    private World world = new World(Config config);
    private String result;
    private Map<String, String> data; //Current robot state
    private RobotStatus robotState;

    public Response(String result, Map<String, String> data) {
        this.result = result;
        this.data = data; //Object returns message if the Request is not passed in correctly
    }

    public Response(String result, Map<String, String> data, RobotStatus state) {
        this.result = result; // "OK" if result successful
        this.data = data;
        this.robotState = state;
    }

    public Response(String message) {
        this.result = message;
    }

    public Response() {}

    //Factory methods
    public static Response ok(Map<String, String> data, RobotStatus state) {
        Response response = new Response();
        response.setResult("OK");
        response.setData(data);
        response.setState(state);
        return response;
    }

    public static Response error(String message) {
        Response response = new Response();
        response.setResult("ERROR");
        response.setData(Map.of("message", "Placeholder")); //Replace with Map<> object if needed
        // state remains null for errors per your spec
        return response;
    }



    //Getters
    public String getResult() {
        return result;
    }

    public Map<String, String> getStatus() {
        return data;
    }

    public RobotStatus getRobotState() {
        return robotState;
    }

    //Setters
    public void setResult(String result) {
        this.result = result;
    }

    public void setData(Map<String, String> status) {
        this.data = status;
    }

    public void setState(RobotStatus state) {this.robotState = state; }

}
