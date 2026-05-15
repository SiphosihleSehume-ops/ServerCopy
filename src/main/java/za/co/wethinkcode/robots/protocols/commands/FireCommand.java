package za.co.wethinkcode.robots.protocols.commands;

import za.co.wethinkcode.robots.protocols.Response;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.server.World;


import java.util.Map;

/**
 * FireCommand — fires a shot in the direction the shooter is currently facing.
 *
 * Flow:
 *  1. Look up the shooter in the world by name (execute only receives World).
 *  2. Guard: robot exists, is alive, is not busy, has ammo.
 *  3. Consume one shot.
 *  4. Ask World to resolve the shot (line-of-sight scan).
 *  5. If a victim is found → deal damage, build "Hit" response.
 *     Otherwise → build "Miss" response.
 *  6. Return response with shooter's updated state.
 */
public class FireCommand extends Command {

    public FireCommand(String robotName) {
        super(robotName);
    }

    @Override
    public Response execute(World world) {

        Robot shooter = world.findRobotByName(robotName());
        if (shooter == null) {
            return Response.error("Robot not launched");
        }

        if (!shooter.isAlive()) {
            return Response.error(robotName() + " is dead");
        }
        if (shooter.isBusy()) {
            return Response.error(robotName() + " is busy (" + shooter.status() + ")");
        }
        if (!shooter.canFire()) {
            // Out of ammo response
            Map<String, Object> data = Map.of("message", "Empty Ca");
            return Response.ok(data, shooter.state());
        }

        shooter.reduceAmmo();

        // Resolve shot in the world
        Robot victim = world.resolveShot(shooter);

        Map<String, Object> data;
        if (victim != null) {
            victim.takeDamage();
            int distance = withinDistance(shooter, victim);
            data = Map.of(
                    "message",  "Hit",
                    "distance", distance,
                    "robot",    victim.name()
            );
        } else {
            data = Map.of("message", "Miss");
        }

        //
        return Response.ok(data, shooter.state());
    }

    //Helper method

    private int withinDistance(Robot a, Robot b) {
        return Math.abs(a.currentPosition().getX() - b.currentPosition().getX())
                + Math.abs(a.currentPosition().getY() - b.currentPosition().getY());
    }
}












