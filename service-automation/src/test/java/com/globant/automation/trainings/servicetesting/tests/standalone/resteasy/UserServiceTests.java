package com.globant.automation.trainings.servicetesting.tests.standalone.resteasy;

import com.globant.automation.trainings.servicetesting.api.github.resteasy.UserApi;
import com.globant.automation.trainings.servicetesting.models.User;
import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Juan Krzemien
 */
public class UserServiceTests {

    public static final String API_GITHUB = "https://api.github.com/";

    @Test
    public void basicAuthTest() throws IOException {
        // Basic authentication
        ResteasyClient client = new ResteasyClientBuilder()
                .register(new BasicAuthentication("user", "pass"))
                .build();
        ResteasyWebTarget target = client.target(API_GITHUB);

        UserApi userApi = target.proxy(UserApi.class);
        User user = userApi.getAuthenticatedUser("21321491028402184120948098024");
    }

    @Test
    public void tokenAuthTest() throws IOException {
        // Basic authentication
        ResteasyClient client = new ResteasyClientBuilder()
                .register(new CustomAuthHeadersRequestFilter("user", "pass"))
                .build();
        ResteasyWebTarget target = client.target(API_GITHUB);

        UserApi userApi = target.proxy(UserApi.class);
        User user = userApi.getAuthenticatedUser("ASKFJDSLKFJSDLKDSJLFKSJDFLKDSJKLDS");
    }

    public class CustomAuthHeadersRequestFilter implements ClientRequestFilter {

        private final String authToken;

        public CustomAuthHeadersRequestFilter(String username, String password) {
            // Create the required token type here...
            String token = username + ":" + password;
            this.authToken = "Basic " + Base64.encodeBase64String(token.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public void filter(ClientRequestContext requestContext) throws IOException {
            requestContext.getHeaders().add("Authorization", authToken);
        }
    }
}
