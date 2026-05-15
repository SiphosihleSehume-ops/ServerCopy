package za.co.wethinkcode.robots.server;

import java.util.*;
import za.co.wethinkcode.robots.protocols.*;
import za.co.wethinkcode.robots.robot.*;
import za.co.wethinkcode.robots.protocols.config.*;
import za.co.wethinkcode.robots.protocols.config.ConfigLoader;
/**
 * The shared game world.
 *
 * ADDED: resolveShot(Robot shooter) — scans the shooter's line of sight
 *        within visibility range and returns the first robot hit, or null.
 */
public class World {
    private final int width;
    private final int height;
    private final int visibility;
    private final int shieldMax;
    private final int reloadTime;
    private final int repairTime;
    private final Map<Position, Robot> robotPositions = new HashMap<>();
    private final ArrayList<Obstacle> obstacles       = new ArrayList<>();

    public World(Config settings) {
        this.height     = settings.height();
        this.width      = settings.width();
        this.visibility = settings.visibility();
        this.shieldMax  = settings.shieldMax();
        this.reloadTime = settings.reloadTime();
        this.repairTime = settings.repairTime();
    }


    public int visibility()  { return visibility; }
    public int shieldMax()   { return shieldMax; }
    public int reloadTime()  { return reloadTime; }
    public int repairTime()  { return repairTime; }


    public Set<Robot> robots() {
        return new HashSet<>(robotPositions.values());
    }

    public int robotCount()    { return robotPositions.size(); }
    public int obstacleCount() { return obstacles.size(); }

    public synchronized void addRobot(Robot robot) {
        if (robotNameTaken(robot.name())) {
            throw new IllegalArgumentException(robot.name() + " has been taken");
        }
        Position initialRobotPosition = generatePosition();
        robot.newPosition(initialRobotPosition);
        robotPositions.put(initialRobotPosition, robot);
    }

    public boolean robotNameTaken(String robotName) {
        for (Robot r : robots()) {
            if (r.name().equals(robotName)) return true;
        }
        return false;
    }

    public synchronized boolean removeRobot(Robot robot) {
        if (robot.currentPosition() != null) {
            return robotPositions.remove(robot.currentPosition(), robot);
        }
        return false;
    }

    public synchronized String tryMoveRobot(Robot robot, Position newPosition) {
        Position oldPosition = robot.currentPosition();

        if (robot.isBusy())   return robot.name() + " is Busy";
        if (!robot.isAlive()) return robot.name() + " is Dead :(";

        String result = found(newPosition).name();
        if (!result.equals("NOTHING")) {
            return robot.name() + " encountered " + result;
        }

        robotPositions.remove(oldPosition);
        robot.newPosition(newPosition);
        robotPositions.put(newPosition, robot);
        return "Done";
    }

    public Robot findRobotByName(String name) {
        for (Robot bot : robots()) {
            if (bot.name().equalsIgnoreCase(name)) return bot;
        }
        return null;
    }

    // Shot resolution logic
    /**
     * Resolves a shot fired by {@code shooter} in the direction they are facing.
     * Scans step-by-step up to {@code visibility} squares.
     * Returns the first robot hit, or {@code null} if the shot missed or was
     * blocked by an obstacle.
     *
     * @param shooter the robot that fired
     * @return the hit robot, or null
     */
    public synchronized Robot resolveShot(Robot shooter) {
        Position origin    = shooter.currentPosition();
        Direction facing   = shooter.currentDirection();
        int       dx       = facing.dx();   // Direction must expose dx/dy
        int       dy       = facing.dy();

        for (int step = 1; step <= visibility; step++) {
            int x = origin.getX() + dx * step;
            int y = origin.getY() + dy * step;
            Position cell = new Position(x, y);

            // Stop at world edge
            if (!isInsideWorld(cell)) break;

            // Stop at path-blocking obstacle (shot is absorbed)
            if (inPathBlockingObstacle(cell)) break;

            // Hit a robot?
            Robot victim = robotPositions.get(cell);
            if (victim != null && victim.isAlive()) {
                return victim;
            }
        }
        return null;
    }

    // World geometry

    public WorldObjects found(Position pos) {
        boolean northOrSouth = pos.getX() == northernEdge() || pos.getX() == southernEdge();
        boolean eastOrWest   = pos.getY() == easternEdge()  || pos.getY() == westernEdge();

        if (northOrSouth || eastOrWest)       return WorldObjects.EDGE;
        if (robotPositions.containsKey(pos))  return WorldObjects.ROBOT;
        if (inPathBlockingObstacle(pos))      return WorldObjects.OBSTACLES;
        return WorldObjects.NOTHING;
    }

    public void loadMap(WorldMap map) { /* future */ }

    private boolean isOccupied(Position pos) {
        return robotPositions.containsKey(pos) || inPathBlockingObstacle(pos);
    }

    private boolean isInsideWorld(Position pos) {
        return pos.isIn(topLeft(), bottomRight());
    }

    private int northernEdge() { return topLeft().getX(); }
    private int westernEdge()  { return topLeft().getY(); }
    private int easternEdge()  { return bottomRight().getY(); }
    private int southernEdge() { return bottomRight().getX(); }

    public Position topLeft() {
        int x = width  % 2 != 0 ? width  - 1 : width;
        int y = height % 2 != 0 ? height - 1 : height;
        return new Position(-x / 2, y / 2);
    }

    public Position bottomRight() {
        int x = width  % 2 != 0 ? width  - 1 : width;
        int y = height % 2 != 0 ? height - 1 : height;
        return new Position(x / 2, -y / 2);
    }

    private boolean inPathBlockingObstacle(Position pos) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.contains(pos) && obstacle.type().blocksPath()) return true;
        }
        return false;
    }

    private synchronized Position generatePosition() {
        Random r   = new Random();
        Position min = topLeft();
        Position max = bottomRight();

        for (int i = 0; i < 100; i++) {
            int x = r.nextInt(max.getX() - min.getX() + 1) + min.getX();
            int y = r.nextInt(min.getY() - max.getY() + 1) + max.getY();
            Position candidate = new Position(x, y);
            if (!isOccupied(candidate) && isInsideWorld(candidate)) return candidate;
        }
        throw new IllegalStateException("Could not find a free position after 100 attempts.");
    }

    // Dump Server-side operation view

    /**
     * Returns a human-readable snapshot of the world for the server operator.
     */
    public String dump() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== WORLD DUMP ===\n");
        sb.append("Size      : ").append(width).append("x").append(height).append("\n");
        sb.append("Visibility: ").append(visibility).append("\n");
        sb.append("Obstacles : ").append(obstacleCount()).append("\n");
        sb.append("Robots    : ").append(robotCount()).append("\n");
        for (Robot r : robots()) {
            sb.append("  ").append(r).append("\n");
        }
        sb.append("==================\n");
        return sb.toString();
    }
}