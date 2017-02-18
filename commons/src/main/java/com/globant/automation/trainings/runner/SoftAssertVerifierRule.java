package com.globant.automation.trainings.runner;

import com.google.common.base.Optional;
import com.smarttested.qa.smartassert.SoftAssertException;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.function.Supplier;

import static com.globant.automation.trainings.logging.Reporter.REPORTER;
import static com.globant.automation.trainings.utils.Lazy.lazily;
import static com.smarttested.qa.smartassert.SmartAssert.getSoftFailures;
import static com.smarttested.qa.smartassert.SmartAssert.validateSoftAsserts;

/**
 * @author Juan Krzemien
 */
public class SoftAssertVerifierRule implements TestRule {

    private static final Supplier<SoftAssertVerifierRule> INSTANCE = lazily(SoftAssertVerifierRule::new);

    public static SoftAssertVerifierRule instance() {
        return INSTANCE.get();
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new VerifierStatement(base);
    }

    private class VerifierStatement extends Statement {

        private Statement base;

        private boolean expectsException;

        private VerifierStatement(Statement base) {
            this.base = base;
            this.expectsException = ExpectException.class.isAssignableFrom(base.getClass());
        }

        @Override
        public void evaluate() throws Throwable {
            try {
                base.evaluate();
            } catch (AssertionError e) {
                REPORTER.error(e.getLocalizedMessage(), e);
                if (expectsException) {
                    Optional<SoftAssertException> softException = getSoftFailures().getException();
                    if (softException.isPresent() && e.getMessage().contains(softException.get().getClass().getCanonicalName())) {
                        /* everything is OK, we expect this exception. Leave without error */
                        return;
                    }
                    throw e;
                }
            }
            try {
                validateSoftAsserts();
            } catch (SoftAssertException sae) {
                REPORTER.error(sae.getLocalizedMessage(), sae);
                throw sae;
            } finally {
                getSoftFailures().cleanupFailures();
            }
        }
    }
}

