package frameworks;

import frameworks.logging.Logging;
import frameworks.web.BrowserQueue;
import frameworks.web.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public enum Container implements Logging {

    CONTAINER;

    private final AnnotationConfigApplicationContext container;

    Container() {
        this.container = new AnnotationConfigApplicationContext(Context.class);
        getLogger().info(format("Registered %s beans...", container.getBeanDefinitionCount()));
        container.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                getLogger().info("Closing Spring context!");
                container.getBeansOfType(WebDriver.class).forEach((s, driver) -> driver.quit());
            }
        });
    }

    public Object autoWire(Object object) {
        container.getAutowireCapableBeanFactory().autowireBean(object);
        return object;
    }

    public void destroy() {
        container.destroy();
    }

    public Container with(Class<?> clazz) {
        container.scan(clazz.getPackage().getName());
        return this;
    }

    @Configuration
    @ComponentScan("frameworks")
    static class Context {

        @Bean
        BrowserQueue getBrowserQueue() {
            return new BrowserQueue();
        }

        @Bean
        WebDriverProvider getWebDriverProvider(@Autowired BrowserQueue browserQueue) {
            return new WebDriverProvider(browserQueue);
        }

    }
}
