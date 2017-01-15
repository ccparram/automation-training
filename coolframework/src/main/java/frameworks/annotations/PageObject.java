package frameworks.annotations;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author Juan Krzemien
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Lazy
@Component
public @interface PageObject {
}
