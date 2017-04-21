package com.globant.automation.trainings.servicetesting.github.tests.standalone.resteasy;

import com.globant.automation.trainings.servicetesting.github.api.resteasy.UserApi;
import com.globant.automation.trainings.servicetesting.github.models.User;
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

import static com.globant.automation.trainings.config.CommonSettings.COMMON;

/**
 * @author Juan Krzemien
 */
public class UserServiceTest {

    @Test
    public void basicAuthTest() throws Exception {
        // Basic authentication
        ResteasyClient client = new ResteasyClientBuilder()
                .register(new BasicAuthentication("user", "pass"))
                .build();
        ResteasyWebTarget target = client.target(COMMON.environment().getBaseUrl());

        UserApi userApi = target.proxy(UserApi.class);
        User user = userApi.getAuthenticatedUser("anUnrelatedHeaderForDemoPurposes");
    }

    @Test
    public void tokenAuthTest() throws Exception {
        // Customized authentication
        ResteasyClient client = new ResteasyClientBuilder()
                .register(new CustomAuthHeadersRequestFilter("user", "pass"))
                .build();
        ResteasyWebTarget target = client.target(COMMON.environment().getBaseUrl());

        UserApi userApi = target.proxy(UserApi.class);
        User user = userApi.getAuthenticatedUser("anUnrelatedHeaderForDemoPurposes");
    }

    public class CustomAuthHeadersRequestFilter implements ClientRequestFilter {

        private final String authToken;

        public CustomAuthHeadersRequestFilter(String username, String password) {
            // Create the required token type here...this is not a real "token creating" function!
            String token = username + ":" + password;
            this.authToken = "Bearer " + Base64.encodeBase64String(token.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public void filter(ClientRequestContext requestContext) throws IOException {
            requestContext.getHeaders().add("Authorization", authToken);
        }
    }
}
