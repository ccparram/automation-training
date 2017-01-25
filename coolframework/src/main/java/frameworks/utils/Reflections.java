package frameworks.utils;

import frameworks.web.BasePageObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author Juan Krzemien
 */
public class Reflections {

    private static final Logger LOG = LoggerFactory.getLogger(Reflections.class);

    public static boolean isPom(Field field) {
        return BasePageObject.class.isAssignableFrom(field.getType());
    }

    public static void injectFieldsPageObject(final Field field, final Object testInstance) {
        field.setAccessible(true);
        try {
            field.set(testInstance, field.getType().newInstance());
        } catch (IllegalAccessException | InstantiationException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

}
