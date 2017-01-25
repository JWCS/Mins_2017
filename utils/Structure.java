package utils;

import Try1.RobotConstants;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.HashMap;
import java.util.Map;

/**
 * Package <utils>, Mins_2017 Project
 * Created by JWCS on 1/16/17.
 */


public class Structure {

    static RobotController rc;
    int rawStructure;
    int structure;
    ArcLoc loc;
    MapLocation mapLoc;
    MapLocation origin;

    // Garden Building Specific
    Direction holeDir;
    MapLocation hole;

    // Garden types
    public enum StructureType{

        SQUARE_N(0,Direction.getNorth()),
        SQUARE_W(1,Direction.getWest()),
        SQUARE_S(2,Direction.getSouth()),
        SQUARE_E(3,Direction.getEast()),
        HEX_N(   4,Direction.getNorth());

        public final int        id;
        public final Direction  dir;
        private static final Map<Integer,StructureType> _map = new HashMap<Integer, StructureType>();
        static{
            for(StructureType st : StructureType.values())
                _map.put(st.id, st);
        }

        public static StructureType stEnum(int id){
            return _map.get(id);
        }

        private StructureType(int id, Direction dir){
            this.id = id;
            this.dir = dir;
        }

    }

    //public getNextFrontPiece(){}

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
        mapLoc = loc.getTrueLoc();
    }

    public int packRawStructure(int structure_type, byte x, byte y){
        return (structure_type << 16 ) | (x << 8) | y;
    }

    public ArcLoc getLoc(){ return loc; }
    public MapLocation getMapLoc(){ return mapLoc; }

    //public float[][] corners = { {-2, 2, 0}, {-}, {}, {} };

}
