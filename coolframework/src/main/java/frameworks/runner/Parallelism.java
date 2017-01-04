package frameworks.runner;

import org.junit.runners.Parameterized;
import org.junit.runners.model.RunnerScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * This class is supposed to be used from JUnit's RunWith annotation.
 * Adds support for parallel test runs (default is 5).
 * Extends JUnit's Parameterized class, so you still get the parameterized tests :)
 *
 * @author Juan Krzemien
 */
public class Parallelism extends Parameterized {

    public Parallelism(Class<?> clazz) throws Throwable {
        super(clazz);
        setScheduler(new ThreadPoolScheduler());
    }

    static class ThreadPoolScheduler implements RunnerScheduler {

        private static final int TIMEOUT_MINUTES = 10;

        private final ExecutorService executor;

        ThreadPoolScheduler() {
            String threads = System.getProperty("junit.parallel.threads", "5");
            int numThreads = Integer.parseInt(threads);
            this.executor = Executors.newFixedThreadPool(numThreads, new TestThreadPool());
        }

        @Override
        public void finished() {
            executor.shutdown();
            try {
                executor.awaitTermination(TIMEOUT_MINUTES, MINUTES);
            } catch (InterruptedException exc) {
                throw new RuntimeException(exc);
            }
        }

        @Override
        public void schedule(Runnable childStatement) {
            executor.submit(childStatement);
        }

        static class TestThreadPool implements ThreadFactory {
            private static final AtomicInteger poolNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "TestPool-Thread-" + poolNumber.getAndIncrement());
            }
        }
    }
}
