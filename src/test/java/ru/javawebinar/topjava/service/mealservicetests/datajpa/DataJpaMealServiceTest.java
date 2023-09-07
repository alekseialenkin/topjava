package ru.javawebinar.topjava.service.mealservicetests.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.mealservicetests.MealServiceTest;

import static ru.javawebinar.topjava.Profiles.DATAJPA;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {

    @Test
    public void getMealWithUser() {
        Meal m = service.getWithUser(MealTestData.USER_MEAL_ID, UserTestData.USER_WITH_MEAL_ID);
        MealTestData.MEAL_MATCHER.assertMatch(m, MealTestData.userMeal1);
        UserTestData.USER_MATCHER.assertMatch(m.getUser(), UserTestData.userWithMeal);
    }


}
