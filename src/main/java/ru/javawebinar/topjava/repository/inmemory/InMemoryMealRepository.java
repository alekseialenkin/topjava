package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Map<Integer, Meal>> mealsWithUserId = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.userMeals.forEach(meal -> save(meal, MealsUtil.USER_ID));
        MealsUtil.adminMeals.forEach(meal -> save(meal, MealsUtil.ADMIN_ID));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        Map<Integer, Meal> meals = mealsWithUserId.computeIfAbsent(userId, idOfUser -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            meals.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int mealId, int userId) {
        log.info("delete {}", mealId);
        return mealsWithUserId.get(userId).remove(mealId) != null;
    }

    @Override
    public Meal get(int mealId, int userId) {
        log.info("get {}", mealId);
        return mealsWithUserId.getOrDefault(userId, new ConcurrentHashMap<>()).get(mealId);
    }

    @Override
    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        return MealsUtil.getTos(mealsWithUserId.get(userId).values(), caloriesPerDay);
    }

    @Override
    public List<MealTo> getAllSorted(int userId, int caloriesPerDay, LocalDate startDate, LocalDate endDate,
                                     LocalTime startTime, LocalTime endTime) {
        return MealsUtil.getFilteredTos(mealsWithUserId.get(userId).values(), caloriesPerDay, startDate, endDate,
                startTime, endTime);
    }
}

