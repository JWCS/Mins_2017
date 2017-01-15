package Try1;
import battlecode.common.*;
import java.util.BitSet.*;

/**
 * Package <Try1>, Mins_2017 Project
 * Created by JWCS on 1/12/17.
 * This holds all the constants implemented by RobotCode.
 */

// Consider making this an interface
public class RobotConstants {

    int[] broadcast = new int[GameConstants.BROADCAST_MAX_CHANNELS];
    int EARLY_END = 500;


    // Channels
    // int Reserve_channels: = 0-9
    int G_ASSIGN_CH0    = 10;
    int G_ASSIGN_CH1    = 11;
    int G_STATUS_CH0    = 17;
    int G_STATUS_CH1    = 18;
    int G_TASK_CH0      = 12;
    int G_TASK_CH1      = 13;
    int G_TASK_CH2      = 14;
    int G_TASK_CH3      = 15;
    int G_F_TASK_CH     = 16;       // Also serves as overflow G storage. Last bits show 'extra' Gs
    int FARMER_FLAG     = 0b11111111111111111111111111111110;
    int BUILDER_FLAG    = 0b11111111111111111111111111111111;


    // Enemy Archons
    int E_A_CH0         = 20;
    int E_A_CH1         = 21;
    int E_A_CH2         = 22;

}
