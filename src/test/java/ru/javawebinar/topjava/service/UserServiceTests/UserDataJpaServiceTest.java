package ru.javawebinar.topjava.service.UserServiceTests;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MatcherFactory;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;

@ActiveProfiles("datajpa")
public class UserDataJpaServiceTest extends UserServiceTest {

    @Test
    public void getUserWithMeal() {
        User u = service.getUserWithMeal(UserTestData.USER_WITH_MEAL_ID);
        MatcherFactory.usingIgnoringFieldsComparator("registered", "roles").assertMatch(u, UserTestData.userWithMeal);
    }
}
