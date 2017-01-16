package frameworks.web;

import frameworks.logging.Logging;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public class BrowserQueue implements Logging {

    private final BlockingQueue<Browser> queue = new LinkedBlockingDeque<>();

    public void put(Browser browser) throws InterruptedException {
        getLogger().info(format("Queueing browser %s...", browser.name()));
        queue.put(browser);
    }

    public Browser take() throws InterruptedException {
        getLogger().info("Removing browser from queue...");
        Browser browser = queue.take();
        getLogger().info(format("Took browser %s from queue...", browser.name()));
        return browser;
    }
}
