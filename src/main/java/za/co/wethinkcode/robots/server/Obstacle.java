package za.co.wethinkcode.robots.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import za.co.wethinkcode.robots.protocols.*;

import java.util.Objects;

public class Obstacle {
    private final Position topLeft;
    private final Position bottomRight;
    private final ObstacleType type;

    /**
     * Creates an instance of an obstacle
     * @param topLeft the top left corner of the obstacle
     * @param bottomRight the bottom right corner of the obstacle
     * @param type the type of obstacle
     */
    @JsonCreator
    public Obstacle(
            @JsonProperty("topLeft") Position topLeft,
            @JsonProperty("bottomRight") Position bottomRight,
            @JsonProperty("type") ObstacleType type)
    {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.type = type;
    }

    public Obstacle(int x1, int y1, int x2, int y2,
                    ObstacleType type){
        this(new Position(x1, y1), new Position(x2, y2), type);

    }

    public ObstacleType type() {
        return type;
    }

    public Position topLeft(){
        return topLeft;
    }

    public Position bottomRight(){
        return bottomRight;
    }

    /**
     * Determines whether the given position falls within the bounds of this obstacle
     * @param position the position to check
     * @return true if point within the obstacle, otherwise false
     */
    public boolean contains(Position position){
        return position.isIn(topLeft, bottomRight);
    }

    /**
     * Determines whether this obstacle overlaps with another obstacle
     * @param other obstacle to check
     * @return true if the obstacles overlap, otherwise false
     */
    public boolean overlaps(Obstacle other){
        return this.topLeft.getX() < other.bottomRight.getX()
                && this.bottomRight.getX() > other.topLeft.getX()
                && this.topLeft.getY() > other.bottomRight.getY()
                && this.bottomRight.getY() < other.topLeft.getY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Obstacle obstacle = (Obstacle) o;
        return Objects.equals(topLeft, obstacle.topLeft) &&
                Objects.equals(bottomRight, obstacle.bottomRight) &&
                type == obstacle.type;
    }


}

