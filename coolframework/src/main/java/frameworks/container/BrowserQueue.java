package frameworks.container;

import frameworks.web.Browser;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Juan Krzemien
 */
public enum BrowserQueue {

    BROWSER_QUEUE;

    public static final int MAX_BROWSERS = 15;

    private final ThreadLocal<BlockingQueue<Browser>> queue = ThreadLocal.withInitial(() -> new ArrayBlockingQueue<>(MAX_BROWSERS));


    public void put(Browser browser) throws InterruptedException {
        queue.get().put(browser);
    }

    public Browser get() throws InterruptedException {
        return queue.get().take();
    }
}
