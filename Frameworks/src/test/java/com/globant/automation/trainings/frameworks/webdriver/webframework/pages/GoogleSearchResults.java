package com.globant.automation.trainings.frameworks.webdriver.webframework.pages;

import com.globant.automation.trainings.frameworks.webdriver.webframework.tests.pageobject.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

/**
 * @author Juan Krzemien
 */
public class GoogleSearchResults extends PageObject {

    @FindBy(css = "h3 > a")
    private List<WebElement> results;

    public List<String> getResultsTexts() {
        waitFor(visibilityOfAllElements(results));
        return results.stream().map(WebElement::getText).collect(toList());
    }
}
