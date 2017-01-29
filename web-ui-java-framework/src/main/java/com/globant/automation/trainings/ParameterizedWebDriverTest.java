package com.globant.automation.trainings;

import com.globant.automation.trainings.logging.Logging;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

/**
 * Super class created just to avoid using 2 annotations per suite.
 * If you need to extend from another class then just copy the 2 annotations in this class together on to your suite class.
 *
 * @author Juan Krzemien
 */
@RunWith(WebDriverRunner.ParametrizedParallel.class)
@UseParametersRunnerFactory(value = ParameterizedWebDriverTest.InjectPageObjectsParametersRunnerFactory.class)
public abstract class ParameterizedWebDriverTest {

    public static class InjectPageObjectsParametersRunnerFactory implements ParametersRunnerFactory, Logging {

        public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
            return new RunnerWithParametersInjector(test);
        }

    }

}
