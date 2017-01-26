package com.globant.automation.trainings.tests.threading;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.timing.Timer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public class ParallelTaskExecution implements Logging {

    private final MyNewService myNewService = new MyNewService();

    @Test
    public void taskDispatcher() {
        new Timer("Method doThingsInParallel") {{
            // code to time
            List<Result> results = myNewService.doThingsInParallel();
            results.forEach(r -> getLogger().info(r.toString()));
        }}.timeMe();
        myNewService.shutdown();
    }

    @Test
    public void taskDispatcherJava8() {
        new Timer("Method doThingsInParallel") {{
            // code to time
            // Does not use Executors...
            List<Result> results = myNewService.doThingsInParallelJava8();
            results.forEach(r -> getLogger().info(r.toString()));
        }}.timeMe();
    }

    private class MyService {
        private ExternalService myAutoWiredService = new ExternalService();

        List<Result> doStuff(Thing thing) {
            long sleepTime = (new Random().nextInt(3) + 1) * 1000;
            getLogger().info(format("Service call delayed %s seconds...", sleepTime));
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return myAutoWiredService.doExternalCall(thing);
        }

    }

    private class MyNewService {

        private final ExecutorService executor = Executors.newFixedThreadPool(3);

        private MyService myService = new MyService();

        private List<Thing> things = someCallToStaticMethodWithThings();

        private List<Thing> someCallToStaticMethodWithThings() {
            return Arrays.asList(new Thing("One"), new Thing("Two"), new Thing("Three"));
        }

        List<Result> doThingsInParallelJava8() {
            final List<Result> results = new ArrayList<>();
            things.parallelStream().forEach(thing -> results.addAll(myService.doStuff(thing)));
            return results;
        }

        List<Result> doThingsInParallel() {
            final List<Future<List<Result>>> futureResults = new ArrayList<>();
            for (final Thing thing : things) {
                futureResults.add(executor.submit(new Callable<List<Result>>() {
                    public List<Result> call() throws Exception {
                        return myService.doStuff(thing);
                    }
                }));
            }

            final List<Result> results = new ArrayList<>();
            for (final Future<List<Result>> futureResult : futureResults) {
                try {
                    results.addAll(futureResult.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return results;
        }

        void shutdown() {
            executor.shutdown();
        }
    }

    private class Result {
        private final Thing value;

        Result(Thing value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Result -> " + value.getValue();
        }
    }

    private class ExternalService {
        List<Result> doExternalCall(Thing thing) {
            return Arrays.asList(new Result(thing), new Result(thing), new Result(thing));
        }
    }

    private class Thing {
        private final String value;

        Thing(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }
    }
}

