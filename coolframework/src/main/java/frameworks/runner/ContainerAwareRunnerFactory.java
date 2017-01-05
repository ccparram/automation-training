package frameworks.runner;

import frameworks.logging.Logging;
import frameworks.tests.BaseTest;
import frameworks.web.Browser;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

import java.lang.reflect.Field;

import static frameworks.container.BrowserQueue.BROWSER_QUEUE;
import static frameworks.container.Container.CONTAINER;

/**
 * @author Juan Krzemien
 */
public class ContainerAwareRunnerFactory implements ParametersRunnerFactory, Logging {

    public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
        return new BlockJUnit4ClassRunnerWithParameters(test) {
            @Override
            public Object createTest() throws Exception {
                Object testInstance = super.createTest();
                autoWireTest(testInstance);
                return testInstance;
            }
        };
    }

    private Object autoWireTest(Object test) throws Exception {
        Class<?> cls = validateAndExtractBaseTestClass(test);
        BROWSER_QUEUE.put((Browser) cls.getDeclaredField("currentBrowser").get(test));
        Field f = cls.getDeclaredField("initialPage");
        Object bean = CONTAINER.getComponent(f.getType());
        try {
            f.setAccessible(true);
            f.set(test, bean);
        } catch (IllegalAccessException e) {
            getLogger().error(e.getLocalizedMessage(), e);
        }
        return test;
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
