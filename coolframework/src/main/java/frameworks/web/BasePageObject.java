package frameworks.web;

import jodd.petite.meta.PetiteInject;
import org.openqa.selenium.WebDriver;

/**
 * @author Juan Krzemien
 */
public abstract class BasePageObject {

    @PetiteInject
    private WebDriver webDriver;


}
