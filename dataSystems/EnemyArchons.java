package dataSystems;
import battlecode.common.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

/**
 * Package <dataSystems>, Mins_2017 Project
 * Created by JWCS on 1/13/17.
 */


public class EnemyArchons {

    public class Data{
        float x = 0;
        float y = 0;
        int dir = 0;
        int data = 0;
        BitSet dataSet, xSet, ySet, dirSet; // Intermediaries

        // int xBitNo = 13;
        // int yBitNo = 13;
        // int dirBitNo = 6;

        public Data(){}

        void setX( float in ){ x = in; }
        void setY( float in ){ y = in; }
        void setX( int in ){ x = in; }
        void setY( int in ){ y = in; }
        float getX(){ return x; }
        float getY(){ return y; }
        void setDir( int in ){ dir = in; }
        int getDir(){ return dir; }

        void setData( int in ){
            dataSet = BitSet.valueOf(ByteBuffer.allocate(4).putInt(in));
            xSet = dataSet.get(0, 13);      // 0 -> xBitNo
            ySet = dataSet.get(13, 26);     // xBitNo -> xBitNo + yBitNo
            dirSet = dataSet.get(26, 32);   // xBitNo + yBitNo -> sum of the 3
            // Figure out conv scheme
        }

        int getData(){
            // Then undo
            return -1;
        }

    }

    int archonNum = 0;

    public EnemyArchons(MapLocation[] initialArchonLocs){
        ArrayList<MapLocation> locs = new ArrayList<MapLocation>(Arrays.asList(initialArchonLocs));
        archonNum = locs.size();

    }



}
