package pluggable.filter;

import com.globant.automation.trainings.logging.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pluggable.plugin.Plugin;

import java.util.Collection;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author Juan Krzemien
 */
public class FilterChain implements Logging {

    private static final Logger LOG = LoggerFactory.getLogger(FilterChain.class);

    public static Object processFilters(Collection<Plugin> plugins, Object object) {
        PriorityBlockingQueue<Plugin> orderedPlugins = new PriorityBlockingQueue<>(plugins);

        Object response = object;
        for (Plugin plugin : orderedPlugins) {
            // pass request & response through various filters
            response = plugin.execute(response);
        }
        LOG.info("Processed object: " + object.toString());
        return response;
    }

}

