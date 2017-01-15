package Try1;
import battlecode.common.*;
import utils.Comms;

/**
 * Package <Try1>, Mins_2017 Project
 * Created by JWCS on 1/10/17.
 * The code implemented by the Gardener. Optimized or common methods from methods package.
 */


public class Gardener extends RobotCode{

    Gardener() throws GameActionException{
        super();
    }

    int ch      = -1;
    int pos     = -1;
    int mask    = -1;
    int task    = 0b0;

    int readMyBroadcast(){
        int ret = -1;
        try {
            ret = ((mask & rc.readBroadcast(ch)) >> 4*pos) & 0x0f;
        } catch (GameActionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    void run() throws GameActionException {
        // Init gardener
        initGardener();
        while (true) {
            try {   // Try/catch blocks stop unhandled exceptions, which cause your robot to explode



//                // Listen for home archon's location
//                int xPos = rc.readBroadcast(0);
//                int yPos = rc.readBroadcast(1);
//                MapLocation archonLoc = new MapLocation(xPos,yPos);
//
//                // Generate a random direction
//                Direction dir = randomDirection();
//
//                // Randomly attempt to build a soldier or lumberjack in this direction
//                if (rc.canBuildRobot(RobotType.SOLDIER, dir) && Math.random() < .01) {
//                    rc.buildRobot(RobotType.SOLDIER, dir);
//                } else if (rc.canBuildRobot(RobotType.LUMBERJACK, dir) && Math.random() < .01 && rc.isBuildReady()) {
//                    rc.buildRobot(RobotType.LUMBERJACK, dir);
//                }
//
//                // Move randomly
//                tryMove(randomDirection());

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Gardener Exception, task: " + task);
                e.printStackTrace();
            }
        }
    }

    void initGardener() throws GameActionException{
        int broadcastCH0 = rc.readBroadcast(G_ASSIGN_CH0);
        int broadcastCH1 = rc.readBroadcast(G_ASSIGN_CH1);
        if ((broadcastCH0 &1) == (BUILDER_FLAG &1) && broadcastCH0 != 0xffffffff){  // If signal & not full/max builders, become builder
            for( int i = 31; i>=0; --i)
                if(((broadcastCH0 >> i) & 1) == 0){
                    pos = i;
                    rc.broadcast(G_ASSIGN_CH0, broadcastCH0 | (1<<i) );
                    break;
                }
            ch = pos / 8;
            mask = Comms.getMask4(pos % 8);
            task = readMyBroadcast();
        }else{ // Become a farmer
            ch = G_F_TASK_CH;
            task = rc.readBroadcast(ch);
            mask = 0b1111;
            for( int i = 31; i >= 0; --i)
                if(((broadcastCH1 >> i) & 1) == 0){
                    pos = i;
                    rc.broadcast(G_ASSIGN_CH1, broadcastCH1 | (1<<i) );
                    break;
                }
            if(pos == -1)   // Overflow of farmers
                for( int i = 31; i >=0; --i)
                    if(((task >> i) & 1) == 0){
                        pos = i + 31;
                        rc.broadcast(ch, task | (1<<i) );
                    }
        }
    }

    void buildGarden(){

    }

}
