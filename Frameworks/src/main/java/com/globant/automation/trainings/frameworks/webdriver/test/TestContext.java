package com.globant.automation.trainings.frameworks.webdriver.test;

import com.globant.automation.trainings.frameworks.webdriver.test.pageobject.PageObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;

/**
 * Created by jkrzemien on 29/07/2016.
 */
public class TestContext {

    private String name;
    private Map<String, Object> beans;
    private Thread owningThread;
    volatile boolean canKill;
    private GenericWebDriverTest<? extends PageObject> parentTest;

    private static final ThreadLocal<TestContext> LOCAL = new ThreadLocal<TestContext>() {
        @Override
        protected TestContext initialValue() {
            return new TestContext(Thread.currentThread().getName());
        }
    };

    public static void setCurrent(TestContext context) {
        LOCAL.set(context);
        context.owningThread = Thread.currentThread();
    }

    public static TestContext getCurrent() {
        return LOCAL.get();
    }

    public static void removeCurrent() {
        LOCAL.remove();
    }

    public Set<String> getAllKeys() {
        return this.beans.keySet();
    }

    public TestContext(String name) {
        this(name, new HashMap<>());
    }

    public TestContext(String name, Map<String, Object> beans) {
        this.canKill = false;
        this.name = name;
        this.beans = beans;
        this.canKill = false;
    }

    public String toString() {
        return super.toString() + format("[%s, %s]", this.name, this.beans.toString());
    }

    Thread getThread() {
        return this.owningThread;
    }

    GenericWebDriverTest getParentTest() {
        return this.parentTest;
    }

    public String name() {
        return this.name;
    }

    public <T> T get(String key) {
        return (T) this.beans.get(key);
    }

    public <T> void set(String key, T val) {
        this.beans.put(key, val);
    }
}

