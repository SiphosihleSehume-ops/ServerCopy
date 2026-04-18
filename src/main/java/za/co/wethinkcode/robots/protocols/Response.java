package za.co.wethinkcode.robots.protocols;

import za.co.wethinkcode.robots.server.RobotState;

public class Response {
    private String result;
    private RobotState robotState; //Current robot state

    public Response(String result, Object data, RobotState status) {
        this.result = result;
        this.robotState = status;
    }

    //Getters
    public String getResult() {
        return result;
    }

    public RobotState getStatus() {
        return robotState;
    }

    //Setters
    public void setResult(String result) {
        this.result = result;
    }

    public void setStatus(RobotState status) {
        this.robotState = status;
    }

}
