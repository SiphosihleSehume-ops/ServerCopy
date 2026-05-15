package za.co.wethinkcode.robots.protocols;

public enum Direction {
    NORTH( 0,  1),
    EAST ( 1,  0),
    SOUTH( 0, -1),
    WEST (-1,  0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int dx() { return dx; }
    public int dy() { return dy; }

    public Direction turnLeft() {
        switch (this) {
            case NORTH -> { return WEST;  }
            case WEST  -> { return SOUTH; }
            case SOUTH -> { return EAST;  }
            case EAST  -> { return NORTH; }
            default    -> { return this;  }
        }
    }

    public Direction turnRight() {
        switch (this) {
            case NORTH -> { return EAST;  }
            case EAST  -> { return SOUTH; }
            case SOUTH -> { return WEST;  }
            case WEST  -> { return NORTH; }
            default    -> { return this;  }
        }
    }
}
