package com.globant.automation.trainings.webdriver.tests.junit;

import com.globant.automation.trainings.languages.Language;
import com.globant.automation.trainings.tests.junit.ApiFrameworkMethod;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import org.junit.runners.model.FrameworkMethod;

/**
 * WebDriver aware replacement for JUnit's FrameworkMethod.
 *
 * @author Juan Krzemien
 */
class WebDriverFrameworkMethod extends ApiFrameworkMethod {

    private final Browser browser;

    /**
     * Returns a new {@code FrameworkMethod} for {@code method}
     *
     * @param method   original framework test method
     * @param browser  browser associated with this test method
     * @param language language associated with this test method
     */
    WebDriverFrameworkMethod(FrameworkMethod method, Browser browser, Language language) {
        super(method, language);
        this.browser = browser;
    }

    Browser getBrowser() {
        return browser;
    }

}
