package Try1;
import battlecode.common.*;

/**
 * Package <Try1>, Mins_2017 Project
 * Created by JWCS on 1/10/17.
 * This class is a common instance of a type of robot's code. It has global databanks, settings, masks, etc. that
 * should be known on init. When a robot is made, the run command is called & overridden by the appropriate subclass
 */
@SuppressWarnings("unused")

public class RobotCode extends RobotConstants{
    public static RobotController rc = RobotPlayer.rc;

    int roundNum;
    MapLocation origin;
    Team team;
    int bcID;

    // Abstract run method

    void run() throws GameActionException {
        while(true){
            System.out.println("I'm a do nothing-er. There should be an error.");
            try{
                throw new GameActionException( GameActionExceptionType.CANT_DO_THAT, "Since there's nothing. No run. ");
            }catch (Exception e){
                // Do nothing
            }
            Clock.yield();
        }
    }

    // Subclass assignment

    public RobotCode() throws GameActionException { // Default Constructor required for subclasses
        // Any initializations
        roundNum = rc.getRoundNum();
        team = rc.getTeam();
        bcID = rc.getID();
        MapLocation rawOrigin = rc.getInitialArchonLocations(team)[0];
        origin = new MapLocation((float)Math.floor(rawOrigin.x), (float)Math.floor(rawOrigin.y));
    }

}
