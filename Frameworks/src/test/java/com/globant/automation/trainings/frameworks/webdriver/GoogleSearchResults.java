package com.globant.automation.trainings.frameworks.webdriver;

import com.globant.automation.trainings.frameworks.webdriver.tests.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

/**
 * Created by jkrzemien on 06/07/2016.
 */
public class GoogleSearchResults extends PageObject{

    @FindBy(css = "h3 > a")
    private List<WebElement> results;

    public List<String> getResultsTexts() {
        wait.until(visibilityOfAllElements(results));
        return results.stream().map(WebElement::getText).collect(toList());
    }
}
