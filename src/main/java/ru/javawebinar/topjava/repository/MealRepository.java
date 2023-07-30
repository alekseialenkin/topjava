package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

// TODO add userId
public interface MealRepository {
    Integer userId = null;

    // null if updated meal does not belong to userId
    Meal save(Meal meal);

    // false if meal does not belong to userId
    boolean delete(int userId, int mealId);

    // null if meal does not belong to userId
    Meal get(int userId,int mealId);

    // ORDERED dateTime desc
    Collection<Meal> getAll();
}
