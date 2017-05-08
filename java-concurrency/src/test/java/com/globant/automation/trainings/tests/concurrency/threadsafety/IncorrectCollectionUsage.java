package com.globant.automation.trainings.tests.concurrency.threadsafety;

import com.globant.automation.trainings.tests.concurrency.threadsafety.interfaces.IIntGenerator;
import com.globant.automation.trainings.timing.TimedIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.String.format;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertFalse;

/**
 * Java concurrency - Example 1
 * <p>
 * The intention is to show how concurrency issues happen when choosing the wrong collection types.
 * Issues can happen even if the rest of the code is Thread Safe.
 * <p>
 * Run tests enough times and you will see the first two (ONLY) failing from time to time.
 *
 * @author Juan Krzemien
 */
@RunWith(Parameterized.class)
public class IncorrectCollectionUsage extends HideNonRelatedStuff {
    private final Runnable taskRetrieveUniqueNumbers;

    public IncorrectCollectionUsage(IIntGenerator generator, Collection<Integer> collectionMain, Collection<Integer> collectionThreads) {
        super(generator, collectionMain, collectionThreads);
        this.taskRetrieveUniqueNumbers = () -> range(0, MAX_NUMBERS_TO_RETRIEVE).forEach(i -> {
            try {
                storageForThreads.add(generator.getNextInt());
            } catch (ArrayIndexOutOfBoundsException e) {
                getLogger().info(format("[Data structure %s issue] Could not grow as fast as threads needed to...", collectionName));
                throw e;
            }
        });
    }

    @Parameters(name = "Combination: {0} {1} {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Safe generators
                {new SafeIntGenerator(), new ArrayList<>(), new ArrayList<>()},
                {new AnotherSafeIntGenerator(), new ArrayList<>(), new ArrayList<>()},
                {new SafeIntGenerator(), new CopyOnWriteArrayList<>(), new CopyOnWriteArrayList<>()},
                {new AnotherSafeIntGenerator(), new CopyOnWriteArrayList<>(), new CopyOnWriteArrayList<>()},
                {new SafeIntGenerator(), new ConcurrentSkipListSet<>(), new ConcurrentSkipListSet<>()},
                {new AnotherSafeIntGenerator(), new ConcurrentSkipListSet<>(), new ConcurrentSkipListSet<>()},
                {new SafeIntGenerator(), new Vector<>(), new Vector<>()},
                {new AnotherSafeIntGenerator(), new Vector<>(), new Vector<>()}
        });
    }

    /**
     * Loop for MAX_RUN_TIME_SECONDS seconds doing:
     * - Clear structures
     * - Spawn MAX_THREADS threads -> Each thread retrieves MAX_THREADS numbers from numberGenerator
     * - Main thread retrieves MAX_THREADS numbers from numberGenerator too
     * - Asserts retrieved numbers: no duplicated/missing
     */
    @Test
    public void collectionIssues() {
        new TimedIterator(MAX_RUN_TIME_SECONDS).forEachRemaining(
                l -> {
                    storageForMainThread.clear();
                    storageForThreads.clear();
                    range(0, MAX_THREADS).parallel().forEach(i -> taskRetrieveUniqueNumbers.run());
                    // No parallel() here...
                    range(0, MAX_NUMBERS_TO_RETRIEVE).forEach(i -> storageForMainThread.add(numberGenerator.getNextInt()));
                    printStats(l);
                }
        );
    }

    private void printStats(Long loopNumber) {
        getLogger().info(format("%s/%s - Attempt #%s", generatorName, collectionName, loopNumber));
        getLogger().info(format("Integers retrieved by Main: %s", storageForMainThread));
        getLogger().info(format("Integers retrieved by Threads: %s", storageForThreads));
        try {
            assertFalse(format("[Data structure %s issue] Some elements are NULL!", collectionName), storageForThreads.parallelStream().anyMatch(Objects::isNull));
        } catch (AssertionError ae) {
            getLogger().info(format("Awww snap! I started to behave erratically on attempt #%s...Good times I'm not in a production environment!", loopNumber));
            throw ae;
        }
    }
}

