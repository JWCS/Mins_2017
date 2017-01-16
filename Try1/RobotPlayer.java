package Try1;
import battlecode.common.*;

/**
 * Package <Try1>, Mins_2017 Project
 * Created by JWCS on 1/10/17.
 * This class is a boilerplate class providing the game-required 'run' command to kick-off the show.
 * The generic robot code is instead placed in RobotCode.java
 */

public strictfp class RobotPlayer {
    public static RobotController rc;
    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    @SuppressWarnings("unused")

    public static void run(RobotController rc) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        RobotPlayer.rc = rc;
        RobotCode code;
        // Implements a different class specific to each RobotType, but common elements are in the base RobotCode
        switch (rc.getType()) {
            case ARCHON:
                code = new Archon();
                break;
            case GARDENER:
                if((rc.readBroadcast(0) & 1) == 0) // Is a farmer
                    code = new Farmer();
                else
                    code = new Builder();
                break;
            case SOLDIER:
                code = new Soldier();
                break;
            case LUMBERJACK:
                code = new Lumberjack();
                break;
            case SCOUT:
                code = new Scout();
                break;
            case TANK:
                code = new Tank();
                break;
            default:
                code = new RobotCode();
                break;
        }
        code.run();
    }


}
