package crap;

import com.globant.automation.trainings.webdriver.server.SeleniumServerStandAlone;

/**
 * @author Juan Krzemien
 */
public class BothCrap {
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown));
    }
}
