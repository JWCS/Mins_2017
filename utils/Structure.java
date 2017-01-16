package utils;

import Try1.RobotConstants;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

/**
 * Package <utils>, Mins_2017 Project
 * Created by JWCS on 1/16/17.
 */


public class Structure {

    static RobotController rc;
    int rawStructure;
    int structure;
    ArcLoc loc;
    MapLocation origin;

    // Garden types
    public static final byte SQUARE  = 0;
    public static final byte HEX     = 1;

    public Structure(RobotController rc, MapLocation origin) throws GameActionException{
        this.rc = rc;
        this.origin = origin;
    }

    public void getGarden() throws GameActionException{
        rawStructure = rc.readBroadcast(RobotConstants.F_GARDEN_LIST_CHS[0]);
        unpackRawStructure();
    }

    public void getNewGarden() throws GameActionException{
        for(int ch : RobotConstants.F_GARDEN_LIST_CHS){
            int newGarden = rc.readBroadcast(ch);
            if( newGarden != rawStructure){
                rawStructure = newGarden;
                break;
            }
        }
        unpackRawStructure();
    }

    void unpackRawStructure(){
        structure = rawStructure & (0xff << 16);
        loc = new ArcLoc(rawStructure & (0xff << 8), rawStructure & 0xff, origin );
    }

    public int packRawStructure(byte structure_type, byte x, byte y){
        return (structure_type << 16 ) | (x << 8) | y;
    }

    public ArcLoc getLoc(){ return loc; }

}
