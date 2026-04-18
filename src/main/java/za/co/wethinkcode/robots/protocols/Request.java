package za.co.wethinkcode.robots.protocols;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

//Known as DTOs: Data Transfer Objects
public class Request {
   //@JsonProperty
    // Make use of the Jackson annotations here
    @JsonProperty("robot")
    private String robotName;

    @JsonProperty("command")
    private String command;

    @JsonProperty("arguments")
    private List<String> arguments;

    public Request() {} //Jackson needs a no-argument constructor

    //Method Overloading
    public Request(String robot, String command, List<String> args) {
        this.robotName = robot;
        this.command = command;
        this.arguments = args;
    }

    //Jackson does need your Getters and Setters
    //Getters
    public String getRobotName() {
        return robotName;
    }

    public String getCommand () {
        return command;
    }

    public List<String> getArguments () {
        return arguments;
    }

    //Setters
    public void setRobotName(String name) {
        this.robotName = name;
    }

    public void setCommand(String cmd) {
        this.command = cmd;
    }

    public void setArguments(List<String> args) {
        this.arguments = args;
    }

}
