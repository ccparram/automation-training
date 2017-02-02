package crap;


import com.globant.automation.trainings.logging.Logging;
import org.openqa.selenium.WebDriver;

/**
 * @author Juan Krzemien
 */
public abstract class PageObject implements Logging {

    private final WebDriver driver = WebDriverStorage.get();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName()).append(" - ");
        if (driver == null) {
            sb.append("NO DRIVER");
        } else {
            sb.append(driver.toString());
        }
        return sb.toString();
    }

}
