package utils;
import battlecode.common.MapLocation;
import javafx.scene.shape.Arc;

import java.util.Map;

/**
 * Package <utils>, Mins_2017 Project
 * Created by JWCS on 1/16/17.
 */


public class ArcLoc{

    MapLocation origin;
    public byte X;
    public byte Y;
    public MapLocation loc;

    public ArcLoc( byte x, byte y, MapLocation origin){
        this.origin = origin;
        X = x;
        Y = y;
        loc = new MapLocation(origin.x + X, origin.y + Y);
    }

    public ArcLoc( int x, int y, MapLocation origin){
        this.origin = origin;
        X = (byte)(x & 0xff);
        Y = (byte)(y & 0xff);
        loc = new MapLocation(origin.x + X, origin.y + Y);
    }

    public ArcLoc( MapLocation pos, MapLocation origin ){
        this.origin = origin;
        X = (byte)(pos.x + origin.x);
        Y = (byte)(pos.y + origin.y);
        loc = pos;
    }

    public MapLocation getTrueLoc(){
        return loc;
    }

}
