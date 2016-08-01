package com.globant.automation.trainings.frameworks.webdriver.test.cucumber;

import com.globant.automation.trainings.frameworks.webdriver.test.TestContext;
import com.globant.automation.trainings.frameworks.webdriver.test.pageobject.PageObject;
import org.slf4j.Logger;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Juan Krzemien
 */
public final class Context {

    private static final Logger LOG = getLogger(PageObject.class);
    private final static String PAGE_OBJECT = "PageObject";
    public final static String BROWSER = "Browser";

    public static <T extends PageObject> void PAGE_TO_CONTEXT(T page) {
        LOG.info(format("Promoting Page Object [%s] to context...", page.getClass().getSimpleName()));
        TestContext.getCurrent().set(PAGE_OBJECT, page);
    }

    public static <T extends PageObject> T PAGE_FROM_CONTEXT() {
        T page = TestContext.getCurrent().get(PAGE_OBJECT);
        LOG.info(format("Retrieving Page Object [%s] from context...", page.getClass().getSimpleName()));
        return page;
    }
}


