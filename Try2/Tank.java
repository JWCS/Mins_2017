package Try2;

import battlecode.common.*;

import static battlecode.common.RobotType.*;

/**
 * Package <Try2>, Mins_2017 Project
 * Created by JWCS on 1/25/17.
 * The code implemented by the Tank. Optimized or common methods from methods package.
 */
@SuppressWarnings("unused")

public class Tank extends RobotCode {

    Tank() throws GameActionException{
        super();
    }

    MapLocation target = enemyArchons[0];
    RobotInfo[] enemies;

    void run() throws GameActionException{
        while (true){
            try {
                // Update for new round
                Update();
                // If dying, declare dead
                checkDeath(Constants.TANK_NUM_CH);
                // Update on nearby elements
                enemies = rc.senseNearbyRobots(-1, them);
                // Update enemy loc for team if found
                for(RobotInfo rob : enemies)
                    if(rob.getType()== ARCHON){
                        setEnemyLoc(rob.getLocation());
                        break;
                    }

                // Sort enemy nearby robots by type
                int eArchonsNo = 0, eGardenersNo = 0, eJacksNo = 0, eSoldiersNo = 0, eScoutsNo = 0, eTankNo = 0;
                RobotInfo[] eArchons, eGardeners, eJacks, eSoldiers, eScouts, eTanks;
                for( RobotInfo rob : enemies )
                    switch (rob.getType()){
                        case ARCHON:
                            eArchonsNo++;
                            break;
                        case GARDENER:
                            eGardenersNo++;
                            break;
                        case LUMBERJACK:
                            eJacksNo++;
                            break;
                        case SOLDIER:
                            eSoldiersNo++;
                            break;
                        case SCOUT:
                            eScoutsNo++;
                            break;
                        case TANK:
                            eTankNo++;
                            break;
                    }
                eArchons = new RobotInfo[eArchonsNo];
                eGardeners = new RobotInfo[eGardenersNo];
                eJacks = new RobotInfo[eJacksNo];
                eSoldiers = new RobotInfo[eSoldiersNo];
                eScouts = new RobotInfo[eScoutsNo];
                eTanks = new RobotInfo[eTankNo];
                for( int i = 0; i < eArchonsNo; i++)
                    for(RobotInfo rob : enemies)
                        if(rob.getType() == ARCHON)
                            eArchons[i] = rob;
                for( int i = 0; i < eGardenersNo; i++)
                    for(RobotInfo rob : enemies)
                        if(rob.getType() == GARDENER)
                            eGardeners[i] = rob;
                for( int i = 0; i < eJacksNo; i++)
                    for(RobotInfo rob : enemies)
                        if(rob.getType() == LUMBERJACK)
                            eJacks[i] = rob;
                for( int i = 0; i < eSoldiersNo; i++)
                    for(RobotInfo rob : enemies)
                        if(rob.getType() == SOLDIER)
                            eSoldiers[i] = rob;
                for( int i = 0; i < eScoutsNo; i++)
                    for(RobotInfo rob : enemies)
                        if(rob.getType() == SCOUT)
                            eScouts[i] = rob;
                for( int i = 0; i < eTankNo; i++)
                    for(RobotInfo rob : enemies)
                        if(rob.getType() == TANK)
                            eTanks[i] = rob;

                // Kill archons, gardeners (even through trees), kill lumberjacks if close, soldiers if poss, scouts if close
                priorities:
                {
                    if(eArchonsNo > 0){
                        target = eArchons[0].getLocation();
                        goTo(target);
                        if(rc.canFirePentadShot())
                            rc.firePentadShot(here.directionTo(target));
                        break priorities;
                    }else if(eGardenersNo > 0){
                        target = eGardeners[0].getLocation();
                        goTo(target, false);
                        if(rc.canFirePentadShot())
                            rc.firePentadShot(here.directionTo(target));
                        break priorities;
                    }else if (eJacksNo > 0 && eJacks[0].getLocation().distanceTo(here) < 5 ){
                        target = here.add(eJacks[0].getLocation().directionTo(here));   // They can outrun, reduces damage
                        goTo(target);
                        if(rc.canFirePentadShot())
                            rc.firePentadShot(here.directionTo(target).opposite()); // Target is negative dir of jack
                        break priorities;
                    }else if(eScoutsNo > 0 && eScouts[0].getLocation().distanceTo(here) < 4){
                        target = eScouts[0].getLocation();
                        goTo(target);
                        if(rc.canFirePentadShot())
                            rc.firePentadShot(here.directionTo(target));
                        break priorities;
                    }else if(eSoldiersNo > 0) {
                        target = eSoldiers[0].getLocation();
                        goTo(target);
                        if(rc.canFirePentadShot())
                            rc.firePentadShot(here.directionTo(target));
                        break priorities;
                    }else if(eTankNo > 0){      // Already pretty much ignore them- if winning, conflict good, if not, bad; cant prevent tho
                        Direction targetDir = here.directionTo(target);
                        Direction badGuyDir = here.directionTo(eTanks[0].getLocation());
                        if(Math.abs(targetDir.degreesBetween(badGuyDir.rotateRightDegrees(120))) <
                                Math.abs(targetDir.degreesBetween(badGuyDir.rotateLeftDegrees(120))))
                            goTo(here.add(badGuyDir.rotateRightDegrees(120)));
                        else
                            goTo(here.add(badGuyDir.rotateLeftDegrees(120)));
                        // Fire at tank anyway, while running
                        if(rc.canFirePentadShot())
                            rc.firePentadShot(here.directionTo(eTanks[0].getLocation()));
                        break priorities;
                    }else{      // No enemies, go to enemy
                        // Enemy base is down
                        target = getEnemyLoc();
                        goTo(target);
                        // Just in case
                        enemies = rc.senseNearbyRobots(-1, them);
                        if(enemies.length > 0 && rc.canFirePentadShot())
                            rc.firePentadShot(here.directionTo(enemies[0].getLocation()));
                    }
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            }catch (Exception e){
                System.out.println("Tank Exception");
                e.printStackTrace();
            }
        }
    }

}
