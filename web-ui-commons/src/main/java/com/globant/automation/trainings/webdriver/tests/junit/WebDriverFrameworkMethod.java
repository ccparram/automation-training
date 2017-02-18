package com.globant.automation.trainings.webdriver.tests.junit;

import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.languages.Language;
import org.junit.runners.model.FrameworkMethod;

/**
 * WebDriver aware replacement for JUnit's FrameworkMethod.
 *
 * @author Juan Krzemien
 */
class WebDriverFrameworkMethod extends FrameworkMethod {

    private final Browser browser;
    private final Language language;

    /**
     * Returns a new {@code FrameworkMethod} for {@code method}
     *
     * @param method   original framework test method
     * @param browser  browser associated with this test method
     * @param language language associated with this test method
     */
    WebDriverFrameworkMethod(FrameworkMethod method, Browser browser, Language language) {
        super(method.getMethod());
        this.browser = browser;
        this.language = language;
    }

    Browser getBrowser() {
        return browser;
    }

    public Language getLanguage() {
        return language;
    }
}