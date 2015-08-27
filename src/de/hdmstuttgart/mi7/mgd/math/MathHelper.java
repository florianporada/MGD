package de.hdmstuttgart.mi7.mgd.math;

/**
 * Created by florianporada on 27.08.15.
 */
public class MathHelper {
    public static float clamp(float value, float min, float max) {
        value = Math.min(value, max);
        value = Math.max(value, min);
        return value;
    }
}
