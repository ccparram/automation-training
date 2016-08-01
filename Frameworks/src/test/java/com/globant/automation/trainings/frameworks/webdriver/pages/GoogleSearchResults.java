package com.globant.automation.trainings.frameworks.webdriver.pages;

import com.globant.automation.trainings.frameworks.webdriver.test.pageobject.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

/**
 * Sample Page Object for Google's Results Page
 *
 * @author Juan Krzemien
 */
public class GoogleSearchResults extends PageObject {

    @FindBy(css = "h3 > a")
    private List<WebElement> results;

    public List<String> getResultsTexts() {
        return results.stream().map(WebElement::getText).collect(toList());
    }

    @Override
    public boolean isVisible() {
        doWait(visibilityOfAllElements(results));
        return true;
    }
}
