package com.globant.automation.trainings.tests.concurrency.threadsafety;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.tests.concurrency.threadsafety.interfaces.IIntGenerator;

import java.time.Duration;
import java.util.Collection;

/**
 * @author Juan Krzemien
 */
public class HideNonRelatedStuff implements Logging {

    final Collection<Integer> storageForMainThread;
    final Collection<Integer> storageForThreads;
    final IIntGenerator numberGenerator;

    final String collectionName;
    final String generatorName;

    static final Duration MAX_RUN_TIME_SECONDS = Duration.ofSeconds(2);
    static final int MAX_THREADS = 5;
    static final int MAX_NUMBERS_TO_RETRIEVE = 5;

    HideNonRelatedStuff(IIntGenerator generator, Collection<Integer> forMain, Collection<Integer> forThreads) {
        this.numberGenerator = generator;
        this.storageForMainThread = forMain;
        this.storageForThreads = forThreads;
        this.collectionName = forThreads.getClass().getSimpleName();
        this.generatorName = numberGenerator.getClass().getSimpleName();
    }
}
