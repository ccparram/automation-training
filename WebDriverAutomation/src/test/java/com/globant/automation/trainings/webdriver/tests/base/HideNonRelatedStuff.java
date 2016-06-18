package com.globant.automation.trainings.webdriver.tests.base;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;

import static java.lang.System.out;
import static org.mockito.Mockito.when;

/**
 * Created by Juan Krzemien on 6/16/2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class HideNonRelatedStuff {

    @Mock
    protected WebElement anElement;

    private volatile boolean displayed;

    @Before
    public void setUp() {
        launchVisibilitySwitcherThread();
        when(anElement.isDisplayed()).thenAnswer(i -> {
            out.println("isDisplayed(): " + displayed);
            return displayed;
        });
    }

    private void launchVisibilitySwitcherThread() {
        new Thread(() -> {
            try {
                displayed = false;
                // Simulate delay in element rendering...
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // Ignored
            }
            displayed = true;
        }).start();
    }
}
