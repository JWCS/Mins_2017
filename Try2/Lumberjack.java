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

    MapLocation target = enemyArchons[0];
    TreeInfo[] nTrees, myTrees, theirTrees;
    RobotInfo[] alliesInRange, enemies;
    boolean canAttack = true;
    float range = me.bodyRadius + GameConstants.LUMBERJACK_STRIKE_RADIUS;

    void run() throws GameActionException {

        while (true) {
            try {
                // Update for new round
                Update();
                // If dying, declare dead
                checkDeath(Constants.JACK_NUM_CH);
                // Update on nearby elements
                nTrees = rc.senseNearbyTrees(-1,Team.NEUTRAL);
                myTrees = rc.senseNearbyTrees( -1, us);
                theirTrees = rc.senseNearbyTrees(-1, them);
                enemies = rc.senseNearbyRobots(-1, them);
                alliesInRange = rc.senseNearbyRobots(range, us);

                // Priority list: chase weak bots, avoid tanks & jacks, chop neutrals w/ robots, chop enemy trees, collect bullets
                // And if near archon, chop neutral trees, very important
                priorities:
                {
                    // Near Archon
                    for(MapLocation arc : myArchons){
                        if( here.distanceTo(arc) < 15 ){
                            if(enemies.length > 0)
                                break;
                            int id;
                            if(nTrees.length > 0){
                                id = nTrees[0].getID();
                                if(rc.canInteractWithTree(id)){
                                    if(rc.canShake(id))
                                        rc.shake(id);
                                    if(rc.canChop(id))
                                        rc.chop(id);
                                }else
                                    goTo(nTrees[0].getLocation());
                            }else if(theirTrees.length > 0){
                                id = nTrees[0].getID();
                                if(rc.canInteractWithTree(id)){
                                    if(rc.canShake(id))
                                        rc.shake(id);
                                    if(rc.canChop(id))
                                        rc.chop(id);
                                }else
                                    goTo(nTrees[0].getLocation());

                            }else
                                goTo(target);
                            break priorities;
                        }
                    }

                    // Don't harm allies
                    canAttack = alliesInRange.length > 0;
                    // Chase scouts (if in range), gardeners, archons, & avoid tanks & jacks
                    for(RobotInfo rob : enemies){
                        if(rob.getType()==RobotType.SCOUT && rob.getLocation().distanceTo(here) < range + stride){
                            target = rob.getLocation();
                            goTo(target);
                            if(canAttack)
                                rc.strike();
                            break priorities;
                        }else if(rob.getType()==RobotType.GARDENER || rob.getType()==RobotType.ARCHON){
                            target = rob.getLocation();
                            goTo(target);
                            if(canAttack)
                                rc.strike();
                            break priorities;
                        }else if(rob.getType()==RobotType.TANK || rob.getType()==RobotType.LUMBERJACK){
                            Direction targetDir = here.directionTo(target);
                            Direction badGuyDir = here.directionTo(rob.getLocation());
                            if(Math.abs(targetDir.degreesBetween(badGuyDir.rotateRightDegrees(120))) <
                                    Math.abs(targetDir.degreesBetween(badGuyDir.rotateLeftDegrees(120))))
                                goTo(here.add(badGuyDir.rotateRightDegrees(120)));
                            else
                                goTo(here.add(badGuyDir.rotateLeftDegrees(120)));
                            if(rob.getLocation().distanceTo(here) < range && canAttack && rc.canStrike())
                                rc.strike();
                            else if(theirTrees.length > 0 && rc.canInteractWithTree(theirTrees[0].getID()) && rc.canChop(theirTrees[0].getID()))
                                rc.chop(theirTrees[0].getID());
                            else if(nTrees.length > 0 && rc.canInteractWithTree(nTrees[0].getID())){
                                if(rc.canShake(nTrees[0].getID()))
                                    rc.shake(nTrees[0].getID());
                                if(rc.canChop(nTrees[0].getID()))
                                    rc.chop(nTrees[0].getID());
                            }
                            break priorities;
                        }
                    }

                    // Chop neutrals with robots
                    for(TreeInfo tree : nTrees){
                        if(tree.getContainedRobot() != null){
                            target = tree.getLocation();
                            if(rc.canInteractWithTree(target)) {
                                if (rc.canShake(target))
                                    rc.shake(target);
                                if (rc.canChop(target))
                                    rc.chop(target);
                            }else
                                goTo(target);
                            if(rc.canInteractWithTree(target)) {
                                if (rc.canShake(target))
                                    rc.shake(target);
                                if (rc.canChop(target))
                                    rc.chop(target);
                            }
                            break priorities;
                        }
                    }

                    // Not near archon, no enemies about, no robots in trees: go chop enemy trees
                    if(theirTrees.length > 0){
                        target = theirTrees[0].getLocation();
                        if (rc.canChop(target))
                                rc.chop(target);
                        else
                            goTo(target);
                        if (rc.canChop(target))
                            rc.chop(target);
                        break priorities;
                    }

                    // Not much to do yet, in process no doubt; get bullets if poss, otherwise go to enemy
                    for( TreeInfo tree : nTrees ){
                        if(tree.getContainedBullets() > 0){
                            target = tree.getLocation();
                            if(rc.canInteractWithTree(target)) {
                                if (rc.canShake(target))
                                    rc.shake(target);
                                if (rc.canChop(target))
                                    rc.chop(target);
                            }else
                                goTo(target);
                            if(rc.canInteractWithTree(target)) {
                                if (rc.canShake(target))
                                    rc.shake(target);
                                if (rc.canChop(target))
                                    rc.chop(target);
                            }
                            break priorities;
                        }
                    }

                    // Enemy base is down
                    target = getEnemyLoc();
                    goTo(target);

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
