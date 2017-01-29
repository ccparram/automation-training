package pluggable.plugin;

import java.util.Collection;

/**
 * @author Juan Krzemien
 */
public interface PluginManager extends AutoCloseable {

    Collection<Plugin> getPlugins();

}
