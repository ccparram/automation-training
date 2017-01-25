package com.globant.automation.trainings.functions;

/**
 * @author Juan Krzemien
 */
@FunctionalInterface
public interface TriFunction<T, U, R> {

    /**
     * Applies this function to the given arguments by type.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(T t, U u);
}
