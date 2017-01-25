package frameworks.web;

import frameworks.runner.ParametrizedParallelism;

public class ParametrizedWebDriverRunner extends ParametrizedParallelism {

    public ParametrizedWebDriverRunner(Class<?> clazz) throws Throwable {
        super(clazz);
        Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown));
    }

}
