package frameworks.runner;

import frameworks.web.BrowserQueue;
import frameworks.logging.Logging;
import frameworks.tests.BaseTest;
import frameworks.web.Browser;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

import static frameworks.Container.CONTAINER;

/**
 * @author Juan Krzemien
 */
public class ContainerAwareRunnerFactory implements ParametersRunnerFactory, Logging {

    private BrowserQueue browserQueue = new BrowserQueue();

    public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
        return new BlockJUnit4ClassRunnerWithParameters(test) {
            @Override
            public Object createTest() throws Exception {
                return autoWireTest(super.createTest());
            }
        };
    }

    private Object autoWireTest(Object test) throws Exception {
        Class<?> cls = validateAndExtractBaseTestClass(test);
        browserQueue.put((Browser) cls.getDeclaredField("currentBrowser").get(test));
        return CONTAINER.inject(test);
    }

    private Class<?> validateAndExtractBaseTestClass(Object test) {
        Class<?> cls = test.getClass();
        while (cls != BaseTest.class && cls != Object.class) {
            cls = cls.getSuperclass();
        }
        if (cls == Object.class) throw new RuntimeException("Test suites MUST inherit from BaseTest abstract class!");
        return cls;
    }
}
