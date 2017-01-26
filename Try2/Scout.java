package Try2;

import battlecode.common.*;

/**
 * Package <Try2>, Mins_2017 Project
 * Created by JWCS on 1/10/17.
 * The code implemented by the Scout. It's purpose is to bug the enemy archon.
 */
@SuppressWarnings("unused")

public class Scout extends RobotCode {

    Scout() throws GameActionException{
        super();
        enemyArchonNum = enemyArchons.length;
        eArchonsIDs = new int[enemyArchonNum][3];
        for(int i = 0; i < enemyArchonNum; i++ ) {
            eArchonsIDs[i][0] = 0;
            eArchonsIDs[i][1] = (int) enemyArchons[i].x;
            eArchonsIDs[i][2] = (int) enemyArchons[i].y;
        }
        enemyArchonsAlive = new boolean[enemyArchonNum];
        for(int i = 0; i < enemyArchonNum; ++i)
            enemyArchonsAlive[i] = true;
        // Choose an archon and bug it; this could really be cleaned up
        targetArchonInd = rc.getID() % enemyArchonNum;
        target = new MapLocation((float)eArchonsIDs[targetArchonInd][1],(float)eArchonsIDs[targetArchonInd][2]);
    }

    RobotInfo[] enemies;
    int[][] eArchonsIDs;
    boolean[] enemyArchonsAlive;
    int targetArchonInd = -1;
    int enemyArchonNum;
    MapLocation target;
    boolean atTarget = false;

    void run() throws GameActionException{
        while(true){
            try {
                // Update for new round
                Update();
                // If dying, declare dead
                checkDeath(Constants.SCOUT_NUM_CH);
                // Update on nearby elements
                enemies = rc.senseNearbyRobots(-1, them);
                // Update enemy loc for team if found
                for(RobotInfo rob : enemies)
                    if(rob.getType()== RobotType.ARCHON){
                        int id = rob.getID();
                        int x = (int)rob.getLocation().x;
                        int y = (int)rob.getLocation().y;
                        target = rob.getLocation();
                        atTarget = true;
                        for(int i = 0; i <= enemyArchonNum; ++i)
                            if(i!=eArchonsIDs.length && id==eArchonsIDs[i][0]){
                                eArchonsIDs[i][1] = x;
                                eArchonsIDs[i][2] = y;
                                rc.broadcast(Constants.ENEMY_LOC_X_CH + 3*i, x);
                                rc.broadcast(Constants.ENEMY_LOC_Y_CH + 3*i, y);
                                break;
                            }else if(i==enemyArchonNum)
                                for (int j = enemyArchonNum - 1; j >= 0; --j)
                                    if(eArchonsIDs[i][0] == 0){
                                        eArchonsIDs[i][0] = id;
                                        eArchonsIDs[i][1] = x;
                                        eArchonsIDs[i][2] = y;
                                        rc.broadcast(Constants.ENEMY_ID_CH + 3*j, id);
                                        rc.broadcast(Constants.ENEMY_LOC_X_CH + 3*j, x);
                                        rc.broadcast(Constants.ENEMY_LOC_Y_CH + 3*j, y);
                                        break;
                                    }
                        break;
                    }

                // If can't find target, change
                if(!atTarget && here.distanceTo(target) < 5){
                    targetArchonInd = targetArchonInd+1 % enemyArchonNum;
                    target = new MapLocation((float)eArchonsIDs[targetArchonInd][1],(float)eArchonsIDs[targetArchonInd][2]);
                }

                // Actual action code
                goTo(target);
                if(rc.canFireTriadShot())
                    rc.fireSingleShot(here.directionTo(target));

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            }catch (Exception e){
                System.out.println("Scout Exception");
                e.printStackTrace();
            }
        }
    }

}
