package za.co.wethinkcode.robots.server;

import org.eclipse.jgit.api.Status;
import za.co.wethinkcode.robots.protocols.*;

import java.util.Random;

public class Robot {
    private Position position;
    private Direction currentDirection;
    private Status status;
    private String name;
    private final World aWorld;

    public Robot(String name, World aWorld) {
        this.name = name;
        this.status = Status.D;
        this.position = generatePosition();
        this.currentDirection = Direction.UP;
        this.aWorld = aWorld;
    }

    public String name(){
        return name;
    }
    public Status getStatus() {
        return status;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public Position getPosition() {
        return position;
    }

    public Direction currentDirection(){
        return currentDirection;
    }

    public void setStatus(Status newStatus) {
        status = newStatus;
    }

    public boolean handleCommand(Command command) {
        return command.execute(this);
    }

    private Position generatePosition(){
        Random r = new Random();
        int x = 0, y = 0;
        Position min = aWorld.topLeft();
        Position max = aWorld.bottomRight();
        Position possiblePosition = new Position(x, y);

        boolean found = false;
        while (!found){
            x = r.nextInt(max.getX() - min.getX() + 1) + min.getX();
            y = r.nextInt(min.getY() - max.getY() + 1) + max.getY();
            possiblePosition = new Position(x, y);
            if (!aWorld.isOccupied(possiblePosition)
                    && aWorld.isInsideWorld(possiblePosition)){
                found = true;
            }
        }
        return possiblePosition;
    }

    public void changeDirection(Direction newDirection){
        if (newDirection != null) {
            this.currentDirection = newDirection;
        }

    }

    public void setPosition(Position newPosition){
        this.position = newPosition;
    }



    @Override
    public String toString() {
        return "[" + this.position.getX() + "," + this.position.getY() + "] "
                + this.name + "> " + this.status;
    }

}







































//public class Robot {
//    /* Create a Robot:
//        - String name
//        - String arguments\
//        - int shield
//        - int ammunition
//
//    * Basic Getter methods
//    * Add Jackson Annotations when you send it
//    * */
//}

