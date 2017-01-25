package frameworks.runner;

import frameworks.logging.Logging;
import frameworks.web.BlockJUnit4ClassRunnerWithParametersInjector;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

/**
 * @author Juan Krzemien
 */
public class InjectPageObjectsParametersRunnerFactory implements ParametersRunnerFactory, Logging {

    public InjectPageObjectsParametersRunnerFactory() {
    }

    public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
        return new BlockJUnit4ClassRunnerWithParametersInjector(test);
    }
}
