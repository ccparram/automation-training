package com.globant.automation.trainings.frameworks.webdriver.test.cucumber.definitions;


import com.globant.automation.trainings.frameworks.webdriver.factories.Drivers;
import com.globant.automation.trainings.frameworks.webdriver.test.TestContext;
import com.globant.automation.trainings.frameworks.webdriver.test.cucumber.MethodHandlersDispatcher;
import com.globant.automation.trainings.frameworks.webdriver.utils.Generator;
import cucumber.api.java.en.And;

import static com.globant.automation.trainings.frameworks.webdriver.test.cucumber.MethodHandlersDispatcher.ElementType.*;
import static java.lang.String.format;
import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;
import static org.testng.Assert.assertEquals;

/**
 * Created by Juan Krzemien on 7/15/2016.
 */
public class CommonSteps {

    private final MethodHandlersDispatcher dispatcher = new MethodHandlersDispatcher();

    @And("^User clicks the \"([^\"]*)\" checkbox$")
    public void userClicksTheCheckbox(String checkboxName) throws Throwable {
        dispatcher.triggerMethodHandler(checkboxName, CHECKBOX);
    }

    @And("^User clicks the \"([^\"]*)\" link$")
    public void userClicksTheLink(String linkName) throws Throwable {
        dispatcher.triggerMethodHandler(linkName, LINK);
    }

    @And("^User clicks the \"([^\"]*)\" button$")
    public void userClicksTheButton(String buttonName) throws Throwable {
        dispatcher.triggerMethodHandler(buttonName, BUTTON);
    }

    @And("^User clicks \"([^\"]*)\" tab in nav bar$")
    public void userClicksTabInNavBar(String tab) throws Throwable {
        dispatcher.triggerMethodHandler(tab, ANY);
    }

    @And("^User enters \"([^\"]*)\" in \"([^\"]*)\" field$")
    public void userEntersInField(String text, String fieldName) throws Throwable {
        dispatcher.triggerMethodHandler(fieldName, TEXT, text);
        // Make available for possible later usage in other steps...
        TestContext.getCurrent().set(fieldName, text);
    }

    @And("^User enters (\\d+?) random alphanumeric chars in \"([^\"]*)\" field$")
    public void userEntersARandomAlphanumericStringInField(int number, String fieldName) throws Throwable {
        String randomString = Generator.randomAlphanumericString(number);
        dispatcher.triggerMethodHandler(fieldName, TEXT, randomString);
        // Make available for possible later usage in other steps...
        TestContext.getCurrent().set(fieldName, randomString);
    }

    @And("^User selects \"([^\"]*)\" option in \"([^\"]*)\" dropdown")
    public void userSelectsInField(String option, String fieldName) throws Throwable {
        dispatcher.triggerMethodHandler(fieldName, SELECT, option);
        // Make available for possible later usage in other steps...
        TestContext.getCurrent().set(fieldName, option);
    }

    @And("^User agrees to confirmation dialog$")
    public void userAgreesToConfirmationDialog() throws Throwable {
        dispatcher.doWait(alertIsPresent()).accept();
    }

    @And("^User enters value for \"([^\"]*)\" in \"([^\"]*)\" field$")
    public void userEntersKnownValueInField(String oldFieldName, String newFieldName) throws Throwable {
        // Retrieve value used in other steps...
        String autoGenerateValue = TestContext.getCurrent().get(oldFieldName);
        // Set in field
        dispatcher.triggerMethodHandler(newFieldName, TEXT, autoGenerateValue);
        // Make available for possible later usage in other steps...
        TestContext.getCurrent().set(newFieldName, autoGenerateValue);
    }

    @And("^\"([^\"]*)\" field should be empty$")
    public void fieldIsNotEmpty(String fieldName) throws Throwable {
        String result = dispatcher.triggerMethodGetter(fieldName, ANY);
        assertEquals(result, "", fieldName + " is not empty");
    }

    @And("^User switches to \"([^\"]*)\" iframe$")
    public void userSwitchesToIframe(String iFrameField) throws Throwable {
        dispatcher.triggerMethodHandler(iFrameField, IFRAME);
    }

    @And("^User switches back to default context$")
    public void userSwitchesBackToDefaultContext() throws Throwable {
        Drivers.INSTANCES.get().switchTo().defaultContent();
    }

    @And("^\"([^\"]*)\" field is the same as previously entered$")
    public void fieldsValueIsTheSameAsPreviouslyEntered(String fieldName) throws Throwable {
        String result = dispatcher.triggerMethodGetter(fieldName, ANY);
        assertEquals(result, TestContext.getCurrent().get(fieldName), fieldName + " mismatch");
    }

    @And("^\"([^\"]*)\" field's value is \"([^\"]*)\"$")
    public void fieldsValueIs(String fieldName, String value) throws Throwable {
        String result = dispatcher.triggerMethodGetter(fieldName, ANY);
        assertEquals(result, value, format("[%s] does not match [%s]", fieldName, value));
    }

    @And("^\"([^\"]*)\" is the same as previously entered in \"([^\"]*)\" field$")
    public void isTheSameAsPreviouslyEnteredInField(String fieldName, String previousFieldName) throws Throwable {
        // Retrieve value used in other steps...
        String previousFieldValue = TestContext.getCurrent().get(previousFieldName);
        // Get current field value
        String result = dispatcher.triggerMethodGetter(fieldName, ANY);
        // Compare
        assertEquals(result, previousFieldValue, format("[%s] does not match [%s]", fieldName, previousFieldValue));
    }
}
