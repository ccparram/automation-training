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
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Optional.ofNullable;

/**
 * @author Juan Krzemien
 */
public enum Reporter implements Logging {

    REPORTER;

    private static final String REPORTS_DIRECTORY = "reports";
    private static final String SCREENSHOTS_DIRECTORY = "screenshots";
    private static final ThreadLocal<ExtentTest> REPORT_PER_THREAD = new InheritableThreadLocal<>();
    private final ExtentReports REPORT;

    Reporter() {
        try {
            Files.createDirectories(Paths.get(REPORTS_DIRECTORY));
            Files.createDirectories(Paths.get(SCREENSHOTS_DIRECTORY));
        } catch (IOException e) {
            getLogger().error("Could not create directory!", e);
        }

        String dateTime = now().format(ofPattern("ddMMyyyy-hhmmss"));
        String filePath = format("%s/report-%s.html", REPORTS_DIRECTORY, dateTime);

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
        get().ifPresent(extentTest -> extentTest.log(PASS, format(text, args)));
    }

    public void warn(String text, Object... args) {
        get().ifPresent(extentTest -> extentTest.log(WARNING, format(text, args)));
    }

    public void skip(String text, Object... args) {
        get().ifPresent(extentTest -> extentTest.log(SKIP, format(text, args)));
    }

    public void fail(String text, Object... args) {
        get().ifPresent(extentTest -> extentTest.log(FAIL, format(text, args)));
    }

    public void error(String text, Object... args) {
        get().ifPresent(extentTest -> extentTest.log(ERROR, format(text, args)));
    }

    public void info(String text, Object... args) {
        get().ifPresent(extentTest -> extentTest.log(INFO, format(text, args)));
    }

    public void fatal(String text, Object... args) {
        get().ifPresent(extentTest -> extentTest.log(FATAL, format(text, args)));
    }

    public void unknown(String text, Object... args) {
        get().ifPresent(extentTest -> extentTest.log(UNKNOWN, format(text, args)));
    }

    public void addBase64ScreenShot(String base64ScreenShot) {
        get().ifPresent(extentTest -> extentTest.log(INFO, extentTest.addBase64ScreenShot("data:image/png;base64," + base64ScreenShot)));
    }

    public void addScreenShot(String screenShot) {
        get().ifPresent(extentTest -> extentTest.log(INFO, extentTest.addScreenCapture(screenShot)));
    }
}

