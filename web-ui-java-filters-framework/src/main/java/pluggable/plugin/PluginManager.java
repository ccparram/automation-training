package pluggable.plugin;

import com.globant.automation.trainings.logging.Logging;
import org.reflections.Reflections;
import pluggable.plugin.events.PluginsFound;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public class PluginManager implements AutoCloseable, Logging {

    private static final Reflections REFLECTIONS = new Reflections(PluginManager.class.getPackage().getName());

    private final Set<PluginsFound> observers = new HashSet<>();
    private final Set<AbstractPlugin> plugins = new HashSet<>();

    public PluginManager() {
        Set<Class<? extends AbstractPlugin>> pluginClasses = REFLECTIONS.getSubTypesOf(AbstractPlugin.class);
        createPlugins(pluginClasses);
    }

    private void createPlugins(Set<Class<? extends AbstractPlugin>> pluginClasses) {
        pluginClasses.forEach(pluginClass -> {
            getLogger().info(format("Detected Plugin class: %s", pluginClass.getName()));
            try {
                AbstractPlugin plugin = createPlugin(pluginClass);
                plugins.add(plugin);
                plugin.load();
            } catch (Exception e) {
                getLogger().error(format("Could not instantiate Plugin class: %s", pluginClass.getName()), e);
            }
        });
        notifyObservers();
    }

    private AbstractPlugin createPlugin(Class<? extends AbstractPlugin> pluginClass) throws Exception {
        Constructor<? extends AbstractPlugin> constructor = pluginClass.getConstructor();
        return constructor.newInstance();
    }

    public void onPluginFound(PluginsFound onPluginFound) {
        observers.add(onPluginFound);
    }

    public Set<AbstractPlugin> getPlugins() {
        return plugins;
    }

    private void notifyObservers() {
        observers.parallelStream().forEach(observer -> observer.pluginFound(plugins));
    }

    @Override
    public void close() throws Exception {
        observers.clear();
        plugins.clear();
    }
}
