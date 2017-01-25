package frameworks.web;

import frameworks.runner.InjectPageObjectsParametersRunnerFactory;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

/**
 * Super class created just to avoid using 2 annotations per suite.
 * If you need to extend from another class then just copy the 2 annotations in this class together on to your suite class.
 *
 * @author Juan Krzemien
 */
@RunWith(ParametrizedWebDriverRunner.class)
@UseParametersRunnerFactory(value = InjectPageObjectsParametersRunnerFactory.class)
public class ParameterizedWebDriverTest {
}
