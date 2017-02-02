package crap;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.runner.ParametrizedParallelism;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.config.Framework;
import com.globant.automation.trainings.webdriver.server.SeleniumServerStandAlone;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.Set;

import static com.globant.automation.trainings.utils.Reflection.*;
import static java.lang.String.format;
import static java.util.Arrays.stream;

/**
 * @author Juan Krzemien
 */
@RunWith(ParametrizedParallelism.class)
public abstract class BaseJUnit implements Logging {

    @Parameter
    public Browser browser;

    @Rule
    public MethodRule pomInjector = new MethodRule() {

        @Override
        public Statement apply(Statement base, FrameworkMethod method, Object target) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    before();
                    stream(target.getClass().getDeclaredFields())
                            .filter(this::isPom)
                            .forEach(field -> {
                                try {
                                    Object pom = injectFieldOwnInstance(field, target);
                                    setField(field, target, pom);
                                } catch (Exception e) {
                                    getLogger().error(format("Could not instantiate %s. Make sure class has a non-arg constructor.", field.getType().getName()));
                                }
                            });
                    base.evaluate();
                    after();
                }

                private boolean isPom(Field field) {
                    return fieldIsSubClassOf(field, PageObject.class);
                }

            };
        }


        private void after() {
            WebDriverStorage.remove();
        }

        private void before() throws MalformedURLException {
            WebDriverStorage.set(WebDriverProvider.createDriverWith(browser));
        }

    };

    protected BaseJUnit() {
        Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown));

    }

    @Parameters(name = "Crap {0}")
    public static Set<Browser> data() {
        return Framework.CONFIGURATION.AvailableDrivers();
    }
}
