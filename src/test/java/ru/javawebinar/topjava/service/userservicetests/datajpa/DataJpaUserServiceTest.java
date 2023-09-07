package ru.javawebinar.topjava.service.userservicetests.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.userservicetests.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collections;

import static ru.javawebinar.topjava.Profiles.DATAJPA;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getWithMeal() {
        User u = service.getWithMeal(UserTestData.USER_WITH_MEAL_ID);
        UserTestData.USER_MATCHER.assertMatch(u, UserTestData.userWithMeal);
        MealTestData.MEAL_MATCHER.assertMatch(u.getMeals(), MealTestData.userMeals);
    }

    @Test
    public void getWithMealNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> service.getWithMeal(UserTestData.NOT_FOUND));
    }

    @Test
    public void getWithoutMeals() {
        User u = service.getWithMeal(UserTestData.GUEST_ID);
        UserTestData.USER_MATCHER.assertMatch(u, UserTestData.guest);
        MealTestData.MEAL_MATCHER.assertMatch(u.getMeals(), Collections.emptyList());
    }

}
