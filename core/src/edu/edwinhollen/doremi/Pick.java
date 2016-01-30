package edu.edwinhollen.doremi;

import java.util.Random;

/**
 * Created by Fubar on 1/17/2016.
 */
public class Pick {
    private static Random random;
    static{
        random = new Random();
    }
    public static Integer integer(Integer min, Integer max){
        return random.nextInt(max - min + 1) + min;
    }
    public static <T> T pick(T[] array){
        return array[integer(0, array.length - 1)];
    }
    public static boolean bool(double likelihood){
        return integer(0, 100) < likelihood * 100;
    }

    public static boolean bool(){
        return bool(0.5);
    }
}
