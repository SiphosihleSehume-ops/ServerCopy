package za.co.wethinkcode.robots.protocols;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


/**
 * Immutable representation of a single point on a grid
 */
public class Position {
    private final int x;
    private final int y;

    @JsonCreator
    public Position(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;
    }

    @Override
    public int hashCode(){
        return Objects.hash(x, y);
    }

    /**
     * Determines whether this position lies within a rectangular boundary
     * The rectangle is defined by its top-left and bottom-right corners in a
     * Cartesian coordinate system where the Y-axis increases upward
     *
     * @param topLeft the top-left corner of the rectangle (minimum X, maximum Y)
     * @param bottomRight the bottom-right corner of the rectangle (maximum X, minimum Y)
     * @return true if this position lies within or on the boundary of the rectangle, false otherwise
     */
    public boolean isIn(Position topLeft, Position bottomRight) {
        boolean withinTop = this.y <= topLeft.getY();
        boolean withinBottom = this.y >= bottomRight.getY();
        boolean withinLeft = this.x >= topLeft.getX();
        boolean withinRight = this.x <= bottomRight.getX();
        return withinTop && withinBottom && withinLeft && withinRight;
    }

    @Override
    public String toString() {
        return "[%d, %d]".formatted(x, y);
    }
}
