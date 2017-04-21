package com.globant.automation.trainings.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Helper class for random numbers generation
 *
 * @author Juan Krzemien
 */
public class Randomness {

    private Randomness() {
    }

    public static int nextIntBetween(int a, int b) {
        int min = Math.min(a, b);
        int max = Math.max(a, b);
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}
