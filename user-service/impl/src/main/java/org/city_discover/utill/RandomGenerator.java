package org.city_discover.utill;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomGenerator {
    private static final Random random = new Random();

    public static String generateCode(int from, int to) {
        int randomNum = random.nextInt(to - from + 1) + from;
        return String.valueOf(randomNum);
    }
}
