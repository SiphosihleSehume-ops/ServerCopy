package za.co.wethinkcode.robots.server;

public enum ObstacleType {
    MOUNTAIN{
        public boolean blocksPath() {
            return true;
        }

        public boolean blocksVision(){
            return true;
        }
    },

    LAKE{
        public boolean blocksPath() {
            return true;
        }

        public boolean blocksVision(){
            return false;
        }
    },

    BOTTOMLESSPIT{

        public boolean blocksPath() {
            return false;
        }

        public boolean blocksVision(){
            return false;
        }
    };

    /**
     * Indicates whether this obstacle blocks movement
     * @return true if it blocks the path, false otherwise
     */
    public abstract boolean blocksPath();

    /**
     * Indicate whether this obstacle blocks vision
     * @return true the obstacle blocks vision, false otherwise
     */
    public abstract boolean blocksVision();

}

