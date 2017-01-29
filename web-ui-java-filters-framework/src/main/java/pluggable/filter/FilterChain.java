package pluggable.filter;

import pluggable.plugin.Plugin;

/**
 * @author Juan Krzemien
 */
public interface FilterChain {

    Object processFilter(Object input);

    void addFilter(Plugin plugin);

}
