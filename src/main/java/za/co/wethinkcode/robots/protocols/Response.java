package za.co.wethinkcode.robots.protocols;

import za.co.wethinkcode.robots.server.RobotState;
import za.co.wethinkcode.robots.server.World;
import za.co.wethinkcode.robots.server.WorldState;

import java.util.Map;


public class Response {
//    private World world = new World(Config config);
    private String result;
    private Message data; //Current robot state
    private RobotState robotState;

    public Response(String result, Message data) {
        this.result = result;
        this.data = data; //Object returns message if the Request is not passed in correctly
    }

    public Response(String result, Message data, RobotState state) {
        this.result = result; // "OK" if result successful
        this.data = data;
        this.robotState = state;
    }

    public Response() {}

    //Factory methods
    public static Response ok(Message data, RobotState state) {
        Response response = new Response();
        response.setResult("OK");
        response.setData(data);
        response.setState(state);
        return response;
    }

    public static Response error(String message) {
        Response response = new Response();
        response.setResult("ERROR");
        response.setData(Map.of("message", message)); //Replace with Map<> object if needed
        // state remains null for errors per your spec
        return response;
    }



    //Getters
    public String getResult() {
        return result;
    }

    public Message getStatus() {
        return data;
    }

    public RobotState getRobotState() {
        return robotState;
    }

    //Setters
    public void setResult(String result) {
        this.result = result;
    }

    public void setData(Message status) {
        this.data = status;
    }

    public void setState(RobotState state) {this.robotState = state; }

}
