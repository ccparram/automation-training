package frameworks.runner;

import frameworks.Container;
import frameworks.logging.Logging;
import frameworks.web.SeleniumServerStandAlone;

public class AutomationFramework extends Parallelism implements Logging {

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown));
    }

    public AutomationFramework(Class<?> clazz) throws Throwable {
        super(clazz);
        Runtime.getRuntime().addShutdownHook(new Thread(Container.CONTAINER.with(clazz)::destroy));
    }

}
