package com.globant.automation.trainings.servicetesting.tests;

import com.globant.automation.trainings.servicetesting.Credentials;
import com.globant.automation.trainings.servicetesting.ServiceTestForTokenSecured;
import com.globant.automation.trainings.servicetesting.tests.api.AnAPI;
import com.globant.automation.trainings.servicetesting.tests.api.Authorization;
import org.junit.Test;

/**
 * @author Juan Krzemien
 */
public class SomeServiceTests extends ServiceTestForTokenSecured<Authorization, AnAPI> {

    @Override
    protected Credentials getCredentials() {
        return new Credentials("user", "pass");
    }

    @Test
    public void sampleTest() {
        get
    }


}
