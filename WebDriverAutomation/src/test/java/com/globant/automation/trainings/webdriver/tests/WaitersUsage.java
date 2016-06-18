package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.webdriver.tests.base.HideNonRelatedStuff;
import com.globant.automation.trainings.webdriver.waiting.ComplexWaiter;
import com.globant.automation.trainings.webdriver.waiting.SimpleWaiter;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import static java.lang.System.out;

/**
 * Created by Juan Krzemien on 6/16/2016.
 */

public class WaitersUsage extends HideNonRelatedStuff {

    @Before
    public void setUp() {
        super.setUp();
        out.println("About to wait for element's visibility...");
    }

    @Test
    public void simpleWaiterUsage() {
        new SimpleWaiter().withTimeOut(5).waitUntil(anElement, WebElement::isDisplayed);
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. So element is visible.");
    }

    @Test(expected = TimeoutException.class)
    public void simpleWaiterTimesOut() {
        new SimpleWaiter().withTimeOut(1).waitUntil(anElement, WebElement::isDisplayed);
    }

    @Test
    public void complexWaiterUsage1() {
        new ComplexWaiter<WebElement>().on(anElement).is(WebElement::isDisplayed);
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. So element is visible.");
    }

    @Test
    public void complexWaiterUsage2() {
        new ComplexWaiter<>(anElement).and(anElement).and(anElement).are(WebElement::isDisplayed);
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. So element is visible.");
    }

    @Test(expected = TimeoutException.class)
    public void complexWaiterTimesOut() {
        new ComplexWaiter<WebElement>().withTimeOut(1).on(anElement).is(WebElement::isDisplayed);
    }
}
