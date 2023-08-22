package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import java.util.concurrent.TimeUnit;

public class TimeLoggingRule {
    public static final StringBuilder result = new StringBuilder("\n          Test                    Duration\n");

    public static final Stopwatch STOPWATCH = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            result.append(String.format("%-30s", description.getMethodName()))
                    .append(String.format("%10d", TimeUnit.NANOSECONDS.toMillis(nanos))).append("ms").append("\n");
        }
    };
}
