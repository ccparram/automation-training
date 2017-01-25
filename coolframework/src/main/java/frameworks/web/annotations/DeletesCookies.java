package frameworks.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks POM classes to automatically delete cookies after instantiation of POM.
 * <p>
 * <b>NOTE</b> <i>Some driver implementations (such as IE) does not support deletion of some types of cookies. Example: httpOnly cookies</i>
 *
 * @author Juan Krzemien
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DeletesCookies {
}
