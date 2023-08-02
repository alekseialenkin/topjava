package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealRepository {

    // null if updated meal does not belong to userId
    Meal save(Meal meal, int userId);

    // false if meal does not belong to userId
    boolean delete(int mealId, int userId);

    // null if meal does not belong to userId
    Meal get(int mealId, int userId);

    List<MealTo> getAll(int userId, int caloriesPerDay);

    // ORDERED dateTime desc
    List<MealTo> getAllSorted(int userId, int caloriesPerDay, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime);
}
