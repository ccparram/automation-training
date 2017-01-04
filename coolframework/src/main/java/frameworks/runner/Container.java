package frameworks.runner;

import frameworks.annotations.Bean;
import frameworks.logging.Logging;
import frameworks.web.BasePageObject;
import frameworks.web.WebDriverProvider;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.behaviors.ThreadCaching;
import org.picocontainer.lifecycle.StartableLifecycleStrategy;
import org.picocontainer.monitors.ConsoleComponentMonitor;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public enum Container implements Logging {

    CONTAINER;

    private final DefaultPicoContainer container;
    private Reflections reflections;

    Container() {
        this.container = new DefaultPicoContainer(new ThreadCaching(), new StartableLifecycleStrategy(new ConsoleComponentMonitor()) {
            @Override
            public boolean isLazy(ComponentAdapter<?> adapter) {
                return true;
            }
        }, null);
        container.addAdapter(new WebDriverProvider());
    }

    public Reflections getReflections() {
        return reflections;
    }

    public void scanBeansIn(Class<?> clazz) {
        if (reflections == null) {
            this.reflections = new Reflections(
                    new ConfigurationBuilder()
                            .addScanners(new FieldAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner())
                            .forPackages(Container.class.getPackage().getName(), clazz.getPackage().getName())
            );
            registerBeans(reflections.getSubTypesOf(BasePageObject.class));
            registerBeans(reflections.getTypesAnnotatedWith(Bean.class));
        }
    }

    private <T> void registerBeans(Set<Class<? extends T>> beans) {
        final AtomicInteger count = new AtomicInteger();
        beans.forEach(b -> {
            getLogger().info(format("Detected [%s]...", b.getName()));
            container.addComponent(b);
            count.incrementAndGet();
        });
        getLogger().info(format("Registered %s beans...", count.get()));
    }

    public Object getComponent(Class<?> type) {
        return container.getComponent(type);
    }
}
