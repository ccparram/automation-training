package frameworks.web;

import com.globant.automation.trainings.runner.ParametrizedParallelism;
import com.globant.automation.trainings.webdriver.server.SeleniumServerStandAlone;

public class ParametrizedWebDriverRunner extends ParametrizedParallelism {

    public ParametrizedWebDriverRunner(Class<?> clazz) throws Throwable {
        super(clazz);
        Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown));
    }

}
