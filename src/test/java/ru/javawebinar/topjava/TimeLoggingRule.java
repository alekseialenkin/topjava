package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeLoggingRule implements TestRule {
    private final Logger log = LoggerFactory.getLogger(TimeLoggingRule.class);

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long startTime = 0;
                long endTime;
                try {
                    startTime = System.currentTimeMillis();
                    base.evaluate();
                } finally {
                    endTime = System.currentTimeMillis();
                    log.info("Execution time of test {}: {}ms", description.getMethodName(), endTime - startTime);
                }
            }
        };
    }
}
