package com.globant.automation.trainings.logging;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import static com.relevantcodes.extentreports.LogStatus.*;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * @author Juan Krzemien
 */
public enum Reporter implements Logging {

    REPORTER;

    private static final String REPORTS_DIRECTORY = "reports";
    private static final ThreadLocal<ExtentTest> REPORT_PER_THREAD = new InheritableThreadLocal<>();
    private final ExtentReports REPORT;

    Reporter() {
        try {
            Files.createDirectories(Paths.get(REPORTS_DIRECTORY));
            Files.createDirectories(Paths.get(format("%s/screenshots", REPORTS_DIRECTORY)));
        } catch (IOException e) {
            getLogger().error("Could not create directory!", e);
        }

        String filePath = format("%s/report-latest.html", REPORTS_DIRECTORY);

        REPORT = new ExtentReports(filePath, NetworkMode.OFFLINE);

        // Dump report when JVM stops
        Runtime.getRuntime().addShutdownHook(new Thread(REPORT::flush));
    }

    private Optional<ExtentTest> get() {
        return ofNullable(REPORT_PER_THREAD.get());
    }

    public void startTest(String testName, String description) {
        ExtentTest currentTest = REPORT.startTest(testName, description);
        currentTest.setStartedTime(new Date());
        REPORT_PER_THREAD.set(currentTest);
    }

    public void endTest() {
        get().ifPresent(REPORT::endTest);
    }

    public void pass(String text, Object... args) {
        String message = escapeHtml4(args.length > 0 ? format(text, args) : text);
        getLogger().info(message);
        get().ifPresent(extentTest -> extentTest.log(PASS, message));
    }

    public void warn(String text, Object... args) {
        String message = escapeHtml4(args.length > 0 ? format(text, args) : text);
        getLogger().warn(message);
        get().ifPresent(extentTest -> extentTest.log(WARNING, message));
    }

    public void skip(String text, Object... args) {
        String message = escapeHtml4(args.length > 0 ? format(text, args) : text);
        getLogger().warn(message);
        get().ifPresent(extentTest -> extentTest.log(SKIP, message));
    }

    public void fail(String text, Object... args) {
        String message = escapeHtml4(args.length > 0 ? format(text, args) : text);
        getLogger().error(message);
        get().ifPresent(extentTest -> extentTest.log(FAIL, message));
    }

    public void error(String text, Object... args) {
        String message = escapeHtml4(args.length > 0 ? format(text, args) : text);
        getLogger().error(message);
        get().ifPresent(extentTest -> extentTest.log(ERROR, message));
    }

    public void info(String text, Object... args) {
        String message = escapeHtml4(args.length > 0 ? format(text, args) : text);
        getLogger().info(message);
        get().ifPresent(extentTest -> extentTest.log(INFO, message));
    }

    public void fatal(String text, Object... args) {
        String message = escapeHtml4(args.length > 0 ? format(text, args) : text);
        getLogger().error(message);
        get().ifPresent(extentTest -> extentTest.log(FATAL, message));
    }

    public void unknown(String text, Object... args) {
        String message = escapeHtml4(args.length > 0 ? format(text, args) : text);
        getLogger().error(message);
        get().ifPresent(extentTest -> extentTest.log(UNKNOWN, message));
    }

    public void debug(String text, Object... args) {
        String message = args.length > 0 ? format(text, args) : text;
        getLogger().debug(message);
    }

    public void addBase64ScreenShot(String base64ScreenShot) {
        get().ifPresent(extentTest -> extentTest.log(INFO, extentTest.addBase64ScreenShot("data:image/png;base64," + base64ScreenShot)));
    }

    public void addScreenShot(String screenShot) {
        get().ifPresent(extentTest -> extentTest.log(INFO, extentTest.addScreenCapture(screenShot)));
    }
}

