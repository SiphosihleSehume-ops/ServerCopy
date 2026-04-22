package za.co.wethinkcode.robots.protocols;

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

    public abstract boolean blocksPath();
    public abstract boolean blocksVision();

}
