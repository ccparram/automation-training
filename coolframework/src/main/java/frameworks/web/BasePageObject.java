package frameworks.web;

import org.openqa.selenium.WebDriver;
import org.picocontainer.annotations.Inject;

/**
 * @author Juan Krzemien
 */
public abstract class BasePageObject {

    @Inject
    private WebDriver webDriver;

    public void dispose() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
