package Try1;
import battlecode.common.*;
import utils.ArcLoc;
import utils.Structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Package <Try1>, Mins_2017 Project
 * Created by JWCS on 1/16/17.
 */


public class Farmer extends RobotCode {

    int NUM;
    int[] COMMS;        // [ channel, poss in ch]
    Structure garden = new Structure(rc, origin);
    // States
    boolean isStable = false;
    boolean holeOpen = false;
    boolean atGarden = false;
    MapLocation mapPos;
    MapLocation gardenPos;
    MapLocation hole;
    MapLocation inFrontOfHole;
    Direction   holeDir;
    Direction   gardenDir;
    ArrayList<TreeInfo> gardenTrees = new ArrayList<TreeInfo>();
    RobotInfo[] nearbyEnemies;

    void run() throws GameActionException{
        init();
        while( !isStable ){
            goPlantGarden();
        }
        while(true){        // Stable operations: mainly dealing with enemies
            // Water lowest hp tree
            Collections.sort(gardenTrees, Comparator.comparing(TreeInfo::getHealth));
            rc.water(gardenTrees.get(0).getLocation());
            // Be alert & check for enemies
            if(holeOpen){ // This is for vigilence: TODO: implement "vigilence" and watching/spawning capabilities
//                if(rc.isLocationOccupiedByTree(inFrontOfHole)){
//                    if(rc.canPlantTree(holeDir)){
//                        rc.plantTree(holeDir);
//                        holeOpen = false;
//                    }
//                }else{  // If front is open
//
//                }
            }else{      // If hole is closed
                nearbyEnemies = rc.senseNearbyRobots(-1, enemy);
                if(nearbyEnemies.length > 0)
                    notifyOfEnemies(nearbyEnemies);
            }
            // Deal with death
            if(rc.getHealth() - 10 <= 0)
                notifyOfDeath();
        }
    }

    void notifyOfEnemies(RobotInfo[] nearbyEnemies){

    }

    void goPlantGarden() throws GameActionException{      // Pathing
        // Part one: get there
        mapPos = rc.getLocation();
        gardenDir = mapPos.directionTo(gardenPos);
        if(!atGarden && rc.canMove(gardenPos)){
            rc.move(gardenPos);
            atGarden = true;
        }else if(rc.canMove(gardenDir)){
            rc.move(gardenDir);
        }else if(rc.canMove(gardenDir.rotateLeftDegrees(90))){
            rc.move(gardenDir.rotateLeftDegrees(90));
        }else if(rc.canMove(gardenDir.rotateLeftDegrees(160))){
            rc.move(gardenDir.rotateLeftDegrees(160));
        }
        // Part two: build it
        if(atGarden){
            buildGarden();
        }
        // if all trees built, notify of stability, location, and Farm num
        // isStable = true;
    }

    void buildGarden(){

    }

    void endRound(){
        ++roundNum;
        Clock.yield();
    }

    void notifyOfDeath() throws GameActionException{
        // Send notification

        // Remove self from counter
        rc.broadcast( COMMS[0], rc.readBroadcast(COMMS[0]) ^ (1 << COMMS[1]) );
    }

    void init() throws GameActionException{
        loops:
        for(int i = 0; i < G_ASSIGN_NUM_CH; ++i){
            int ch = rc.readBroadcast(G_ASSIGN_CHS[i]);
            if(ch == 0xFFFFFFFF)
                continue;
            for(int j = 0; j < 32; ++j){
                if(((ch >> j) & 1) == 0){
                    rc.broadcast(i, ch | (1 << j));
                    NUM = i*32 + j;
                    COMMS = new int[] {i, j};
                    break loops;
                }
            }
        }
        garden.getGarden();
        gardenPos = garden.getMapLoc();
    }

    Farmer() throws GameActionException{
        super();
    }

}
