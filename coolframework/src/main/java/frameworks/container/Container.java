package frameworks.container;

import frameworks.annotations.Bean;
import frameworks.logging.Logging;
import frameworks.web.BasePageObject;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.injectors.ProviderAdapter;
import org.reflections.Reflections;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public enum Container implements Logging {

    CONTAINER;

    private final DefaultPicoContainer container = new DefaultPicoContainer();

    private final AtomicInteger count = new AtomicInteger();

    Container() {
        Reflections reflections = new Reflections();
        registerProviders(reflections.getSubTypesOf(ProviderAdapter.class));
        registerBeans(reflections.getSubTypesOf(BasePageObject.class));
        registerBeans(reflections.getTypesAnnotatedWith(Bean.class));
        getLogger().info(format("Registered %s beans...", count.get()));
    }

    private void registerProviders(Set<Class<? extends ProviderAdapter>> providers) {
        providers.forEach(p -> {
            getLogger().info(format("Detected [%s]...", p.getName()));
            try {
                container.addAdapter(p.newInstance());
                count.incrementAndGet();
            } catch (InstantiationException | IllegalAccessException e) {
                getLogger().error(e.getLocalizedMessage(), e);
            }
        });
    }


    private void registerBeans(Set<?> beans) {
        beans.forEach(b -> {
            getLogger().info(format("Detected [%s]...", b));
            container.addComponent(b);
            count.incrementAndGet();
        });
    }

    public Object getComponent(Class<?> type) {
        return container.getComponent(type);
    }
}
