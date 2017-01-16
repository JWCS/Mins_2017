package Try1;
import battlecode.common.*;
import utils.Structure;

/**
 * Package <Try1>, Mins_2017 Project
 * Created by JWCS on 1/16/17.
 */


public class Farmer extends RobotCode {

    int NUM;
    int COMMS[];
    Structure garden = new Structure(rc, origin);
    // States
    boolean isStable = false;
    boolean holeOpen = true;

    void run() throws GameActionException{
        init();
        while( !isStable ){
            goPlantGarden();
        }
        while(true){
            if(holeOpen){
                //if() tree infront of hole
            }else{

            }
        }
    }

    void goPlantGarden(){      // Pathing

    }

    void endRound(){
        ++roundNum;
        Clock.yield();
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
    }

    Farmer() throws GameActionException{
        super();
    }

}
