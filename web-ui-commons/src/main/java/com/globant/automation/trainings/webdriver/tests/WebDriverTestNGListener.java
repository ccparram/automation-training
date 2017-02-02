package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.config.Framework;
import com.globant.automation.trainings.webdriver.server.SeleniumServerStandAlone;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import org.testng.internal.ConstructorOrMethod;
import org.testng.internal.MethodInstance;
import org.testng.xml.XmlTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import static com.globant.automation.trainings.utils.Reflection.fieldIsSubClassOf;
import static com.globant.automation.trainings.utils.Reflection.injectFieldOwnInstance;
import static com.globant.automation.trainings.webdriver.tests.WebDriverContext.WEB_DRIVER_CONTEXT;
import static java.util.Arrays.stream;

/**
 * TestNG listener to multiply number of tests by browsers and to inject POM into test suites automatically.
 *
 * @author Juan Krzemien
 */
public class WebDriverTestNGListener implements IMethodInterceptor, IInvokedMethodListener, Logging {

    public WebDriverTestNGListener() {
        Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown));
    }

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> list, ITestContext iTestContext) {
        final Set<Browser> browsers = Framework.CONFIGURATION.AvailableDrivers();
        final List<IMethodInstance> expandedMethods = new ArrayList<>(list.size() * browsers.size());
        list.forEach(m -> browsers.forEach(b -> expandedMethods.add(new WebDriverMethodInstance(m.getMethod(), b))));
        return expandedMethods;
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        try {
            WebDriverTestNGMethodDelegate delegate = ((WebDriverTestNGMethodDelegate) method.getTestMethod());
            Object testInstance = method.getTestMethod().getInstance();
            createDriverFrom(delegate);
            injectPomsInto(testInstance);
        } catch (MalformedURLException e) {
            getLogger().error(e.getLocalizedMessage(), e);
            testResult.setStatus(ITestResult.FAILURE);
            testResult.setThrowable(e);
        }
    }

    private void createDriverFrom(WebDriverTestNGMethodDelegate delegate) throws MalformedURLException {
        Browser browser = delegate.getBrowser();
        WebDriver driver = WebDriverProvider.createDriverWith(browser);
        WebDriverContext.BrowserDriverPair pair = new WebDriverContext.BrowserDriverPair(browser, driver);
        WEB_DRIVER_CONTEXT.set(pair);
    }

    private void injectPomsInto(Object target) {
        stream(target.getClass().getDeclaredFields()).filter(this::isPom).forEach(f -> injectFieldOwnInstance(f, target));
    }

    private boolean isPom(Field field) {
        return fieldIsSubClassOf(field, PageObject.class);
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        WEB_DRIVER_CONTEXT.remove();
    }

    private class WebDriverMethodInstance extends MethodInstance {

        private Browser browser;

        WebDriverMethodInstance(ITestNGMethod method, Browser browser) {
            super(method);
            this.browser = browser;
        }

        @Override
        public ITestNGMethod getMethod() {
            ITestNGMethod method = super.getMethod();
            return new WebDriverTestNGMethodDelegate(method, browser);
        }
    }

    private class WebDriverTestNGMethodDelegate implements ITestNGMethod {
        private final ITestNGMethod method;
        private final Browser browser;

        public WebDriverTestNGMethodDelegate(ITestNGMethod method, Browser browser) {
            this.method = method;
            this.browser = browser;
        }

        public Browser getBrowser() {
            return browser;
        }

        @Override
        public Class getRealClass() {
            return method.getRealClass();
        }

        @Override
        public ITestClass getTestClass() {
            return method.getTestClass();
        }

        @Override
        public void setTestClass(ITestClass cls) {
            method.setTestClass(cls);
        }

        @Override
        public Method getMethod() {
            return method.getMethod();
        }

        @Override
        public String getMethodName() {
            return String.format("%s - %s", method.getMethodName(), browser.name());
        }

        @Override
        public Object[] getInstances() {
            return method.getInstances();
        }

        @Override
        public Object getInstance() {
            return method.getInstance();
        }

        @Override
        public long[] getInstanceHashCodes() {
            return method.getInstanceHashCodes();
        }

        @Override
        public String[] getGroups() {
            return method.getGroups();
        }

        @Override
        public String[] getGroupsDependedUpon() {
            return method.getGroupsDependedUpon();
        }

        @Override
        public String getMissingGroup() {
            return method.getMissingGroup();
        }

        @Override
        public void setMissingGroup(String group) {
            method.setMissingGroup(group);
        }

        @Override
        public String[] getBeforeGroups() {
            return method.getBeforeGroups();
        }

        @Override
        public String[] getAfterGroups() {
            return method.getAfterGroups();
        }

        @Override
        public String[] getMethodsDependedUpon() {
            return method.getMethodsDependedUpon();
        }

        @Override
        public void addMethodDependedUpon(String methodName) {
            method.addMethodDependedUpon(methodName);
        }

        @Override
        public boolean isTest() {
            return method.isTest();
        }

        @Override
        public boolean isBeforeMethodConfiguration() {
            return method.isBeforeMethodConfiguration();
        }

        @Override
        public boolean isAfterMethodConfiguration() {
            return method.isAfterMethodConfiguration();
        }

        @Override
        public boolean isBeforeClassConfiguration() {
            return method.isBeforeClassConfiguration();
        }

        @Override
        public boolean isAfterClassConfiguration() {
            return method.isAfterClassConfiguration();
        }

        @Override
        public boolean isBeforeSuiteConfiguration() {
            return method.isBeforeSuiteConfiguration();
        }

        @Override
        public boolean isAfterSuiteConfiguration() {
            return method.isAfterSuiteConfiguration();
        }

        @Override
        public boolean isBeforeTestConfiguration() {
            return method.isBeforeTestConfiguration();
        }

        @Override
        public boolean isAfterTestConfiguration() {
            return method.isAfterTestConfiguration();
        }

        @Override
        public boolean isBeforeGroupsConfiguration() {
            return method.isBeforeGroupsConfiguration();
        }

        @Override
        public boolean isAfterGroupsConfiguration() {
            return method.isAfterGroupsConfiguration();
        }

        @Override
        public long getTimeOut() {
            return method.getTimeOut();
        }

        @Override
        public void setTimeOut(long timeOut) {
            method.setTimeOut(timeOut);
        }

        @Override
        public int getInvocationCount() {
            return method.getInvocationCount();
        }

        @Override
        public void setInvocationCount(int count) {
            method.setInvocationCount(count);
        }

        @Override
        public int getTotalInvocationCount() {
            return method.getTotalInvocationCount();
        }

        @Override
        public int getSuccessPercentage() {
            return method.getSuccessPercentage();
        }

        @Override
        public String getId() {
            return method.getId();
        }

        @Override
        public void setId(String id) {
            method.setId(id);
        }

        @Override
        public long getDate() {
            return method.getDate();
        }

        @Override
        public void setDate(long date) {
            method.setDate(date);
        }

        @Override
        public boolean canRunFromClass(IClass testClass) {
            return method.canRunFromClass(testClass);
        }

        @Override
        public boolean isAlwaysRun() {
            return method.isAlwaysRun();
        }

        @Override
        public int getThreadPoolSize() {
            return method.getThreadPoolSize();
        }

        @Override
        public void setThreadPoolSize(int threadPoolSize) {
            method.setThreadPoolSize(threadPoolSize);
        }

        @Override
        public boolean getEnabled() {
            return method.getEnabled();
        }

        @Override
        public String getDescription() {
            return method.getDescription();
        }

        @Override
        public void setDescription(String description) {
            method.setDescription(description);
        }

        @Override
        public void incrementCurrentInvocationCount() {
            method.incrementCurrentInvocationCount();
        }

        @Override
        public int getCurrentInvocationCount() {
            return method.getCurrentInvocationCount();
        }

        @Override
        public int getParameterInvocationCount() {
            return method.getParameterInvocationCount();
        }

        @Override
        public void setParameterInvocationCount(int n) {
            method.setParameterInvocationCount(n);
        }

        @Override
        public void setMoreInvocationChecker(Callable<Boolean> moreInvocationChecker) {
            method.setMoreInvocationChecker(moreInvocationChecker);
        }

        @Override
        public boolean hasMoreInvocation() {
            return method.hasMoreInvocation();
        }

        @Override
        public ITestNGMethod clone() {
            return method.clone();
        }

        @Override
        public IRetryAnalyzer getRetryAnalyzer() {
            return method.getRetryAnalyzer();
        }

        @Override
        public void setRetryAnalyzer(IRetryAnalyzer retryAnalyzer) {
            method.setRetryAnalyzer(retryAnalyzer);
        }

        @Override
        public boolean skipFailedInvocations() {
            return method.skipFailedInvocations();
        }

        @Override
        public void setSkipFailedInvocations(boolean skip) {
            method.setSkipFailedInvocations(skip);
        }

        @Override
        public long getInvocationTimeOut() {
            return method.getInvocationTimeOut();
        }

        @Override
        public boolean ignoreMissingDependencies() {
            return method.ignoreMissingDependencies();
        }

        @Override
        public void setIgnoreMissingDependencies(boolean ignore) {
            method.setIgnoreMissingDependencies(ignore);
        }

        @Override
        public List<Integer> getInvocationNumbers() {
            return method.getInvocationNumbers();
        }

        @Override
        public void setInvocationNumbers(List<Integer> numbers) {
            method.setInvocationNumbers(numbers);
        }

        @Override
        public void addFailedInvocationNumber(int number) {
            method.addFailedInvocationNumber(number);
        }

        @Override
        public List<Integer> getFailedInvocationNumbers() {
            return method.getFailedInvocationNumbers();
        }

        @Override
        public int getPriority() {
            return method.getPriority();
        }

        @Override
        public void setPriority(int priority) {
            method.setPriority(priority);
        }

        @Override
        public XmlTest getXmlTest() {
            return method.getXmlTest();
        }

        @Override
        public ConstructorOrMethod getConstructorOrMethod() {
            return method.getConstructorOrMethod();
        }

        @Override
        public Map<String, String> findMethodParameters(XmlTest test) {
            return method.findMethodParameters(test);
        }

        @Override
        public String getQualifiedName() {
            return method.getQualifiedName();
        }

        @Override
        public int compareTo(Object o) {
            return method.compareTo(o);
        }
    }
}
