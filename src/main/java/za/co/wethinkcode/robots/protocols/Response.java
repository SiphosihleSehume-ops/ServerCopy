package za.co.wethinkcode.robots.protocols;

import za.co.wethinkcode.robots.server.RobotState;
import za.co.wethinkcode.robots.server.World;
import za.co.wethinkcode.robots.server.WorldState;


public class Response {
//    private World world = new World(Config config);
    private String result;
    private World state = //Current robot state
    private RobotState robotState;

    public Response(String result, World world) {
        this.result = result;
        this.state = world; //Object returns message if the Request is not passed in correctly
    }

    public Response(String result, World world, RobotState state) {
        this.result = result; // "OK" if result successful
        this.state = world;
        this.robotState = state;
    }

    //Getters
    public String getResult() {
        return result;
    }

    public WorldState getStatus() {
        return state;
    }

    public RobotState getRobotState() {
        return robotState;
    }

    //Setters
    public void setResult(String result) {
        this.result = result;
    }

    public void setStatus(World status) {
        this.state = status;
    }

    public void setRobotState(RobotState state) {this.robotState = state; }

}
