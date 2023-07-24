package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    Meal create(Meal meal);

    Meal update(Meal meal);

    Meal get(int id);

    void delete(int id);

    Collection<Meal> getAll();

}
