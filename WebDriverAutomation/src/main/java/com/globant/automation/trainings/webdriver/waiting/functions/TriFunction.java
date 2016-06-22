package com.globant.automation.trainings.webdriver.waiting.functions;

/**
 * Created by Juan Krzemien on 6/22/2016.
 */
@FunctionalInterface
public interface TriFunction<T, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(T t, U u);
}
