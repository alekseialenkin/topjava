package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static int mealId = START_SEQ + 3;

    public static final int idOfSomeMeal = START_SEQ + 3;

    public static final int NOT_FOUND_MEAL_ID = 15;

    public static final Meal userMeal1 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
            "Завтрак", 1000);

    private static final Meal userMeal2 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
            "Обед", 500);

    private static final Meal userMeal3 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
            "Ужин", 500);

    private static final Meal userMeal4 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
            "Еда на граничное значение", 1000);

    private static final Meal userMeal5 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
            "Завтрак", 1000);

    private static final Meal userMeal6 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
            "Обед", 500);

    private static final Meal userMeal7 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
            "Ужин", 500);
    public static final List<Meal> userMeal = Stream.of(
                    userMeal1, userMeal2, userMeal3, userMeal4, userMeal5, userMeal6, userMeal7)
            .sorted(Comparator.comparing(Meal::getDateTime)
                    .reversed())
            .collect(Collectors.toList());

    private static final Meal adminMeal1 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
            "Breakfasts", 500);

    private static final Meal adminMeal2 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
            "Lunch", 1000);
    private static final Meal adminMeal3 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
            "Dinner", 500);
    private static final Meal adminMeal4 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
            "Some food", 100);
    private static final Meal adminMeal5 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
            "Breakfasts", 1000);
    private static final Meal adminMeal6 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
            "Lunch", 500);
    private static final Meal adminMeal7 = new Meal(mealId++, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
            "Dinner", 410);

    public static final List<Meal> adminMeal = Stream.of(adminMeal1, adminMeal2, adminMeal3, adminMeal4, adminMeal5, adminMeal6, adminMeal7)
            .sorted(Comparator.comparing(Meal::getDateTime)
                    .reversed())
            .collect(Collectors.toList());

    public static final List<Meal> filteredMeals = Stream.of(
                    userMeal1, userMeal2, userMeal3)
            .sorted(Comparator.comparing(Meal::getDateTime)
                    .reversed())
            .collect(Collectors.toList());

    public static Meal getNewMeal() {
        return new Meal(null, LocalDateTime.of(2019, Month.JANUARY, 1, 12, 0, 0), "New", 1);
    }

    public static Meal getUpdatedMeal() {
        Meal updateMeal = new Meal(userMeal1);
        updateMeal.setCalories(777);
        updateMeal.setDescription("updatedTest");
        updateMeal.setDateTime(LocalDateTime.of(2013, Month.JANUARY, 31, 10, 0));
        return updateMeal;
    }
    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
