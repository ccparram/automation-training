package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.webdriver.tests.base.HideNonRelatedStuff;
import com.globant.automation.trainings.webdriver.waiting.ComplexWaiter;
import com.globant.automation.trainings.webdriver.waiting.SimpleWaiter;
import com.globant.automation.trainings.webdriver.waiting.conditions.Conditions;
import org.junit.Test;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.globant.automation.trainings.webdriver.waiting.conditions.Conditions.Browser.ContainsInUrl;
import static com.globant.automation.trainings.webdriver.waiting.conditions.Conditions.Element.Visibility.Visible;
import static java.lang.System.out;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Juan Krzemien on 6/16/2016.
 */

public class WaitersUsage extends HideNonRelatedStuff {

    @Test
    public void simpleWaiterUsage() {
        new SimpleWaiter().withTimeOut(5).waitUntil(anElement, Visible);
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. So element is visible.");
    }

    @Test
    public void simpleWaiterUsageOnManyElements() {
        new SimpleWaiter().withTimeOut(5).waitUntil(manyElements, Conditions.Elements.Visibility.Visible);
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. So element is visible.");
    }

    @Test(expected = TimeoutException.class)
    public void simpleWaiterTimesOut() {
        new SimpleWaiter().withTimeOut(1).waitUntil(anElement, Visible);
    }

    @Test
    public void complexWaiterUsage1() {
        new ComplexWaiter<WebElement>().on(anElement).is(Visible);
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. So element is visible.");
    }

    @Test
    public void complexWaiterUsage2() {
        new ComplexWaiter<>(anElement).and(anotherElement).are(Visible);
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. So element is visible.");
    }

    @Test
    public void complexWaiterUsageOnManyElements() {
        new ComplexWaiter<>(manyElements).are(Conditions.Elements.Visibility.Visible);
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. So element is visible.");
    }

    @Test(expected = TimeoutException.class)
    public void complexWaiterTimesOut() {
        new ComplexWaiter<WebElement>().withTimeOut(1).on(anElement).is(Visible);
    }

    @Test
    public void simpleWaiterUsageTriFunctionCondition() {
        new SimpleWaiter().withTimeOut(5).waitUntil(aDriver, ContainsInUrl, "about:blank");
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. Url matched.");
    }

    @Test
    public void simpleWaiterUsageTriFunctionCondition2() {
        WebElement element = new SimpleWaiter().withTimeOut(5).waitUntil(aDriver, Conditions.Locator.Exists, anId);
        assertNotNull(element);
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. Element was found.");
    }

    @Test
    public void complexWaiterUsageTriFunctionCondition() {
        WebElement element = new ComplexWaiter<WebDriver>().withTimeOut(5).on(aDriver).until(Conditions.Locator.Exists, anId);
        assertNotNull(element);
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. Element was found.");
    }

    @Test(expected = TimeoutException.class)
    public void complexWaiterUsageTriFunctionConditionTimesOut() {
        WebElement element = new ComplexWaiter<WebDriver>().withTimeOut(1).on(aDriver).until(Conditions.Locator.Exists, anXPath);
    }
}
