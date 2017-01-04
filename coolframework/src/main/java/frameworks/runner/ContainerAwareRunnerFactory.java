package frameworks.runner;

import frameworks.annotations.PageObject;
import frameworks.logging.Logging;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

import java.lang.reflect.Field;
import java.util.Set;

import static frameworks.runner.Container.CONTAINER;

/**
 * @author Juan Krzemien
 */
public class ContainerAwareRunnerFactory implements ParametersRunnerFactory, Logging {

    public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
        CONTAINER.scanBeansIn(test.getTestClass().getJavaClass());
        return new BlockJUnit4ClassRunnerWithParameters(test) {
            @Override
            public Object createTest() throws Exception {
                Object testInstance = super.createTest();
                autowireTest(testInstance);
                return testInstance;
            }
        };
    }

    private Object autowireTest(Object test) {
        Set<Field> poms = CONTAINER.getReflections().getFieldsAnnotatedWith(PageObject.class);
        poms.forEach(f -> {
            Object bean = CONTAINER.getComponent(f.getType());
            try {
                f.setAccessible(true);
                f.set(test, bean);
            } catch (IllegalAccessException e) {
                getLogger().error(e.getLocalizedMessage(), e);
            }
        });
        return test;
    }
}
