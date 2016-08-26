package com.globant.automation.trainings.frameworks.webdriver.tests;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import java.lang.reflect.Field;
import java.util.List;

import static com.globant.automation.trainings.frameworks.webdriver.config.Framework.CONFIGURATION;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

abstract class PageCommon extends WebDriverOperations {

    PageCommon() {
        AjaxElementLocatorFactory factory = new AjaxElementLocatorFactory(driver, CONFIGURATION.WebDriver().getExplicitTimeOut());
        PageFactory.initElements(factory, this);
    }

    protected List<WebElement> getOwnWebElements() {
        Field[] fields = getClass().getDeclaredFields();
        return stream(fields).filter(f -> f.getType().isAssignableFrom(WebElement.class)).map(f -> {
            try {
                f.setAccessible(true);
                WebElement we = (WebElement) f.get(this);
                f.setAccessible(false);
                return we;
            } catch (IllegalAccessException e) {
                LOG.error("Could not retrieve WebElements from Page Object!", e);
                return null;
            }
        }).collect(toList());
    }
}
