package ru.javawebinar.topjava.service.userservicetests.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MatcherFactory;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.userservicetests.UserServiceTest;

import static ru.javawebinar.topjava.Profiles.DATAJPA;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getUserWithMeal() {
        User u = service.getWithMeal(UserTestData.USER_WITH_MEAL_ID);
        MatcherFactory.usingIgnoringFieldsComparator("registered", "roles").assertMatch(u, UserTestData.userWithMeal);
    }

}
