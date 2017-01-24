package frameworks.runner;

import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * This class is supposed to be used from JUnit's RunWith annotation.
 * Adds support for parallel test runs (default is double the number of available processors).
 *
 * @author Juan Krzemien
 */
public class Parallelism extends BlockJUnit4ClassRunner {

    public Parallelism(Class<?> clazz) throws Throwable {
        super(clazz);
        setScheduler(new ThreadPoolScheduler());
    }
}
