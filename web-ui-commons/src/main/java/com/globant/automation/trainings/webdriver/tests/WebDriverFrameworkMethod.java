package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.webdriver.browsers.Browser;
import org.junit.runners.model.FrameworkMethod;

/**
 * WebDriver aware replacement for JUnit's FrameworkMethod.
 *
 * @author Juan Krzemien
 */
class WebDriverFrameworkMethod extends FrameworkMethod {

    private final Browser browser;

    /**
     * Returns a new {@code FrameworkMethod} for {@code method}
     *
     * @param method  original framework test method
     * @param browser browser associated with this test method
     */
    WebDriverFrameworkMethod(FrameworkMethod method, Browser browser) {
        super(method.getMethod());
        this.browser = browser;
    }

    Browser getBrowser() {
        return browser;
    }
}