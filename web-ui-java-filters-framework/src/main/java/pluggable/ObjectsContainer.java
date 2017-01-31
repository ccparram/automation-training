package pluggable;

import com.globant.automation.trainings.logging.Logging;
import pluggable.plugin.AbstractPlugin;
import pluggable.plugin.PluginManager;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

import static java.util.stream.Collectors.toList;

/**
 * @author Juan Krzemien
 */
public enum ObjectsContainer implements AutoCloseable, Logging {

    INSTANCE;

    private final ThreadLocal<Set<Object>> objects = ThreadLocal.withInitial(HashSet::new);

    private PluginManager pluginManager = new PluginManager();

    ObjectsContainer() {
        pluginManager.onPluginFound(plugins -> getForThread().forEach(o -> processFilters(plugins, o)));
    }

    private Set<Object> getForThread() {
        return objects.get();
    }

    public <T> void add(T object) {
        Object processedObject = processFilters(pluginManager.getPlugins(), object);
        getForThread().add(processedObject);
    }

    public <T> List<T> get(Class<T> objectType) {
        return getForThread()
                .parallelStream()
                .filter(o -> objectType.isAssignableFrom(o.getClass()))
                .map(objectType::cast)
                .collect(toList());
    }

    @Override
    public void close() throws Exception {
        getForThread().clear();
        objects.remove();
        pluginManager.close();
    }

    private Object processFilters(Collection<? extends AbstractPlugin> plugins, Object object) {
        SortedSet<? extends AbstractPlugin> sortedSet = new ConcurrentSkipListSet<>(plugins);

        Object response = object;
        for (AbstractPlugin plugin : sortedSet) {
            Class<?> pluginWorksOnType = getPluginWorkType(plugin);
            if (pluginWorksOnType.isAssignableFrom(object.getClass())) {
                // pass request & response through various filters
                response = plugin.execute(pluginWorksOnType.cast(response));
            }
        }
        getLogger().info("Processed object: " + object.toString());
        return response;
    }

    private Class<?> getPluginWorkType(AbstractPlugin plugin) {
        return (Class<?>) ((ParameterizedType) plugin.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
