package de.hdmstuttgart.mi7.mgd.math;

import de.hdmstuttgart.mi7.mgd.gameObject.EnemyObject;
import de.hdmstuttgart.mi7.mgd.gameObject.GameObject;

import java.util.ArrayList;
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
        Random rand = new Random();
        float randomNum = rand.nextFloat() * (max - min) + min;

        return randomNum;
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static int getMinValueIndex(ArrayList<EnemyObject> array){
        int minIndex = 0;
        float minValue = array.get(0).getMatrix().m[13];
        for(int i=1;i<array.size();i++){
            if(array.get(i).getMatrix().m[13] < minValue){
                minValue = array.get(i).getMatrix().m[13];
                minIndex = i;
            }
        }
        return minIndex;
    }


}
