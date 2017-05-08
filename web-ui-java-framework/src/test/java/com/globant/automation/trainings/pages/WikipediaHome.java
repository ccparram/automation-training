package com.globant.automation.trainings.pages;

import com.globant.automation.trainings.languages.Language;
import com.globant.automation.trainings.webdriver.annotations.Url;
import com.globant.automation.trainings.webdriver.tests.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Juan Krzemien
 */
@Url("/")
public class WikipediaHome extends PageObject<WikipediaHome> {

    @FindBy(id = "searchInput")
    private WebElement searchCriteria;

    @FindBy(id = "searchLanguage")
    private WebElement searchLanguage;

    @FindBy(css = "#search-form > fieldset > button")
    private WebElement searchButton;

    public boolean isVisible() {
        return isVisible(searchButton);
    }

    public WikipediaHome withLanguage(Language language) {
        select(searchLanguage, language.toLocale().getLanguage(), false);
        return this;
    }

    public WikipediaArticle doSearch(String criteria) {
        type(searchCriteria, criteria);
        click(searchButton);
        return new WikipediaArticle();
    }
}
