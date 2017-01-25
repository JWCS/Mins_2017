package Try2;

import battlecode.common.*;
import Try2.Constants.*;
import static utils.Methods.randomDirection;
import static utils.Methods.tryMove;

/**
 * Package <Try2>, Mins_2017 Project
 * Created by JWCS on 23/1/17.
 * The code implemented by the Gardener. Optimized or common methods from methods package.
 */


public class Gardener extends RobotCode {

    Gardener() throws GameActionException{
        super();
    }

    boolean stable = false;
    boolean gotGardeCenter = false;
    MapLocation gardenCenter;
    int treesInGarden = 0;
    float distToGoal = 100;
    int roundsToBuild = 10;
    TreeInfo[] garden = new TreeInfo[8];

    void run() throws GameActionException {

        while (true) {
            try {
                // Update for new round
                Update();
                // If dying, declare dead
                checkDeath(Constants.GARDENER_NUM_CH);
                if(roundsToBuild > 0)
                    --roundsToBuild;

                // Build unit if unit needed
                if(rc.isBuildReady() && treesInGarden < 8)
                    units:
                    for(int i = Constants.JACK_NEED_CH; i <= Constants.TANK_NEED_CH; i += 2){
                        int num = rc.readBroadcast(i + 1) + 1;
                        if(rc.readBroadcast(i) - num >= 0)
                            switch (i){
                                case Constants.JACK_NEED_CH:
                                    build(RobotType.LUMBERJACK);
                                    rc.broadcast(i+1, num);
                                    break units;
                                case Constants.SCOUT_NEED_CH:
                                    build(RobotType.SCOUT);
                                    rc.broadcast(i+1, num);
                                    break units;
                                case Constants.SOLDIER_NEED_CH:
                                    build(RobotType.SOLDIER);
                                    rc.broadcast(i+1, num);
                                    break units;
                                case Constants.TANK_NEED_CH:
                                    build(RobotType.TANK);
                                    rc.broadcast(i+1, num);
                                    break units;
                            }
                    }

                // Find a place to plant a garden
                if(!gotGardeCenter){
                    TreeInfo[] treesAround = rc.senseNearbyTrees(4.25f);
                    //RobotInfo[] robotsAround = rc.senseNearbyRobots();
//                      for( RobotInfo rob : robotsAround ){
//                        if
//                    }
                    int num_trees = treesAround.length;
                    if(num_trees == 0){
                        gardenCenter = rc.getLocation();
                        gotGardeCenter = true;
                    }else{
                        float dx = 0, dy = 0;
                        for(TreeInfo tree : treesAround){
                            dx += 1 / (here.x - tree.getLocation().x);
                            dy += 1 / (here.y - tree.getLocation().y);
                        }
                        for(MapLocation arc : myArchons){
                            dx += 1 / (here.x - arc.x);
                            dy += 1 / (here.y - arc.y);
                        }
                        for(MapLocation arc : enemyArchons){
                            dx += 1 / (arc.x - here.x);
                            dy += 1 / (arc.y - here.y);
                        }
                        float len = (float)Math.sqrt(dx*dx + dy*dy);
                        dx *= 2/len;
                        dy *= 2/len;
                        goTo(new MapLocation(here.x + dx, here.y + dy));
                    }
                }else{
                    plantGarden(gardenCenter);
                }

                // Very Important
                waterTrees();

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }
    }

    void checkDeath(int ch) throws GameActionException{
        // If dying, declare dead
        if(rc.getHealth() <= 10 && !dead){
            dead = true;
            rc.broadcast(ch, rc.readBroadcast(ch) - 1);
            rc.broadcast(Constants.GARDENER_STABLE_CH, rc.readBroadcast(Constants.GARDENER_STABLE_CH) - 1);
        }
    }

    void build(RobotType unit) throws GameActionException{
        if(rc.hasRobotBuildRequirements(unit) && rc.canBuildRobot(unit, Direction.getNorth())){
            rc.buildRobot(unit, Direction.getNorth());
            roundsToBuild = 10;
        }
    }

    void plantGarden(MapLocation centre) throws GameActionException {
        //  2 7 5
        //  1 X 4
        //  0 6 3
        if (roundsToBuild > 3) {
            waterTrees();
        }else{
            MapLocation target = centre;
            switch (treesInGarden) {
                case 0:
                    target = centre.add(Direction.getWest(), 2);
                    break;
                case 1:
                    target = centre.add(Direction.getWest(), 2).add(Direction.getNorth(), 2);
                    break;
                case 2:
                    target = centre.add(Direction.getWest(), 2).add(Direction.getNorth(), 4);
                    break;
                case 3:
                    target = centre.add(Direction.getEast(), 2);
                    break;
                case 4:
                    target = centre.add(Direction.getEast(), 2).add(Direction.getNorth(), 2);
                    break;
                case 5:
                    target = centre.add(Direction.getWest(), 2).add(Direction.getNorth(), 4);
                    break;
                case 6:
                    target = centre.add(Direction.getNorth(), GameConstants.GENERAL_SPAWN_OFFSET);
                    break;
                case 7:
                    target = centre;
                    break;
                default:
                    // Check if tree died & replace it
            }
            goTo(target);
            if (here.equals(target) && rc.hasTreeBuildRequirements() && rc.canPlantTree(Direction.getSouth())) {
                rc.plantTree(Direction.getSouth());
                garden[treesInGarden] = rc.senseTreeAtLocation(here.add(Direction.getSouth()));
                ++treesInGarden;
                roundsToBuild = 10;
            }
        }
        if(!stable && treesInGarden > 7){
            stable = true;
            rc.broadcast(Constants.GARDENER_STABLE_CH, rc.readBroadcast(Constants.GARDENER_STABLE_CH) + 1);
        }
    }

    void waterTrees() throws GameActionException{
        float minHP = 100;
        int minID = 0;
        for(int i = 0; i < treesInGarden; ++i){
            float hp = garden[i].getHealth();
            if(hp < minHP){
                minHP = hp;
                minID = i;
            }
            if(rc.canWater(garden[minID].getID()))
                rc.water(garden[minID].getID());
        }
    }

}
