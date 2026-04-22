package za.co.wethinkcode.robots.protocols;

import java.util.*;

public class Obstacle {
    private final Position topLeft;
    private final Position bottomRight;
    private final ObstacleType type;


    public Obstacle(Position topLeft, Position bottomRight, ObstacleType type){
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.type = type;
    }

    public ObstacleType type() {
        return type;
    }

    public Position topLeft(){
        return topLeft;
    }

    public Position getBottomRight(){
        return bottomRight;
    }


    public boolean contains(Position position){
        return position.isIn(topLeft, bottomRight);
    }

    public boolean overlaps(Obstacle other){
        return this.topLeft.getX() <= other.bottomRight.getX()
                && this.bottomRight.getX() >= other.topLeft.getX()
                && this.topLeft.getY() >= other.bottomRight.getY()
                && this.bottomRight.getY() <= other.topLeft.getY();
    }

}
