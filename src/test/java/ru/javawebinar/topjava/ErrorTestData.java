package ru.javawebinar.topjava;

import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;

public class ErrorTestData {
    public static MatcherFactory.Matcher<ErrorInfo> ERROR_INFO_MATCHER = MatcherFactory.usingEqualsComparator(ErrorInfo.class);

    public static void isThisError(ResultActions action, ErrorType type) throws Exception {
        ErrorInfo errorInfo = ERROR_INFO_MATCHER.readFromJson(action);
        Assertions.assertEquals(errorInfo.getType(), type);
    }
}
