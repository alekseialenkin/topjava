package ru.javawebinar.topjava.service.mealservicetests.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.mealservicetests.MealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.Profiles.DATAJPA;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {

    @Test
    public void getWithUser() {
        Meal m = service.getWithUser(MealTestData.USER_MEAL_ID, UserTestData.USER_WITH_MEAL_ID);
        MealTestData.MEAL_MATCHER.assertMatch(m, MealTestData.userMeal1);
        UserTestData.USER_MATCHER.assertMatch(m.getUser(), UserTestData.userWithMeal);
    }

    @Test
    public void getWithUserNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> service.getWithUser(MealTestData.NOT_FOUND, UserTestData.USER_WITH_MEAL_ID));
    }

    @Test
    public void getWithUserNotOwn() {
        Assert.assertThrows(NotFoundException.class, () -> service.getWithUser(MealTestData.USER_MEAL_ID, UserTestData.ADMIN_ID));
    }
}
