//package za.co.wethinkcode.robots.robot;
//
//public class RobotType {
//}
package za.co.wethinkcode.robots.robot;

public enum RobotType {
    // we do not have to hard code the types of bots, but i think it makes our lives easier...
    SHOOTER (5, 3),
    SNIPER  (3, 5),
    TANK    (10, 2),
    SCOUT   (2, 2);

    private final int shields;
    private final int shots;


    // Later feature... potentially allow varied visibility... -> is it possible to implement speed?? hmmm
    RobotType(int shields, int shots) {
        this.shields    = shields;
        this.shots      = shots;
    }

    public int getShields()    { return shields; }
    public int getShots()      { return shots; }


    public static RobotType fromString(String input) {
        for (RobotType type : RobotType.values()) {
            if (type.name().equalsIgnoreCase(input)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                "Unknown robot type: '" + input + "'. " +
                        "Valid types are: SHOOTER, SNIPER, TANK, SCOUT"
        );
    }
}
