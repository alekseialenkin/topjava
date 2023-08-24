package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.service.MealServiceTest;

import java.util.concurrent.TimeUnit;

public class TimeLoggingRule {
    private static final StringBuilder result = new StringBuilder("\n          Test                    Duration\n");
    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);
    public static final Stopwatch STOPWATCH = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            log.info("Execution time of test {}: {}ms", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            result.append(String.format("%-30s", description.getMethodName()))
                    .append(String.format("%10d", TimeUnit.NANOSECONDS.toMillis(nanos))).append("ms").append("\n");
        }
    };

    public static void getTestsTimeResult() {
        log.info(result.toString());
    }
}
