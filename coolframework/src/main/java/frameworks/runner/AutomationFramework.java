package frameworks.runner;

import frameworks.annotations.PageObject;
import frameworks.logging.Logging;
import frameworks.web.BasePageObject;
import jodd.petite.PetiteContainer;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Field;
import java.util.Set;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public class AutomationFramework extends BlockJUnit4ClassRunner implements Logging {

    private final PetiteContainer container;
    private final Reflections reflections;

    public AutomationFramework(Class<?> klass) throws InitializationError {
        super(klass);

        this.container = new PetiteContainer();
        this.reflections = new Reflections(
                new ConfigurationBuilder()
                        .addScanners(new FieldAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner())
                        .forPackages(klass.getPackage().getName())
        );

        scanAndRegisterBeans();
    }

    private void scanAndRegisterBeans() {
        Set<Class<? extends BasePageObject>> poms = reflections.getSubTypesOf(BasePageObject.class);
        poms.forEach(po -> {
            getLogger().info(format("Detected [%s]...", po.getName()));
            container.registerPetiteBean(po);
        });
        getLogger().info(format("Registered %s beans...", container.getTotalBeans()));
    }

    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        return autowireTest(test);
    }

    private Object autowireTest(Object test) {
        Set<Field> poms = reflections.getFieldsAnnotatedWith(PageObject.class);
        poms.forEach(f -> {
            try {
                f.set(test, container.getBean(f.getType()));
            } catch (IllegalAccessException e) {
                f.setAccessible(true);
                try {
                    f.set(test, container.getBean(f.getType()));
                } catch (IllegalAccessException e1) {
                    getLogger().error(e.getLocalizedMessage(), e);
                }
            }
        });
        return test;
    }
}
