package com.globant.automation.trainings.frameworks.webdriver.webframework.events;

import com.globant.automation.trainings.frameworks.webdriver.webframework.events.messages.interfaces.IEvent;
import com.globant.automation.trainings.frameworks.webdriver.webframework.logging.Logging;
import net.engio.mbassy.bus.MBassador;

/**
 * @author Juan Krzemien
 */
public enum EventBus implements Logging {

    FRAMEWORK;

    private static final MBassador<? super IEvent> BUS = new MBassador<>();

    EventBus() {
        getLogger().info("Initializing Event Bus system...");
    }

    public void suscribe(Object listener) {
        BUS.subscribe(listener);
    }

    public void unsuscribe(Object listener) {
        BUS.unsubscribe(listener);
    }

    public <T extends IEvent> void post(T event) {
        BUS.post(event).now();
    }

    public <T extends IEvent> void postAsync(T event) {
        BUS.post(event).asynchronously();
    }
}
