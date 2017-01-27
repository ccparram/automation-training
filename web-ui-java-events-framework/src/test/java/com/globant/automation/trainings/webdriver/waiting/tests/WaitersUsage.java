package com.globant.automation.trainings.webdriver.waiting.tests;

import com.globant.automation.trainings.webdriver.waiting.ComplexWaiter;
import com.globant.automation.trainings.webdriver.waiting.SimpleWaiter;
import com.globant.automation.trainings.webdriver.waiting.conditions.Conditions;
import com.globant.automation.trainings.webdriver.waiting.tests.base.HideNonRelatedStuff;
import org.junit.Test;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.globant.automation.trainings.webdriver.waiting.conditions.Conditions.Browser.ContainsInUrl;
import static com.globant.automation.trainings.webdriver.waiting.conditions.Conditions.Element.Visibility.Visible;
import static java.lang.System.out;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Juan Krzemien
 */

public class WaitersUsage extends HideNonRelatedStuff {

    @Test
    public void simpleWaiterUsage() {
        new SimpleWaiter<WebElement>().withTimeOut(5).until(anElement, Visible);
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. So element is visible.");
    }

    @Test
    public void simpleWaiterUsageOnManyElements() {
        new SimpleWaiter<List<WebElement>>().withTimeOut(5).until(manyElements, Conditions.Elements.Visibility.Visible);
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. So element is visible.");
    }

    @Test(expected = TimeoutException.class)
    public void simpleWaiterTimesOut() {
        new SimpleWaiter<WebElement>().withTimeOut(1).until(anElement, Visible);
    }

    @Test
    public void simpleWaiterWithoutFailing() {
        new SimpleWaiter<WebElement>().withTimeOut(1).withoutFailing().until(anElement, Visible);
    }

    @Test
    public void complexWaiterUsage1() {
        new ComplexWaiter<>(anElement).is(Visible);
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
        new ComplexWaiter<>(anElement).withTimeOut(1).is(Visible);
    }

    @Test
    public void simpleWaiterUsageTriFunctionCondition() {
        new SimpleWaiter<WebDriver>().withTimeOut(5).until(aDriver, ContainsInUrl, "about:blank");
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. Url matched.");
    }

    @Test
    public void simpleWaiterUsageTriFunctionCondition2() {
        WebElement element = new SimpleWaiter<WebDriver>().withTimeOut(5).until(aDriver, Conditions.Locator.Exists, anId);
        assertNotNull(element);
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. Element was found.");
    }

    @Test
    public void complexWaiterUsageTriFunctionCondition() {
        List<WebElement> elements = new ComplexWaiter<>(aDriver).withTimeOut(5).is(Conditions.Locator.Exists, anId);
        assertNotNull(elements);
        assertEquals("There should be one element in here!", 1, elements.size());
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown. Element was found.");
    }

    @Test(expected = TimeoutException.class)
    public void complexWaiterUsageTriFunctionConditionTimesOut() {
        List<WebElement> elements = new ComplexWaiter<>(aDriver).withTimeOut(1).is(Conditions.Locator.Exists, anXPath);
    }

    @Test
    public void complexWaiterWithoutFailing() {
        List<WebElement> elements = new ComplexWaiter<>(aDriver).withTimeOut(1).withoutFailing().is(Conditions.Locator.Exists, anXPath);
        assertNotNull(elements);
        assertEquals("There should be one element in here!", 0, elements.size());
        out.println("Done waiting! Since you can see this, a TimeoutException was not thrown due to withoutFailing().");
    }
}
