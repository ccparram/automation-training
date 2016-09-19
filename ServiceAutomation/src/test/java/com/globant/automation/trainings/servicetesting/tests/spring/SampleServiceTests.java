package com.globant.automation.trainings.servicetesting.tests.spring;

import com.globant.automation.trainings.servicetesting.api.github.ReposApi;
import com.globant.automation.trainings.servicetesting.models.Label;
import com.globant.automation.trainings.servicetesting.spring.SpringServiceTestFor;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Juan Krzemien
 */
@ContextConfiguration(classes = TestContext.class)
public class SampleServiceTests extends SpringServiceTestFor<ReposApi> {

    @Test
    public void attemptToCreateLabels() throws IOException {
        Response<List<Label>> response = call(api().setLabelsByUserByRepoById("aUser", "aRepo", 1, "Damn!"));
        assertEquals("HTTP status is not OK", 200, response.code());
        List<Label> labels = response.body();
        labels.forEach(label -> {
            assertNotNull("Label name should not be null", label.name);
            assertNotNull("Label color should not be null", label.color);
            assertNotNull("Label url should not be null", label.url);
        });
    }

}
