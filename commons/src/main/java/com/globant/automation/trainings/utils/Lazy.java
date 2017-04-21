package com.globant.automation.trainings.utils;

import java.util.function.Supplier;

/**
 * Supplier that allows for lazy instantiation of objects
 *
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
