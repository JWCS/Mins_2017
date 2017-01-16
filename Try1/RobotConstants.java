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

    // int[] broadcast = new int[GameConstants.BROADCAST_MAX_CHANNELS];
    int EARLY_END = 500;


    // Channels
    // int Reserve_channels: = 0-9
    public static final int CHANNEL0                    = 0;
    public static final int BUFFERS_LAST_COUNT_CH       = 1;

    // Various Specific Channels
    public static final int[]   G_ASSIGN_CHS                = { 10, 11, 12, 13, 14, 15, 16, 17 };    // 8 channels
    public static final int     G_ASSIGN_NUM_CH             = 8;
    public static final int[]   F_GARDEN_LIST_CHS           = { 18, 19, 20, 21, 22 };               // 5 channels
    public static final int     F_GARDEN_NUM_CH             = 5;
    int G_F_TASK_CH     = 16;       // Also serves as overflow G storage. Last bits show 'extra' Gs
    int FARMER_FLAG     = 0b11111111111111111111111111111110;
    int BUILDER_FLAG    = 0b11111111111111111111111111111111;


    // Enemy Archons
    int E_A_CH0         = 20;
    int E_A_CH1         = 21;
    int E_A_CH2         = 22;

    // Notification Buffer
    int GEN_BUFFER_START_CH     = 100;


}
