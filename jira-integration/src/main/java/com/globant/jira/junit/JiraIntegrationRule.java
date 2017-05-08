package com.globant.jira.junit;

import com.globant.jira.annotations.Jira;
import com.globant.jira.annotations.Jiras;
import com.globant.jira.api.JiraApi;
import com.globant.jira.logging.Logging;
import com.globant.jira.models.Evidence;
import com.globant.jira.models.ExecutionResults;
import com.globant.jira.models.Test;
import org.junit.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import javax.ws.rs.core.Response;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.globant.jira.config.JiraSettings.CONFIGURATION;
import static java.time.ZonedDateTime.now;
import static java.time.ZonedDateTime.parse;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

public class JiraIntegrationRule extends TestWatcher implements Logging {

    private static final ThreadLocal<Test> CURRENT_TEST = ThreadLocal.withInitial(Test::new);
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
    private static final String AUTOMATED_TEST_RUN = "Automated test run";

    private static final Map<String, ExecutionResults> executionResults = new HashMap<>();
    private final String startTime;

    public JiraIntegrationRule() {
        startTime = currentZonedLocalDateTime();
    }

    private List<Jira> getJiras(Description description) {
        List<Jira> results = new ArrayList<>();
        ofNullable(description.getAnnotation(Jiras.class)).ifPresent(j -> results.addAll(asList(j.value())));
        ofNullable(description.getAnnotation(Jira.class)).ifPresent(results::add);
        return results;
    }

    private void applyJiraInfoToTestWithState(Description description) {
        getJiras(description).forEach(jira -> {
            Test t = CURRENT_TEST.get();
            t.testKey = jira.id();
            t.comment = jira.name();
            String version = jira.version().isEmpty() ? CONFIGURATION.Jira().getAppVersion() : jira.version();
            ExecutionResults executionResult = executionResults.computeIfAbsent(version, k -> new ExecutionResults());
            executionResult.info.version = version;
            executionResult.info.summary = AUTOMATED_TEST_RUN;
            executionResult.info.description = AUTOMATED_TEST_RUN;
            executionResult.info.startDate = startTime;
            executionResult.info.user = CONFIGURATION.Jira().getUsername();
            executionResult.tests.add(Test.from(t));
        });
        executionResults.keySet().forEach(key -> {
            ExecutionResults executionResult = executionResults.get(key);
            AtomicReference<ZonedDateTime> accumulated = new AtomicReference<>(parse(startTime, FORMAT));
            executionResult.tests.forEach(test -> {
                ZonedDateTime f = parse(test.finish, FORMAT);
                ZonedDateTime s = parse(test.start, FORMAT);
                Duration delta = Duration.between(s, f);
                accumulated.set(accumulated.get().plus(delta));
            });
            executionResult.info.finishDate = accumulated.get().format(FORMAT);
        });
    }

    private Evidence getHtmlEvidence(Throwable throwable) {
        Evidence evidence = new Evidence();
        evidence.contentType = "text/html";
        evidence.data = throwable.getLocalizedMessage();
        return evidence;
    }

    private String currentZonedLocalDateTime() {
        return now().format(FORMAT);
    }

    @Override
    protected void finished(Description description) {
        applyJiraInfoToTestWithState(description);

        for (ExecutionResults executionResult : executionResults.values()) {
            Response response = JiraApi.get().importResults(executionResult);
            getLogger().debug("JIRA response code: " + response.getStatusInfo().getStatusCode());
            getLogger().debug("JIRA response message: " + response.getStatusInfo().getReasonPhrase());
        }
    }

    @Override
    protected void failed(Throwable e, Description description) {
        Test t = CURRENT_TEST.get();
        t.status = "FAIL";
        t.evidences.add(getHtmlEvidence(e));
        t.finish = currentZonedLocalDateTime();
        CURRENT_TEST.set(t);
    }

    @Override
    protected void succeeded(Description description) {
        Test t = CURRENT_TEST.get();
        t.status = "PASS";
        t.finish = currentZonedLocalDateTime();
        CURRENT_TEST.set(t);
    }

    @Override
    protected void starting(Description description) {
        Test t = CURRENT_TEST.get();
        t.start = currentZonedLocalDateTime();
        CURRENT_TEST.set(t);
    }

    @Override
    protected void skipped(AssumptionViolatedException e, Description description) {
        Test t = CURRENT_TEST.get();
        t.status = "SKIP";
        t.evidences.add(getHtmlEvidence(e));
        t.finish = currentZonedLocalDateTime();
        CURRENT_TEST.set(t);
    }

}
