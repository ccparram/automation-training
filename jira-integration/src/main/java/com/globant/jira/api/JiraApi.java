package com.globant.jira.api;

import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import java.io.IOException;

import static com.globant.jira.config.JiraSettings.CONFIGURATION;

/**
 * @author Juan Krzemien
 */
public class JiraApi {

    private JiraApi() {
    }

    public static Jira get() throws IOException {
        String baseUrl = CONFIGURATION.Jira().getBaseUrl();
        String username = CONFIGURATION.Jira().getUsername();
        String password = CONFIGURATION.Jira().getPassword();
        ResteasyClient client = createHttpClient(username, password);
        return client.target(baseUrl).proxy(Jira.class);
    }

    private static ResteasyClient createHttpClient(String username, String password) {
        ResteasyClientBuilder client = new ResteasyClientBuilder()
                .register(new BasicAuthentication(username, password));
        if (CONFIGURATION.Jira().isProxyEnabled()) {
            client.defaultProxy(CONFIGURATION.Jira().getProxyHost(), CONFIGURATION.Jira().getProxyPort());
        }
        return client.build();
    }

}
