package pluggable.plugin;

import pluggable.plugin.events.PluginsUpdatedEvent;

import java.util.Collection;

/**
 * @author Juan Krzemien
 */
public interface PluginManager extends AutoCloseable {

    Collection<Plugin> getPlugins();

    void onPluginsUpdate(PluginsUpdatedEvent pluginsUpdatedEvent);

}
