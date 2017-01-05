package frameworks.container;

import frameworks.logging.Logging;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
    }

    public Object inject(Object object) {
        container.getAutowireCapableBeanFactory().autowireBean(object);
        return object;
    }

    public void dispose() {
        container.destroy();
    }

    public Container with(Class<?> clazz) {
        container.scan(clazz.getPackage().getName());
        return this;
    }

    @Configuration
    @ComponentScan("frameworks")
    static class Context {

    }
}
