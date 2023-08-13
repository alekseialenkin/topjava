package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;

    public static final int ADMIN_ID = START_SEQ + 1;

    public static final int NOT_FOUND_MEAL_ID = 15;

    public static final int USER_MEAL_ID = 1;

    public static final int ADMIN_MEAL_ID = 8;

    public static final Meal TEST_MEAL = new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
            "Завтрак", 500);


    public static final List<Meal> USER_MEALS = Stream.of(
                    new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                    new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                    new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                    new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                    new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                    new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                    new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
            ).sorted(Comparator.comparing(Meal::getDateTime)
                    .reversed())
            .collect(Collectors.toList());


    public static final List<Meal> ADMIN_MEALS = Stream.of(
                    new Meal(8, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Breakfasts", 500),
                    new Meal(9, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Lunch", 1000),
                    new Meal(10, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Dinner", 500),
                    new Meal(11, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Some food", 100),
                    new Meal(12, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Breakfasts", 1000),
                    new Meal(13, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Lunch", 500),
                    new Meal(14, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Dinner", 410)
            ).sorted(Comparator.comparing(Meal::getDateTime)
                    .reversed())
            .collect(Collectors.toList());

    public static final List<Meal> FILTERED_MEALS = Stream.of(
                    new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                    new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                    new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                    new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100))
            .sorted(Comparator.comparing(Meal::getDateTime)
                    .reversed())
            .collect(Collectors.toList());

    public static Meal getNewMeal() {
        return new Meal(null, LocalDateTime.of(2019, Month.JANUARY, 1, 12, 0, 0), "New", 1);
    }

    public static Meal getUpdatedMeal() {
        Meal updateMeal = new Meal(TEST_MEAL);
        updateMeal.setCalories(777);
        updateMeal.setDescription("updatedTest");
        updateMeal.setDateTime(LocalDateTime.of(2013, Month.JANUARY, 31, 10, 0));
        return updateMeal;
    }
}
