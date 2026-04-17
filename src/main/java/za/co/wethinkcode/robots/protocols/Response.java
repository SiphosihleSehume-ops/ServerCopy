package za.co.wethinkcode.robots.protocols;

import za.co.wethinkcode.robots.server.RobotState;

public class Response {
    private String result;
    private Object data; // Can be a Map, RobotObject (maybe)
    private RobotState robotState; //Current robot state

    public Response(String result, Object data, RobotState status) {
        this.result = result;
        this.data = data;
        this.robotState = status;
    }

    //Getters
    public String getResult() {
        return result;
    }

    public Object getData() {
        return data;
    }

    public RobotState getStatus() {
        return robotState;
    }

    //Setters
    public void setResult(String result) {
        this.result = result;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setStatus(RobotState status) {
        this.robotState = status;
    }

}
