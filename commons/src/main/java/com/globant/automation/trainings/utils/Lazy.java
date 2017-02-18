package com.globant.automation.trainings.utils;

import java.util.function.Supplier;

/**
 * @author Juan Krzemien
 */
public class Lazy {

    private Lazy() {
    }

    public static <Z> Supplier<Z> lazily(Supplier<Z> supplier) {
        return new Supplier<Z>() {
            Z value; // = null

            @Override
            public Z get() {
                if (value == null)
                    value = supplier.get();
                return value;
            }
        };
    }
}
