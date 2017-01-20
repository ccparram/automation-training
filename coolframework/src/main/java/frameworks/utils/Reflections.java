package frameworks.utils;

import frameworks.web.BasePageObject;

import java.lang.reflect.Field;

/**
 * @author Juan Krzemien
 */
public class Reflections {

    public static boolean isPom(Field field) {
        return BasePageObject.class.isAssignableFrom(field.getType());
    }

    public static void injectFieldsPageObject(final Field field, final Object testInstance) {
        field.setAccessible(true);
        try {
            field.set(testInstance, field.getType().newInstance());
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

}
