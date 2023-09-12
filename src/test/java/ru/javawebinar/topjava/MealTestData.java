package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDateTime.of;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator("user");

    public static final int NOT_FOUND = 10;
    public static final int MEAL1_ID = START_SEQ + 4;
    public static final int ADMIN_MEAL_ID = START_SEQ + 11;
    public static final int USER_MEAL_ID = START_SEQ + 13;

    public static final Meal meal1 = new Meal(MEAL1_ID, of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500,null);
    public static final Meal meal2 = new Meal(MEAL1_ID + 1, of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000, null);
    public static final Meal meal3 = new Meal(MEAL1_ID + 2, of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500, null);
    public static final Meal meal4 = new Meal(MEAL1_ID + 3, of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100, null);
    public static final Meal meal5 = new Meal(MEAL1_ID + 4, of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 500, null);
    public static final Meal meal6 = new Meal(MEAL1_ID + 5, of(2020, Month.JANUARY, 31, 13, 0), "Обед", 1000, null);
    public static final Meal meal7 = new Meal(MEAL1_ID + 6, of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 510, null);
    public static final Meal adminMeal1 = new Meal(ADMIN_MEAL_ID, of(2020, Month.JANUARY, 31, 14, 0), "Админ ланч", 510, null);
    public static final Meal adminMeal2 = new Meal(ADMIN_MEAL_ID + 1, of(2020, Month.JANUARY, 31, 21, 0), "Админ ужин", 1500, null);
    public static final Meal userMeal1 = new Meal(USER_MEAL_ID, of(2020, Month.JANUARY, 31, 15, 0), "Юзер ланч", 610, UserTestData.userWithMeal);
    public static final Meal userMeal2 = new Meal(USER_MEAL_ID + 1, of(2020, Month.JANUARY, 31, 21, 0), "Юзер ужин", 1200, UserTestData.userWithMeal);

    public static final List<Meal> meals = List.of(meal7, meal6, meal5, meal4, meal3, meal2, meal1);

    public static final List<Meal> userMeals = List.of(userMeal2, userMeal1);

    public static Meal getNew() {
        return new Meal(null, of(2020, Month.FEBRUARY, 1, 18, 0), "Созданный ужин", 300, null);
    }

    public static Meal getUpdated() {
        return new Meal(MEAL1_ID, meal1.getDateTime().plus(2, ChronoUnit.MINUTES), "Обновленный завтрак", 200, null);
    }
}
