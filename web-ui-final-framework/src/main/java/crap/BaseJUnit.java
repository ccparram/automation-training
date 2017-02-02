package crap;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.runner.ParametrizedParallelism;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.config.Framework;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Arrays.stream;

/**
 * @author Juan Krzemien
 */
@RunWith(ParametrizedParallelism.class)
public abstract class BaseJUnit extends BothCrap implements Logging {

    @Parameter
    public Browser browser;

    @Rule
    public MethodRule webDriverRule = new MethodRule() {

        private final WebDriverProvider provider = new WebDriverProvider();
        private WebDriver driver;

        @Override
        public Statement apply(Statement base, FrameworkMethod method, Object target) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    before();
                    stream(target.getClass().getDeclaredFields()).filter(field -> isPom(field)).forEach(field -> {
                        try {
                            Object pom = field.getType().getConstructor().newInstance();
                            setField(field, target, pom);
                            Class<?> baseClass = getBaseClass(field);
                            stream(baseClass.getDeclaredFields()).filter(pomField -> isDriver(pomField)).forEach(webDriverField -> setField(webDriverField, pom, driver));
                        } catch (Exception e) {
                            getLogger().error(format("Could not instantiate %s. Make sure class has a non-arg constructor.", field.getType().getName()));
                        }
                    });
                    base.evaluate();
                    after();
                }
            };
        }

        private void setField(Field field, Object target, Object value) {
            field.setAccessible(true);
            try {
                field.set(target, value);
            } catch (IllegalAccessException e) {
                getLogger().error(format("Could not inject field [%s] in [%s] with [%s].", field.getName(), target.toString(), value.toString()));
            }
        }

        private Class<?> getBaseClass(Field field) {
            Class<?> baseClass = field.getType();
            while (baseClass.getSuperclass() != Object.class) {
                baseClass = baseClass.getSuperclass();
            }
            return baseClass;
        }

        private boolean isDriver(Field pomField) {
            return WebDriver.class.isAssignableFrom(pomField.getType());
        }

        private boolean isPom(Field field) {
            return PageObject.class.isAssignableFrom(field.getType());
        }

        private void after() {
            driver.quit();
        }

        private void before() throws MalformedURLException {
            driver = provider.createDriverWith(browser);
        }

    };

    @Parameters(name = "Crap {0}")
    public static Set<Browser> data() {
        return Framework.CONFIGURATION.AvailableDrivers();
    }

}
