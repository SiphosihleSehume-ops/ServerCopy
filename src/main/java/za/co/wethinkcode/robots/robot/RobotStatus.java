package za.co.wethinkcode.robots.robot;

import za.co.wethinkcode.robots.robot.RobotType;

public class RobotStatus {
    private String status;
    private int shields;
    private int shots;
    enum Status{
        RELOAD, REPAIR, NORMAL, DEAD
    }


    public RobotStatus(RobotType type) {
        this.status = Status.NORMAL.name();
        this.shields = type.getShields();
        this.shots = type.getShots();


    }

    public String getStatus() {
        return status;
    }
    public int getShields() {
        return shields;
    }
    public int getShots() {
        return shots;
    }

    public void setStatus(String currentStatus) {
        this.status = currentStatus;
    }
    public void setShields(int shields) {
        this.shields = shields;
    }
    public void setShots(int shots) {
        this.shots = shots;
    }

    public boolean isAlive() {
        return !this.status.equals(Status.DEAD.name());
    }
    public boolean hasShots() { return this.shots > 0;}
    public boolean hasShield() { return this.shields > 0;}

    @Override
    public String toString() {
        return String.format("Status: %s | Shield: %d | Shots: %d", status, shields, shots );
    }

}
