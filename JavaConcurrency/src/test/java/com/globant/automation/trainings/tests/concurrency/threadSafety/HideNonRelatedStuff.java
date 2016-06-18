package com.globant.automation.trainings.tests.concurrency.threadSafety;

import com.globant.automation.trainings.tests.concurrency.threadSafety.interfaces.IIntGenerator;

import java.time.Duration;
import java.util.Collection;

/**
 * Created by Juan Krzemien on 6/18/2016.
 */
public class HideNonRelatedStuff {

    final Collection<Integer> storageForMainThread;
    final Collection<Integer> storageForThreads;
    final IIntGenerator numberGenerator;

    final String collectionName;
    final String generatorName;

    final Duration MAX_RUN_TIME_SECONDS = Duration.ofSeconds(2);
    final int MAX_THREADS = 5;
    final int MAX_NUMBERS_TO_RETRIEVE = 5;

    HideNonRelatedStuff(IIntGenerator generator, Collection<Integer> forMain, Collection<Integer> forThreads) {
        this.numberGenerator = generator;
        this.storageForMainThread = forMain;
        this.storageForThreads = forThreads;
        this.collectionName = forThreads.getClass().getSimpleName();
        this.generatorName = numberGenerator.getClass().getSimpleName();
    }
}
