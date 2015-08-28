package de.hdmstuttgart.mi7.mgd.math;

import java.util.Random;

/**
 * Created by florianporada on 27.08.15.
 */
public class MathHelper {
    public static float clamp(float value, float min, float max) {
        value = Math.min(value, max);
        value = Math.max(value, min);
        return value;
    }

    public static float randfloat(float min, float max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        float randomNum = rand.nextFloat() * (max - min) + min;

        return randomNum;
    }
}
