package frameworks.annotations;

import java.lang.annotation.*;

/**
 * @author Juan Krzemien
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface PageObject {
}
