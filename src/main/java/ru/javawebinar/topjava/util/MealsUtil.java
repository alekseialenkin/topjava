package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );
    public final static int CALORIES_PER_DAY = 2000;

    public static List<MealTo> to(Collection<Meal> meals, int caloriesPerDay) {
        Map<LocalDate, Integer> sumOfCaloriesPerDay = meals.stream()
                .collect(Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum));
        return meals.stream()
                .map(userMeal -> makeTo(userMeal,sumOfCaloriesPerDay.get(userMeal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static MealTo makeTo(Meal meal,boolean excess) {
        return new MealTo(meal.getId(),meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
