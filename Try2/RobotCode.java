package Try2;

import battlecode.common.*;

/**
 * Package <Try2>, Mins_2017 Project
 * Created by JWCS on 1/23/17.
 * This class is a common instance of a type of robot's code. It has global databanks, settings, masks, etc. that
 * should be known on init. When a robot is made, the run command is called & overridden by the appropriate subclass
 */
@SuppressWarnings("unused")

public class RobotCode {
    public static RobotController rc = RobotPlayer.rc;

    // Data fields & common elements

    Team us, them;
    RobotType me;
    float stride;
    float radius;
    boolean dead = false;
    MapLocation here;
    MapLocation[] myArchons;
    MapLocation[] enemyArchons;

    // Abstract run method

    void run() throws GameActionException {
        while(true){
            System.out.println("I'm a do nothing-er.");
            try{
                //throw new GameActionException( GameActionExceptionType.CANT_DO_THAT, "Since there's nothing. No run. ");
            }catch (Exception e){
                // Do nothing
            }
            Clock.yield();
        }
    }

    // Subclass assignment

    public RobotCode() throws GameActionException { // Default Constructor required for subclasses
        // Any initializations
        here = rc.getLocation();
        us = rc.getTeam();
        them = us.opponent();
        me = rc.getType();
        stride = me.strideRadius;
        radius = me.bodyRadius;
        myArchons = rc.getInitialArchonLocations(us);
        enemyArchons = rc.getInitialArchonLocations(them);
    }

    void Update(){
        here = rc.getLocation();

    }

    void checkDeath(int ch) throws GameActionException{
        // If dying, declare dead
        if(rc.getHealth() <= 10 && !dead){
            dead = true;
            rc.broadcast(ch, rc.readBroadcast(ch) - 1);
        }
    }

    void goTo(MapLocation goal) throws GameActionException {     // Bug algorithm 2
        Direction dir = here.directionTo(goal);
//        // Check future dirs: get things and sort by how off they are from path
//        RobotInfo[] robots = rc.senseNearbyRobots();
//        TreeInfo[] trees = rc.senseNearbyTrees(); Oops, this is actually for tanget bug
        BulletInfo[] bullets = rc.senseNearbyBullets();
        // Cue evasion?
        while(rc.isCircleOccupiedExceptByThisRobot(here.add(dir, stride), radius))
            dir.rotateLeftDegrees(10f);
        if(rc.canMove(dir)){
            rc.move(dir);
            here = rc.getLocation();
        }
    }

}
