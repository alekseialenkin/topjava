package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MealInMemory {
    private final Map<Integer, Meal> mealWithCounterId = new ConcurrentHashMap<>();
    //    https://ru.stackoverflow.com/questions/1042712/%D0%9A%D0%B0%D0%BA-%D1%81%D0%B3%D0%B5%D0%BD%D0%B5%D1%80%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D1%82%D1%8C-id-%D0%B4%D0%BB%D1%8F-%D1%81%D0%B5%D1%80%D0%B8%D0%B0%D0%BB%D0%B8%D0%B7%D1%83%D0%B5%D0%BC%D0%BE%D0%B3%D0%BE-%D0%BE%D0%B1%D1%8A%D0%B5%D0%BA%D1%82%D0%B0
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    public void save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(counter.incrementAndGet());
        }
        mealWithCounterId.put(meal.getId(), meal);
    }

    public Meal get(Integer id) {
        return mealWithCounterId.get(id);
    }

    public void delete(Integer id) {
        mealWithCounterId.remove(id);
    }

    public Collection<Meal> getAll() {
        return mealWithCounterId.values().stream().sorted(Comparator.comparing(Meal::getDateTime)).collect(Collectors.toList());
    }
}
