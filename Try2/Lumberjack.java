package Try2;

import battlecode.common.*;

import static utils.Methods.randomDirection;
import static utils.Methods.tryMove;

/**
 * Package <test0>, Mins_2017 Project
 * Created by JWCS on 1/10/17.
 * The code implemented by the Lumberjack. Optimized or common methods from methods package.
 */
@SuppressWarnings("unused")

public class Lumberjack extends RobotCode {

    Lumberjack() throws GameActionException{
        super();
    }

    void run() throws GameActionException {

        while (true) {
            try {
                // Update for new round
                Update();
                // If dying, declare dead
                checkDeath(Constants.JACK_NUM_CH);

                // See if there are any enemy robots within striking range (distance 1 from lumberjack's radius)
                RobotInfo[] robots = rc.senseNearbyRobots(RobotType.LUMBERJACK.bodyRadius+GameConstants.LUMBERJACK_STRIKE_RADIUS, enemy);

                if(robots.length > 0 && !rc.hasAttacked()) {
                    // Use strike() to hit all nearby robots!
                    rc.strike();
                } else {
                    // No close robots, so search for robots within sight radius
                    robots = rc.senseNearbyRobots(-1,enemy);

                    // If there is a robot, move towards it
                    if(robots.length > 0) {
                        MapLocation myLocation = rc.getLocation();
                        MapLocation enemyLocation = robots[0].getLocation();
                        Direction toEnemy = myLocation.directionTo(enemyLocation);

                        tryMove(toEnemy);
                    } else {
                        // Move Randomly
                        tryMove(randomDirection());
                    }
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Lumberjack Exception");
                e.printStackTrace();
            }
        }
    }

}
