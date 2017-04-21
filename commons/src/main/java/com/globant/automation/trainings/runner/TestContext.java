package com.globant.automation.trainings.runner;

import static java.util.Optional.ofNullable;

/**
 * @author Juan Krzemien
 */
public class TestContext {

    private static final ThreadLocal<Context> CONTEXT_PER_THREAD = new ThreadLocal<>();

    TestContext() {
    }

    public static Context get() {
        return CONTEXT_PER_THREAD.get();
    }

    public static void set(Context context) {
        ofNullable(context).ifPresent(ctx -> {
            CONTEXT_PER_THREAD.set(context);
            ctx.init();
        });
    }

    public static void remove() {
        ofNullable(get()).ifPresent(Context::destroy);
        CONTEXT_PER_THREAD.remove();
    }

}
