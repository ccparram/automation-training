package frameworks.runner;

import frameworks.logging.Logging;
import frameworks.tests.BaseTest;
import frameworks.web.BrowserQueue;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;
import org.springframework.beans.factory.annotation.Autowired;

import static frameworks.Container.CONTAINER;

/**
 * @author Juan Krzemien
 */
public class ContainerAwareRunnerFactory implements ParametersRunnerFactory, Logging {

    @Autowired
    private BrowserQueue browserQueue;

    public ContainerAwareRunnerFactory() {
        CONTAINER.autoWire(this);
    }

    public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
        return new BlockJUnit4ClassRunnerWithParameters(test) {
            @Override
            public Object createTest() throws Exception {
                Object test = super.createTest();
                validateBaseTestClassPresence(test);
                browserQueue.put(((BaseTest) test).currentBrowser);
                return CONTAINER.autoWire(test);
            }
        };
    }

    private void validateBaseTestClassPresence(Object test) {
        Class<?> cls = test.getClass();
        while (cls != BaseTest.class && cls != Object.class) {
            cls = cls.getSuperclass();
        }
        if (cls == Object.class) throw new RuntimeException("Test suites MUST inherit from BaseTest abstract class!");
    }
}
