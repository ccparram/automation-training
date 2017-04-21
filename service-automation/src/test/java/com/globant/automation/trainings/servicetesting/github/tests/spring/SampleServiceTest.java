package com.globant.automation.trainings.servicetesting.github.tests.spring;

import com.globant.automation.trainings.servicetesting.github.api.retrofit.ReposApi;
import com.globant.automation.trainings.servicetesting.github.models.Label;
import com.globant.automation.trainings.servicetesting.github.tests.spring.context.TestContext;
import com.globant.automation.trainings.servicetesting.github.tests.spring.repositories.LabelsRepository;
import com.globant.automation.trainings.servicetesting.spring.SpringServiceTestFor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Sample API test using Spring.
 *
 * @author Juan Krzemien
 */
@ContextConfiguration(classes = TestContext.class)
public class SampleServiceTest extends SpringServiceTestFor<ReposApi> {

    @Autowired
    private LabelsRepository pojoRepository;

    @Test
    public void attemptToCreateLabels() throws IOException {
        Response<List<Label>> response = call(api().setLabelsByUserByRepoById("aUser", "aRepo", 1, "Damn!"));
        assertEquals("HTTP status is not OK", 200, response.code());
        List<Label> labels = response.body();
        labels.forEach(label -> {
            assertNotNull("Label id should not be null", label.id);
            assertNotNull("Label name should not be null", label.name);
            assertNotNull("Label color should not be null", label.color);
            assertNotNull("Label url should not be null", label.url);
            // Just a silly example...Do not look for DB elements inside a loop!
            Label dbLabel = pojoRepository.findOne(label.id);
            assertNotNull("Label not present in DB!", dbLabel);
            assertEquals("Label id", label.id, dbLabel.id);
            assertEquals("Label name", label.name, dbLabel.name);
            assertEquals("Label color", label.color, dbLabel.color);
            assertEquals("Label url", label.url, dbLabel.url);
        });
    }

}
