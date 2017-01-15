package utils;

/**
 * Package <methods>, Mins_2017 Project
 * Created by JWCS on 1/12/17.
 */


public class Comms {

    // Get 4bit mask from a 0-8 pos within an int
    public static int getMask4( int posMod8 ){     // Assumes 4bit mask
        switch (posMod8){
            case 0: return 0b1111;
            case 1: return 0b11110000;
            case 2: return 0b111100000000;
            case 3: return 0b1111000000000000;
            case 4: return 0b11110000000000000000;
            case 5: return 0b111100000000000000000000;
            case 6: return 0b1111000000000000000000000000;
            case 7: return 0b11110000000000000000000000000000;
            default: return  0;
        }
    }

}
