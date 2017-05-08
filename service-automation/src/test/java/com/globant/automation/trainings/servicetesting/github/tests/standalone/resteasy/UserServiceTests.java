package com.globant.automation.trainings.servicetesting.github.tests.standalone.resteasy;

import com.globant.automation.trainings.servicetesting.github.api.resteasy.UserApi;
import com.globant.automation.trainings.servicetesting.github.models.User;
import com.globant.automation.trainings.servicetesting.standalone.RestEasyServiceTestFor;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.InvocationCallback;
import java.io.IOException;

import static java.lang.String.format;
import static java.lang.System.err;

/**
 * @author Juan Krzemien
 */
public class UserServiceTests {

    public static class BasicAuthTests extends RestEasyServiceTestFor<UserApi> {

        @Override
        protected void configure(ClientBuilder clientBuilder) {
            clientBuilder.register(new BasicAuthentication("jkrzemien", "R3m1lg4d0"));
        }

        @Test
        public void basicAuthTest() throws Exception {
            ClientBuilder.newClient().target("http://www.anUrl.com/api/something")
                    .request()
                    .async()
                    .get(new InvocationCallback<User>() {
                        @Override
                        public void completed(User user) {
                            // Async request completed
                        }

                        @Override
                        public void failed(Throwable throwable) {
                            // Async request failed
                        }
                    });


            User user = api().getAuthenticatedUser("anUnrelatedHeaderForDemoPurposes");
            err.println(user.id);
        }

    }

    public static class TokenAuthTests extends RestEasyServiceTestFor<UserApi> {

        @Override
        protected void configure(ClientBuilder clientBuilder) {
            clientBuilder.register(new CustomAuthHeadersRequestFilter("user", "pass"));
        }

        @Test
        public void tokenAuthTest() throws Exception {
            User user = api().getAuthenticatedUser("anUnrelatedHeaderForDemoPurposes");
            getLogger().info(format("User ID: %s", user.id));
        }

        public class CustomAuthHeadersRequestFilter implements ClientRequestFilter {

            private final String authToken;

            CustomAuthHeadersRequestFilter(String username, String password) {
                // Create the required token type here...
                // This is not a real "token creating" function!
                this.authToken = "Bearer " + generateTokenFor(username, password);
            }

            private String generateTokenFor(String username, String password) {
                return format("I'm supposed to be a token generated from %s / %s", username, password);
            }

            @Override
            public void filter(ClientRequestContext requestContext) throws IOException {
                requestContext.getHeaders().add("Authorization", authToken);
            }
        }
    }
}
