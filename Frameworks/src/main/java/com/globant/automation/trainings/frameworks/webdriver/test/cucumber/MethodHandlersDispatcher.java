package com.globant.automation.trainings.frameworks.webdriver.test.cucumber;

import com.globant.automation.trainings.frameworks.webdriver.exceptions.MethodInvocationError;
import com.globant.automation.trainings.frameworks.webdriver.interfaces.Waitable;
import com.globant.automation.trainings.frameworks.webdriver.test.pageobject.ActionOnField;
import com.globant.automation.trainings.frameworks.webdriver.test.pageobject.GetterForField;
import com.globant.automation.trainings.frameworks.webdriver.test.pageobject.PageObject;
import com.google.common.base.Predicate;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.globant.automation.trainings.frameworks.webdriver.config.Framework.CONFIGURATION;
import static com.globant.automation.trainings.frameworks.webdriver.factories.Drivers.INSTANCES;
import static com.globant.automation.trainings.frameworks.webdriver.test.cucumber.Context.PAGE_FROM_CONTEXT;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toSet;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Juan Krzemien
 */
public class MethodHandlersDispatcher implements Waitable {

    private static final Logger LOG = getLogger(PageObject.class);
    private final WebDriverWait wait;

    public static class ElementType {

        public static final ElementType BUTTON = new ElementType("Button");
        public static final ElementType LINK = new ElementType("Link");
        public static final ElementType TEXT = new ElementType("Text");
        public static final ElementType SELECT = new ElementType("Select");
        public static final ElementType CHECKBOX = new ElementType("Check");
        public static final ElementType IFRAME = new ElementType("IFrame");
        public static final ElementType ANY = new ElementType("");
        private final String name;

        ElementType(String name) {
            this.name = name.toLowerCase();
        }
    }

    @Override
    public <T> T doWait(ExpectedCondition<T> condition) {
        return wait.until(condition);
    }

    public MethodHandlersDispatcher() {
        this.wait = new WebDriverWait(INSTANCES.get(), CONFIGURATION.WebDriver().getExplicitTimeOut());
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        wait.pollingEvery(CONFIGURATION.WebDriver().getPollingEveryMs(), MILLISECONDS);
    }

    public <T extends PageObject, K> K triggerMethodGetter(final String fieldName, final ElementType type) {
        final T currentPageObject = PAGE_FROM_CONTEXT();
        if (currentPageObject == null) {
            throw new IllegalArgumentException("Page object is null in context! You have not initialized any Page Object!");
        }
        Method[] methods = currentPageObject.getClass().getMethods();
        Set<Method> handlers = stream(methods).filter(m -> m.getName().startsWith("get") && m.isAnnotationPresent(GetterForField.class)).collect(toSet());
        for (Method method : handlers) {
            GetterForField gf = method.getAnnotation(GetterForField.class);
            if (matchesNameAndType(gf.value(), fieldName, type)) {
                return invokeMethodRetrying(currentPageObject, method, null);
            }
        }

        String msg = "Could not find a getter method that handles a field of type [%s] named [%s] in page object [%s]!\nAre you missing an %s annotation?";
        throw new MethodInvocationError(format(msg, type.name.toUpperCase(), fieldName, PAGE_FROM_CONTEXT().toString(), GetterForField.class.getSimpleName()));
    }

    public <T extends PageObject, K extends PageObject> K triggerMethodHandler(final String fieldName, final ElementType type, final Object... args) throws MethodInvocationError {
        final T currentPageObject = PAGE_FROM_CONTEXT();
        if (currentPageObject == null) {
            throw new IllegalArgumentException("Page object is null in context! You have not initialized any Page Object!");
        }
        Method[] methods = currentPageObject.getClass().getMethods();
        Set<Method> handlers = stream(methods).filter(m -> m.isAnnotationPresent(ActionOnField.class)).collect(toSet());
        for (Method method : handlers) {
            ActionOnField hf = method.getAnnotation(ActionOnField.class);
            if (matchesNameAndType(hf.value(), fieldName, type)) {
                return invokeMethodRetrying(currentPageObject, method, args);
            }
        }

        String msg = "Could not find a method that handles a field of type [%s] named [%s] in page object [%s]!\nAre you missing an %s annotation?";
        throw new MethodInvocationError(format(msg, type.name.toUpperCase(), fieldName, PAGE_FROM_CONTEXT().toString(), ActionOnField.class.getSimpleName()));
    }

    private <T extends PageObject, K> K invokeMethodRetrying(final T currentPageObject, final Method method, final Object[] args) throws MethodInvocationError {
        final Object[] result = new Object[]{null};
        final int MAX_RETRIES = 3;
        new FluentWait<>(method)
                .ignoring(WebDriverException.class)
                .withTimeout(MAX_RETRIES * CONFIGURATION.WebDriver().getExplicitTimeOut(), SECONDS)
                .pollingEvery(1, SECONDS)
                .withMessage(format("While trying to invoke method [%s] of page object [%s]", method.getName(), currentPageObject.toString()))
                .until(new Predicate<Method>() {
                    AtomicInteger retries = new AtomicInteger(1);

                    @Override
                    public boolean apply(Method m) {
                        try {
                            LOG.info(format("Triggering method [%s] of page object [%s] [Attempt #%d]...", method.getName(), currentPageObject.toString(), retries.get()));
                            result[0] = m.invoke(currentPageObject, args);
                            return true;
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            if (retries.incrementAndGet() <= MAX_RETRIES) return false;
                            String msg = format("Failed invocation of method [%s] of page object [%s]!", method.getName(), currentPageObject.toString());
                            LOG.error(msg);
                            throw new MethodInvocationError(msg);
                        }
                    }
                });
        return (K) result[0];
    }

    private static boolean matchesNameAndType(String currentField, String desiredFieldName, ElementType type) {
        String asJavaFieldName = desiredFieldName.replaceAll(" ", "") + type.name;
        return type == ElementType.ANY ? currentField.toLowerCase().startsWith(asJavaFieldName.toLowerCase()) : currentField.equalsIgnoreCase(asJavaFieldName);
    }

}
