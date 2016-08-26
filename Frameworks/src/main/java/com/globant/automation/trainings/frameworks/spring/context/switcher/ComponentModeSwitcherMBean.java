package com.globant.automation.trainings.frameworks.spring.context.switcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * @author Juan Krzemien
 */
@ManagedResource(objectName = "client:service=ComponentModeSwitcherMBean", description = "Switch between component's operation modes")
public class ComponentModeSwitcherMBean implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(ComponentModeSwitcherMBean.class.getName());

    @Autowired
    private Environment env;

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private ConfigurableEnvironment springEnvironment;

    private String currentState = "default";

    @ManagedOperation(description = "Hit me to switch component to Simulator mode!")
    public void switchToSimulatorMode() {
        log.info("Switching to [SIMULATOR] profile...");
        setProfileAndRefreshContext("simulator");
    }

    @ManagedOperation(description = "Hit me to switch component to Normal mode!")
    public void switchToNormalMode() {
        log.info("Switching to [DEFAULT] profile...");
        setProfileAndRefreshContext("default");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (appContext == null) {
            this.appContext = event.getApplicationContext();
            this.springEnvironment = ((AbstractRefreshableApplicationContext) appContext).getEnvironment();
        }
    }

    private void setProfileAndRefreshContext(String profile) {
        if (profile.equals(currentState))
            return;

        /**
         * Restricts this class actions to pre-production environments only
         * We don't want to let you play with Production yet :P
         */
        if ((appContext != null) && isPreProductionEnvironment()) {
            springEnvironment.setActiveProfiles(profile);
            try {
                ((AbstractRefreshableApplicationContext) appContext).refresh();
                this.currentState = profile;
            } catch (BeansException | IllegalStateException ignored) {
            } catch (Exception e) {
                log.error("Something went wrong while refreshing the Spring container " + e.getMessage());
            }
        } else {
            throw new RuntimeException("You can't play around with this MBean if it is not on a Dev/QA environment!");
        }
    }

    /**
     * @return true if environment is pre-production, false otherwise
     */
    private boolean isPreProductionEnvironment() {
        /**
         * Some way of identifying the current environment...
         * Not the point of this example
         */
        return !"PROD".equals(env.getRequiredProperty("current.environment"));
    }
}


