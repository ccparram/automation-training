package frameworks.runner;

import frameworks.logging.Logging;
import frameworks.utils.Reflections;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

import static frameworks.utils.Reflections.injectFieldsPageObject;
import static java.util.Arrays.stream;

/**
 * @author Juan Krzemien
 */
public class InjectPageObjectsRunnerFactory implements ParametersRunnerFactory, Logging {

    public InjectPageObjectsRunnerFactory() {
    }

    public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
        return new BlockJUnit4ClassRunnerWithParameters(test) {
            @Override
            public Object createTest() throws Exception {
                Object test = super.createTest();
                stream(test.getClass().getDeclaredFields()).filter(Reflections::isPom).forEach(f -> injectFieldsPageObject(f, test));
                return test;
            }
        };
    }

}
