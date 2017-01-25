package com.globant.automation.trainings.webframework.events;

import com.globant.automation.trainings.logging.Logging;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;

/**
 * @author Juan Krzemien
 */
public enum EventBus implements Logging {

    FRAMEWORK;

    private final MBassador<? super IEvent> BUS;

    EventBus() {
        getLogger().info("Initializing internal Event Bus system...");
        this.BUS = new MBassador<>(new BusConfiguration()
                .addFeature(Feature.SyncPubSub.Default())
                .addFeature(Feature.AsynchronousHandlerInvocation.Default())
                .addFeature(Feature.AsynchronousMessageDispatch.Default())
                .addPublicationErrorHandler(new IPublicationErrorHandler.ConsoleLogger()));
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
