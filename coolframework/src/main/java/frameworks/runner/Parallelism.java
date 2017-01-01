package frameworks.runner;

import org.junit.runners.Parameterized;

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


}
