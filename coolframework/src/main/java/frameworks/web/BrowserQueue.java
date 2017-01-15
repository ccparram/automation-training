package frameworks.web;

import frameworks.logging.Logging;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Juan Krzemien
 */
@Lazy
@Component
public class BrowserQueue implements Logging {

    private static final int MAX_BROWSERS = 15;
    private static final ThreadLocal<BlockingQueue<Browser>> queue = ThreadLocal.withInitial(() -> new ArrayBlockingQueue<>(MAX_BROWSERS));

    public void put(Browser browser) {
        try {
            queue.get().put(browser);
        } catch (InterruptedException e) {
            getLogger().error(e.getLocalizedMessage(), e);
        }
    }

    public Browser get() {
        try {
            return queue.get().take();
        } catch (InterruptedException e) {
            getLogger().error(e.getLocalizedMessage(), e);
        }
        return Browser.CHROME;
    }
}
