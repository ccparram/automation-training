package pluggable.plugin.events;

import pluggable.plugin.AbstractPlugin;

import java.util.Set;

/**
 * @author Juan Krzemien
 */
public interface PluginsFound {

    void pluginFound(Set<? extends AbstractPlugin> plugin);

}
