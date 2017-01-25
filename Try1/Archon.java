package Try1;
import battlecode.common.*;

/**
 * Package <Try1>, Mins_2017 Project
 * Created by JWCS on 1/10/17.
 * The code implemented by the Archon. Optimized or common methods from methods package.
 */
@SuppressWarnings("unused")

class Archon extends RobotCode{

    int archonNum               = -1;
    float bullets               = GameConstants.BULLETS_INITIAL_AMOUNT;
    int farmers                 = 0;
    int builders                = 0;
    int stableFarmers           = 0;
    int busyBuilders            = 0;
    boolean wantToBuildFarmer   = false, wantToBuildBuilder = false;
    int roundsTillHire          = 0;
    Direction myDir             = Direction.getNorth();
    Direction toBuild           = myDir.opposite();
    boolean isTreeToShake       = false;
    int treeToShakeID;

    // Tweakable game consts
    int bulletTaxBase   = 500;
    float baseTax       = 0.075f;
    float taxCurve      = 0.015f;


    void run() throws GameActionException {
        System.out.println("I'm archon no " +  archonNum);


        while (true) {
            try {
                // Get basic data
//                bullets = rc.getTeamBullets();
//                // Bullet Evade Code
//
//                // Decision Matrix // Execute self-actions, and write new myDir, toBuildDir, broadcast orders
//                // Allocate resources
//                vpConvert(); // Convert Excess bullets
//                if(roundNum < EARLY_END && roundsTillHire <= 0){
//                    if( farmers > 5 && builders < 2){
//                        chooseBuildBuilder();
//                    }else if( farmers - stableFarmers < 2 && farmers < 32){
//                        chooseBuildFarmer();
//                    }
//                }else if(roundNum > EARLY_END && roundsTillHire <= 0){
//                    if(farmers < 31 && builders - busyBuilders > 3){
//                        chooseBuildFarmer();
//                    }
//                }
//

                // Enforce Decisions
                // Gardeners
                buildGardeners();
                // Shake dem
                if(isTreeToShake && rc.canShake(treeToShakeID))
                    rc.shake(treeToShakeID);

                ++roundNum;
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Archon " + archonNum + " Exception");
                e.printStackTrace();
            }
        }
    }

    void war() throws GameActionException{  // Coordinates war

    }


    Archon() throws GameActionException{
        super();
        if(rc.getRoundNum() == 1){
            archonNum = rc.readBroadcast(0);
            rc.broadcast(0, archonNum + 1);
        }else{
            System.out.println("Archon init error, round num");
        }
    }

    void buildGardeners() throws GameActionException{
        if( roundsTillHire == 0 && (wantToBuildFarmer || wantToBuildBuilder)  )
            if(rc.canHireGardener(toBuild)){
                rc.hireGardener(toBuild);
                if(wantToBuildFarmer)
                    ++farmers;
                else
                    ++builders;
                roundsTillHire = 10;
                wantToBuildFarmer = false;
                wantToBuildBuilder = false;
            }else
                for( int i = 1; i < 4; ++i) {
                    Direction dir = toBuild.rotateLeftDegrees(i * 90);
                    if (rc.canHireGardener(dir)) {
                        rc.hireGardener(dir);
                        if(wantToBuildFarmer)
                            ++farmers;
                        else
                            ++builders;
                        roundsTillHire = 10;
                        wantToBuildBuilder = false;
                        wantToBuildFarmer = false;
                        break;
                    }
                }
        else if(roundsTillHire > 0)
            --roundsTillHire;
    }

    void vpConvert() throws GameActionException{
//        int bulletsToConvert = bullets < bulletTaxBase ? 0 : (int)(bullets * (baseTax + taxCurve*(bullets-bulletTaxBase)));
//        if(bulletsToConvert > 0){
//            float val = (float)(bulletsToConvert/10)*GameConstants.BULLET_EXCHANGE_RATE;
//            rc.donate(val);
//            bullets -= val * 10;
//        } // If they change exchange rate, this changes
    }

//    void chooseBuildBuilder() throws GameActionException{
//        wantToBuildFarmer = false;
//        wantToBuildBuilder = true;
//        int assign = rc.readBroadcast(G_ASSIGN_CH0);
//        rc.broadcast(G_ASSIGN_CH0, assign & BUILDER_FLAG);
//    }
//
//    void chooseBuildFarmer() throws GameActionException{
//        wantToBuildFarmer = true;
//        wantToBuildBuilder = false;
//        int assign = rc.readBroadcast(G_ASSIGN_CH0);
//        rc.broadcast(G_ASSIGN_CH0, assign & FARMER_FLAG);
//    }

}
