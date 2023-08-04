package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static final int USER_ID = 1;

    public static final int ADMIN_ID = 2;

    public static final List<Meal> userMeals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    public static final List<Meal> adminMeals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Breakfasts", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Lunch", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Dinner", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Some food", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Breakfasts", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Lunch", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Dinner", 410)
    );

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay, Collection<Meal> allMeals) {
        return filterByPredicate(meals, allMeals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredByTimeTos(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(meals, caloriesPerDay, meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime));
    }


    public static List<Meal> getFiltered(Collection<Meal> meals, LocalDate startDate, LocalDate endDate,
                                         LocalTime startTime, LocalTime endTime) {
        return filterBySomePredicatesWithoutTos(meals, meal -> DateTimeUtil.isBetween(meal.getDate(),
                startDate, endDate), meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime));
    }

    private static List<Meal> filterBySomePredicatesWithoutTos(Collection<Meal> meals, Predicate<Meal> filterForDate, Predicate<Meal> filterForTime) {
        return meals.stream()
                .filter(filterForDate)
                .filter(filterForTime)
                .collect(Collectors.toList());
    }


    private static List<MealTo> filterBySomePredicates(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filterForDate, Predicate<Meal> filterForTime) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(filterForDate)
                .filter(filterForTime)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static List<MealTo> filterByPredicate(Collection<Meal> meals, Collection<Meal> allMeals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = allMeals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}