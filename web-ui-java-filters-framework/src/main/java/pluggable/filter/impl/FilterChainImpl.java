package pluggable.filter.impl;

import pluggable.filter.FilterChain;
import pluggable.plugin.Plugin;
import pluggable.plugin.PluginManager;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author Juan Krzemien
 */
public class FilterChainImpl implements FilterChain {

    // filter chain
    private final PriorityBlockingQueue<Plugin> myPlugins;

    public FilterChainImpl(PluginManager pluginsManager) {
        this.myPlugins = new PriorityBlockingQueue<>(pluginsManager.getPlugins());
    }

    @Override
    public Object processFilter(Object input) {
        Object response = input;
        // apply filters
        for (Plugin plugin : myPlugins) {
            // pass request & response through various filters
            response = plugin.execute(response);
        }
        return response;
    }

    @Override
    public void addFilter(Plugin plugin) {
        myPlugins.add(plugin);
    }
}

