package Try2;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;

import static utils.Methods.randomDirection;
import static utils.Methods.tryMove;

/**
 * Package <Try2>, Mins_2017 Project
 * Created by JWCS on 1/25/17.
 * The code implemented by the Archon. Mostly just sits there regulating stuff. Might try moving it
 */
@SuppressWarnings("unused")

class Archon extends RobotCode {

    Archon() throws GameActionException{
        super();
        int archonsHere = rc.readBroadcast(Constants.ARCHONS_ALIVE_CH);
        if(archonsHere==0) {
            enemyArchonNo = enemyArchons.length;
            for (int i = 0; i < enemyArchonNo; ++i) {
                rc.broadcast(Constants.ENEMY_LOC_X_CH + 3 * i, (int) enemyArchons[i].x);
                rc.broadcast(Constants.ENEMY_LOC_Y_CH + 3 * i, (int) enemyArchons[i].y);
            }
        }
        rc.broadcast(Constants.ARCHONS_ALIVE_CH, ++archonsHere);
    }

    int enemyArchonNo;

    // Archon build ratios
    float treesToIncrease       = 4;
    float roundsToIncrease      = 100;
    float gardenerRatio         = 2;
    float scoutRatio            = 1.3f;
    float lumberjackRatio       = 0.8f;
    float soldierRatio          = 0.5f;
    float tankRatio             = 0.3f;
    float gardenerBase          = 2;
    float scoutBase             = 4;
    float lumberjackBase        = 3;
    float soldierBase           = 1;
    float tankBase              = 0;
    int scoutCap                = 4*enemyArchonNo;
    //In broadcast channel order: Gardener, Jack, scout, soldier, tank
    float[] neededUnits         = {gardenerBase, lumberjackBase, scoutBase, soldierBase, tankBase};
    int[]   haveUnits           = new int[6];
    int cost                        = 0;
    float costTransfer              = 0.25f;
    int vpToBuy;

    void run() throws GameActionException {
        while (true) {
            try {
                Update();
                checkDeath(Constants.ARCHONS_ALIVE_CH);
                // Update ratios
                if(rc.getRoundNum() % roundsToIncrease == 0 || rc.getTreeCount() % treesToIncrease == 0){
                    neededUnits[0] += gardenerRatio;
                    neededUnits[1] += lumberjackRatio;
                    if(neededUnits[2] + scoutRatio < scoutCap)
                        neededUnits[2] += scoutRatio;
                    neededUnits[3] += soldierRatio;
                    neededUnits[4] += tankRatio;
                }
                for(int i = 0; i < 5*2; i += 2)
                    rc.broadcast(i + Constants.GARDENER_NEED_CH, (int)Math.ceil(neededUnits[i]));
                // Update units in play
                for(int i = 0; i < 6; i+=2)
                    haveUnits[i] = rc.readBroadcast(Constants.GARDENER_STABLE_CH + 2*i);
                // Calc expected cost rn
                cost = 50*(haveUnits[1]-haveUnits[0]) + ((haveUnits[1]-haveUnits[0] < gardenerRatio) ? 100 : 0); // Gardeners
                cost += 100*(neededUnits[1] - haveUnits[2]);
                cost += 80*(neededUnits[2] - haveUnits[3]);
                cost += 100*(neededUnits[3] - haveUnits[4]);
                cost += 300*(neededUnits[4] - haveUnits[5]);
                vpToBuy = (int)( cost * costTransfer / rc.getVictoryPointCost);

                // Generate a random direction
                Direction dir = randomDirection();

                // Randomly attempt to build a gardener in this direction
                if (rc.canHireGardener(dir) && Math.random() < .01) {
                    rc.hireGardener(dir);
                }

                // Move randomly
                tryMove(randomDirection());

                // Broadcast archon's location for other robots on the team to know
                MapLocation myLocation = rc.getLocation();
                rc.broadcast(0,(int)myLocation.x);
                rc.broadcast(1,(int)myLocation.y);

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Archon Exception");
                e.printStackTrace();
            }
        }
    }

}
