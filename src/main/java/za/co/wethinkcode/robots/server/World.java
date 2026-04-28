package za.co.wethinkcode.robots.server;

import za.co.wethinkcode.robots.protocols.ObstacleType;
import za.co.wethinkcode.robots.protocols.config.Config;
import za.co.wethinkcode.robots.protocols.Obstacle;
import za.co.wethinkcode.robots.protocols.Position;
import za.co.wethinkcode.robots.robot.Robot;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.*;


public class World {
    private final int width;
    private final int height;
    private final int visibility;
    private final int shieldMax;
    private final int reloadTime;
    private final int repairTime;
    private final int MAX_CLIENTS = 50; //Should be later passed in through config file
    private final Map<Position, Robot> robotPositions = new HashMap<>();
    private final ArrayList<Obstacle> obstacles = new ArrayList<>();



    public World(Config settings){
        this.height = settings.height();
        this.width = settings.width();
        this.visibility = settings.visibility();
        this.shieldMax = settings.shieldMax();
        this.reloadTime = settings.reloadTime();
        this.repairTime = settings.repairTime();
    }

    public World(
            int width,
            int height,
            int visibility,
            int shieldMax,
            int reloadTime,
            int repairTime
    ){
        this.width = width;
        this.height = height;
        this.visibility = visibility;
        this.shieldMax = shieldMax;
        this.repairTime = repairTime;
        this.reloadTime = reloadTime;

    }

    //Changed to getVisibility
    public int getVisibility(){
        return visibility;
    }

    public int shieldMax(){
        return shieldMax;
    }

    public int reloadTime(){
        return reloadTime;
    }

    public int repairTime() {
        return repairTime;
    }

    // Manage Robots
    public Set<Robot> robots() {
        return new HashSet<>(robotPositions.values());
    }

    public int robotCount() {
        return robotPositions.size();
    }

    private Position generatePosition(){
        Random r = new Random();
        int x = 0, y = 0;
        Position min = topLeft();
        Position max = bottomRight();
        Position possiblePosition = new Position(x, y);

        boolean found = false;
        while (!found){
            x = r.nextInt(max.getX() - min.getX() + 1) + min.getX();
            y = r.nextInt(min.getY() - max.getY() + 1) + max.getY();
            possiblePosition = new Position(x, y);
            if (!isOccupied(possiblePosition)
                    && isInsideWorld(possiblePosition)){
                found = true;
            }
        }
        return possiblePosition;
    }

    public void addRobot(Robot robot, Position position) {
        if (robotNameTaken(robot)){
            throw new IllegalArgumentException(robot.getBotName() + " has been taken");
        }

        Position intialRobotPosition = generatePosition();
        robot.updatePosition(position.getX(), position.getY());
        robotPositions.put(intialRobotPosition, robot);
    }

    private boolean robotNameTaken(Robot robot){
        for (Robot r: robots()){
            if (r.getBotName().equals(robot.getBotName())){
                return true;
            }
        }
        return false;
    }

    public boolean removeRobot(Robot robot){
        if (robot.getCurrentPosition() != null){
            Position pos = robot.getCurrentPosition();
            return robotPositions.remove(pos, robot);
        }
        return false;
    }

    public boolean tryMoveRobot(Robot robot, Position newPosition){
        Position oldPosition = robot.getCurrentPosition();

        if (oldPosition == null || robot.isBusy() || !robot.isAlive()){
            return false;
        }

        if (!isInsideWorld(newPosition)){
            return false;
        }

        if (isOccupied(newPosition)){
            return false;
        }

        robotPositions.remove(oldPosition);
        robot.updatePosition(newPosition.getX(), newPosition.getY());
        robotPositions.put(newPosition, robot);

        return true;
    }

    // Obstacle management
    public boolean addObstacle(Obstacle obstacle){
        for (Obstacle existing: obstacles){
            if (obstacle.overlaps(existing)){
                return false;
            }
        }
        obstacles.add(obstacle);
        return true;
    }

    public void createRandomObstacles(int nrObstacles){
        Random r = new Random();
        ObstacleType[] values = ObstacleType.values();
        int count = 0;
        while (count < nrObstacles){
            int x1 = r.nextInt(bottomRight().getX() - topLeft().getX()) + topLeft().getX();
            int x2 = r.nextInt(bottomRight().getX() - topLeft().getX()) + topLeft().getX();
            int y1 = r.nextInt(topLeft().getY() - bottomRight().getY()) + bottomRight().getY();
            int y2 = r.nextInt(topLeft().getY() - bottomRight().getY()) + bottomRight().getY();
            ObstacleType type = values[r.nextInt(values.length)];
            Position topLeft = new Position(max(x1, x2 ), max( y1,y2 ));
            Position bottomRight = new Position(min(x1, x2 ), min(y1, y2));
            Obstacle ob = new Obstacle(topLeft, bottomRight, type);

            if (addObstacle(ob)){
                count ++;
            }

        }
    }

    /**
     * Determines if the World has reached full capacity
     *
     * @return {@code true} if the World is full
     *         {@code false} if the World has space
     * */
    public boolean isFull() {
        return robotPositions.size() <= MAX_CLIENTS;
    }

    public boolean isOccupied(Position pos) {
        return robotPositions.containsKey(pos) || inPathBlockingObstacle(pos);
    }

    public boolean isInsideWorld(Position pos) {
        return pos.isIn(topLeft(), bottomRight());
    }

    public Position topLeft(){
        int x = width, y = height;

        if (width % 2 == 0){
            x -= 1;
        }

        if (height % 2 == 0){
            y -= 1;
        }
        return new Position((-x/2), (y/2));
    }

    public Position bottomRight(){
        int x = width, y = height;
        if (width % 2 == 0){
            x -= 1;
        }

        if (height % 2 == 0){
            y -= 1;
        }
        return new Position((x/2), (-y/2));
    }

    public boolean inPathBlockingObstacle(Position pos){
        for (Obstacle obstacle: obstacles){
            if (obstacle.contains(pos) && obstacle.type().blocksPath()){
                return true;
            }
        }
        return false;
    }

    public Map<String, Object> state(Robot robot){
        return Map.of(
                "position", robot.getCurrentPosition().toString(),
                "visibility", visibility,
                "reload", reloadTime,
                "repair", repairTime,
                "shields", shieldMax
        );
//        return new WorldState(position,
//                visibility,
//                repairTime,
//                reloadTime,
//                shieldMax);
    }

}




































//package za.co.wethinkcode.robots.server;
//
//import za.co.wethinkcode.robots.protocols.*;
//
//import java.lang.reflect.Array;
//import java.time.Duration;
//import static java.lang.Math.max;
//import static java.lang.Math.min;
//import java.util.*;
//
//
//public class World {
//
//    private int width;
//    private int height;
//    private int visibility;
//    private int shieldMax;
//    private int reloadTime;
//    private int repairTime;
//    private final int MAX_CLIENTS = 50;
//    private final Map<Position, Robot> robotPositions = new HashMap<>();
//    private final ArrayList<Obstacle> obstacles = new ArrayList<>();
//
//    public World(){}
//
//    public World(Config settings){
//        this.height = settings.height();
//        this.width = settings.width();
//        this.visibility = settings.visibility();
//        this.shieldMax = settings.shieldMax();
//        this.reloadTime = settings.reloadTime();
//        this.repairTime = settings.repairTime();
//    }
//
//    public World(
//            int width,
//            int height,
//            int visibility,
//            int shieldMax,
//            int reloadTime,
//            int repairTime
//    ){
//        this.width = width;
//        this.height = height;
//        this.visibility = visibility;
//        this.shieldMax = shieldMax;
//        this.repairTime = repairTime;
//        this.reloadTime = reloadTime;
//
//
//    }
//
//    public int getVisibility(){
//        return visibility;
//    } //Renamed to 'get'
//
//    public int getShieldMax(){
//        return shieldMax;
//    }
//
//    public int reloadTime(){
//        return reloadTime;
//    }
//
//    public int repairTime() {
//        return repairTime;
//    }
//
//    // Manage Robots
//    public Set<Robot> robots() {
//        return new HashSet<>(robotPositions.values());
//    }
//
//    public int robotCount() {
//        return robotPositions.size();
//    }
//
//    //Added this method
//    public boolean isFull() {
//        return robotCount() <= MAX_CLIENTS;
//    }
//
//    public void addRobot(Robot robot, Position position) { //Should return a string
//        if (robotNameTaken(robot)){
//            throw new IllegalArgumentException(robot.name() + " has been taken");
//        }
//
//        if (!isInsideWorld(position)) {
//            throw new IllegalArgumentException("Position not allowed");
//        }
//
//        if (isOccupied(position)) {
//            throw new IllegalStateException("Position already occupied");
//        }
//
//        robot.setPosition(position);
//        robotPositions.put(position, robot);
//
//    }
//
//    private boolean robotNameTaken(Robot robot){
//        for (Robot r: robots()){
//            if (r.name().equals(robot.name())){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean removeRobot(Robot robot){
//        Position pos = robot.getPosition();
//        return robotPositions.remove(pos, robot);
//    }
//
//    public boolean tryMoveRobot(Robot robot, Position newPosition){
//        Position oldPosition = robot.getPosition();
//
//        if (!isInsideWorld(newPosition)){
//            return false;
//        }
//
//        if (isOccupied(newPosition)){
//            return false;
//        }
//
//        robotPositions.remove(oldPosition);
//        robot.setPosition(newPosition);
//        robotPositions.put(newPosition, robot);
//
//        return true;
//    }
//
//    // Obstacle management
//    public boolean addObstacle(Obstacle obstacle){
//        for (Obstacle existing: obstacles){
//            if (obstacle.overlaps(existing)){
//                return false;
//            }
//        }
//        obstacles.add(obstacle);
//        return true;
//    }
//
//    public void createRandomObstacles(int nrObstacles){
//        Random r = new Random();
//        ObstacleType[] values;
//        values = ObstacleType.values();
//        int count = 0;
//        while (count < nrObstacles){
//            int x1 = r.nextInt(bottomRight().getX() - topLeft().getX()) + topLeft().getX();
//            int x2 = r.nextInt(bottomRight().getX() - topLeft().getX()) + topLeft().getX();
//            int y1 = r.nextInt(topLeft().getY() - bottomRight().getY()) + bottomRight().getY();
//            int y2 = r.nextInt(topLeft().getY() - bottomRight().getY()) + bottomRight().getY();
//            ObstacleType type = values[r.nextInt(values.length)];
//            Position topLeft = new Position(max(x1, x2 ), max( y1,y2 ));
//            Position bottomRight = new Position(min(x1, x2 ), min(y1, y2));
//            Obstacle ob = new Obstacle(topLeft, bottomRight, type);
//
//            if (addObstacle(ob)){
//                count ++;
//            }
//
//        }
//    }
//
//    public boolean isOccupied(Position pos) {
//        return robotPositions.containsKey(pos) || inPathBlockingObstacle(pos);
//    }
//
//    public boolean isInsideWorld(Position pos) {
//        return pos.isIn(topLeft(), bottomRight());
//    }
//
//    public Position topLeft(){
//        int x = width, y = height;
//
//        if (width % 2 == 0){
//            x -= 1;
//        }
//
//        if (height % 2 == 0){
//            y -= 1;
//        }
//        return new Position((-x/2), (y/2));
//    }
//
//    public Position bottomRight(){
//        int x = width, y = height;
//        if (width % 2 == 0){
//            x -= 1;
//        }
//
//        if (height % 2 == 0){
//            y -= 1;
//        }
//        return new Position((x/2), (-y/2));
//    }
//
//    public boolean inPathBlockingObstacle(Position pos){
//        for (Obstacle obstacle: obstacles){
//            if (obstacle.contains(pos) && obstacle.type().blocksPath()){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public WorldState state(Robot robot){
//        int x = robot.getPosition().getX();
//        int y = robot.getPosition().getY();
//        int[] position = new int[] {x, y};
//
//        return new WorldState(position,
//                visibility,
//                repairTime,
//                reloadTime,
//                shieldMax);
//    }
//}
//
////public class World {
////    /* Create a World with hard-coded obstacles
////        - Render obstacles
////        - Render Robots
////    * */
////}
//
///*
//* How Can We Plan The Whole Thing Out?
//* `Response` should be an Object
//   - Before adding a `Robot` to the world you first need to check if it is safe
//     for you to add one. M0
//*
//* */
