package pluggable.plugin.events;

import pluggable.plugin.Plugin;

import java.util.Collection;

/**
 * @author Juan Krzemien
 */
public interface PluginsUpdatedEvent {

    void pluginsUpdated(Collection<Plugin> updatedPlugins);

}

