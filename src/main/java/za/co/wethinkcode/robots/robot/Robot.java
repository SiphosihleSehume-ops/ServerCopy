package za.co.wethinkcode.robots.robot;


import za.co.wethinkcode.robots.protocols.Position;

public class Robot {
    private String botName;
    private RobotType botType;
    private Direction currentDirection;
    private Position currentPosition;
    private RobotStatus botStatus;


    public Robot(String name, RobotType type) {
        this.botName = name;
        this.botType = type;
        this.currentPosition = new Position(0,0);
        this.currentDirection = Direction.NORTH;
        this.botStatus = new RobotStatus(type);
    }
//    Some accessor or getter methods

    public String getBotName() {
        return botName;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }
    public RobotType getBotType() {
        return botType;
    }
    public Position getCurrentPosition() {
        return currentPosition;
    }

    public RobotStatus getBotStatus() {
        return botStatus;
    }

//    I was thinking why must the Robot handle the turning? why cant there be something else that would handle that?
//    What handles turning...what is affected by it? -> Direction...

    public void turnLeft() { this.currentDirection = currentDirection.turnLeft();}
    public void turnRight() { this.currentDirection = currentDirection.turnRight();}

    public void updatePosition(Position position) {
        this.currentPosition.setX(position.getX());
        this.currentPosition.setY(position.getX());
    }

    public void updateResponse(int x, int y, Direction newDirection, String status, int shields, int shots) {
        this.currentPosition.setX(x);
        this.currentPosition.setY(y);
        this.currentDirection = newDirection;
        this.botStatus.setStatus(status);
        this.botStatus.setShields(shields);
        this.botStatus.setShots(shots);

    }
    public boolean isAlive() {return botStatus.isAlive();}
    public boolean canFire() {return botStatus.hasShots();}
    public boolean isBusy() {return  botStatus.equals(RobotStatus.Status.DEAD);}


}




















































//package za.co.wethinkcode.robots.server;
//
//import za.co.wethinkcode.robots.protocols.*;
//import za.co.wethinkcode.robots.protocols.commands.Command;
//
//import java.util.Map;
//import java.util.Random;
//
//public class Robot {
//    private Position position;
//    private Direction currentDirection;
//    private final String status; //Switched to string for now
//    private final String name;
//    private final World aWorld;
//    private final int shieldMax; //Added this field
//
//    public Robot(String name, World aWorld, int shieldMax) {
//        this.name = name;
//        this.shieldMax = shieldMax;
//        this.status = "NORMAL";
//        this.position = generatePosition();
//        this.currentDirection = Direction.UP;
//        this.aWorld = aWorld;
//    }
//
//    public String name(){
//        return name;
//    }
////    public Status getStatus() {
////        return status;
////    }
//
//    //Added this method
//    public int getShieldMax() {
//        return shieldMax;
//    }
//
//    //Added this code
//    public Map<String, Object> getCurrent() {
//        return Map.of(
//                "position", "[ " + position.getX() + ", " + position.getY() + "]",
//                "direction", "NORTH",
//                "shields", getShieldMax(),
//
//        );
//    }
//
//    public Direction getCurrentDirection() {
//        return currentDirection;
//    }
//
//    public Position getPosition() {
//        return position;
//    }
//
//    public Direction currentDirection(){
//        return currentDirection;
//    }
//
////    public void setStatus(Status newStatus) {
////        status = newStatus;
////    }
//
//    //Changed return type to Response
//    public Response handleCommand(Command command) {
//        return command.execute(this);
//    }
//
//    private Position generatePosition(){
//        Random r = new Random();
//        int x = 0, y = 0;
//        Position min = aWorld.topLeft();
//        Position max = aWorld.bottomRight();
//        Position possiblePosition = new Position(x, y);
//
//        boolean found = false;
//        while (!found){
//            x = r.nextInt(max.getX() - min.getX() + 1) + min.getX();
//            y = r.nextInt(min.getY() - max.getY() + 1) + max.getY();
//            possiblePosition = new Position(x, y);
//            if (!aWorld.isOccupied(possiblePosition)
//                    && aWorld.isInsideWorld(possiblePosition)){
//                found = true;
//            }
//        }
//        return possiblePosition;
//    }
//
//    public void changeDirection(Direction newDirection){
//        if (newDirection != null) {
//            this.currentDirection = newDirection;
//        }
//
//    }
//
//    public void setPosition(Position newPosition){
//        this.position = newPosition;
//    }
//
//
//
//    @Override
//    public String toString() {
//        return "[" + this.position.getX() + "," + this.position.getY() + "] "
//                + this.name + "> " + this.status;
//    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////public class Robot {
////    /* Create a Robot:
////        - String name
////        - String arguments\
////        - int shield
////        - int ammunition
////
////    * Basic Getter methods
////    * Add Jackson Annotations when you send it
////    * */
////}
//
