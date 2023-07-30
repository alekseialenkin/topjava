package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private Integer userId;
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            meals.put(meal.getId(), meal);
            repository.put(meal.getUserId(), meals);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getUserId(), (id, oldMap) -> meals).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
//        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int mealId) {
        return repository.get(userId).remove(mealId) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        return repository.get(userId).get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return meals.values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDate)
                        .thenComparing(Meal::getTime))
                .collect(Collectors.toList());
    }
}

