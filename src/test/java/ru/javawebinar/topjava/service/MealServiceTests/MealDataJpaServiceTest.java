package ru.javawebinar.topjava.service.MealServiceTests;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;

@ActiveProfiles("datajpa")
public class MealDataJpaServiceTest extends MealServiceTest {

    @Test
    public void getMealWithUser() {
        Meal m = service.getMealWithUser(MealTestData.MEAL2_ID, UserTestData.USER_WITH_MEAL_ID);
        MealTestData.MEAL_MATCHER.assertMatch(m, MealTestData.userMeal1);
        UserTestData.USER_MATCHER.assertMatch(m.getUser(), UserTestData.userWithMeal);
    }
}
