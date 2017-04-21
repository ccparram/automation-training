package com.globant.automation.trainings.runner.junit;

import com.globant.automation.trainings.languages.Language;
import org.junit.runners.model.FrameworkMethod;

/**
 * WebDriver aware replacement for JUnit's FrameworkMethod.
 *
 * @author Juan Krzemien
 */
public class ApiFrameworkMethod extends FrameworkMethod {

    private final Language language;

    /**
     * Returns a new {@code FrameworkMethod} for {@code method}
     *
     * @param method   original framework test method
     * @param language language associated with this test method
     */
    public ApiFrameworkMethod(FrameworkMethod method, Language language) {
        super(method.getMethod());
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }
}
