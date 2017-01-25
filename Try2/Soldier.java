package Try2;

import battlecode.common.*;

import static battlecode.common.RobotType.*;
import static battlecode.common.RobotType.SCOUT;
import static battlecode.common.RobotType.TANK;
import static utils.Methods.randomDirection;
import static utils.Methods.tryMove;

/**
 * Package <Try2>, Mins_2017 Project
 * Created by JWCS on 1/25/17.
 * The code implemented by the Soldier. Optimized or common methods from methods package.
 */
@SuppressWarnings("unused")

public class Soldier extends RobotCode {

    Soldier() throws GameActionException{
        super();
    }

    MapLocation target = enemyArchons[0];
    RobotInfo[] enemies;
    TreeInfo[] nTrees;

    void run() throws GameActionException {
        while (true) {
            try {
                // Update for new round
                Update();
                // If dying, declare dead
                checkDeath(Constants.SOLDIER_NUM_CH);
                // Update on nearby elements
                enemies = rc.senseNearbyRobots(-1, them);
                nTrees = rc.senseNearbyTrees(-1,Team.NEUTRAL);
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

                // Kill gardeners, tanks, jacks, soldiers, scouts, arcons; collect bullets if poss
                if(eGardenersNo > 0){
                    target = eGardeners[0].getLocation();
                    goTo(target);
                    if(rc.canFirePentadShot())
                        rc.firePentadShot(here.directionTo(target));
                }else if(eTankNo > 0){

                    if(rc.canFirePentadShot())
                        rc.firePentadShot(here.directionTo(eTanks[0].getLocation()));
                }else if (eJacksNo > 0){
                    target = eJacks[0].getLocation();// Maintain safe dist from them
                    if( here.distanceTo(target) > GameConstants.LUMBERJACK_STRIKE_RADIUS + LUMBERJACK.bodyRadius + LUMBERJACK.strideRadius + 1)
                        goTo(target);
                    else if(here.distanceTo(target) < GameConstants.LUMBERJACK_STRIKE_RADIUS + LUMBERJACK.bodyRadius + LUMBERJACK.strideRadius)
                        goTo(here.subtract(here.directionTo(target)));
                    if(rc.canFirePentadShot())
                        rc.firePentadShot(here.directionTo(target)); // Target is negative dir of jack
                }else if(eSoldiersNo > 0) {
                    target = eSoldiers[0].getLocation();
                    goTo(target.add(here.directionTo(target).rotateLeftDegrees(80)));
                    if(rc.canFirePentadShot())
                        rc.firePentadShot(here.directionTo(target));
                }else if(eScoutsNo > 0 && eScouts[0].getLocation().distanceTo(here) < 6){
                    target = eScouts[0].getLocation();
                    goTo(target);
                    if(rc.canFirePentadShot())
                        rc.firePentadShot(here.directionTo(target));
                }else if(eArchonsNo > 0){
                    target = eArchons[0].getLocation();
                    goTo(target);
                    if(rc.canFirePentadShot())
                        rc.firePentadShot(here.directionTo(target));
                }else{      // No enemies, go to enemy
                    priorities:
                    {
                        for (TreeInfo tree : nTrees)
                            if(tree.getContainedBullets() > 0){
                                if(rc.canShake(tree.getID()))
                                    rc.shake(tree.getID());
                                else{
                                    goTo(tree.getLocation());
                                    if(rc.canShake(tree.getID()))
                                        rc.shake(tree.getID());
                                }
                                enemies = rc.senseNearbyRobots(-1, them);
                                if (enemies.length > 0 && rc.canFirePentadShot())
                                    rc.firePentadShot(here.directionTo(enemies[0].getLocation()));
                                break priorities;
                            }
                        // Else: Enemy base is down
                        target = getEnemyLoc();
                        goTo(target);
                        // Just in case
                        enemies = rc.senseNearbyRobots(-1, them);
                        if (enemies.length > 0 && rc.canFirePentadShot())
                            rc.firePentadShot(here.directionTo(enemies[0].getLocation()));
                    }
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Soldier Exception");
                e.printStackTrace();
            }
        }
    }

}
