package com.globant.automation.trainings.frameworks.webdriver.webframework.tests.pageobject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.lang.reflect.Field;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * Package local abstract class contains Selenium's WebElement initialization logic.
 * It is common to both: {@link PageElement} and {@link PageObject} classes
 * <p>
 * It also provides a mechanism to retrieve all the WebElements defined by a POM at once.
 * <p>
 *
 * @author Juan Krzemien
 */
abstract class PageCommon extends WebDriverOperations {

    PageCommon() {
        PageFactory.initElements(Drivers.INSTANCE.get(), this);
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
