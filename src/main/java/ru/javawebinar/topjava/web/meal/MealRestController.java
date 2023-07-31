package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final MealService service;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll(int userId) {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllSorted(LocalTime startTime, LocalTime endTime, int userId) {
        log.info("getAllSorted");
        return MealsUtil.getFilteredTos(service.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY, startTime, endTime);
    }

    public Meal get(int id, int userId) {
        log.info("get {}", id);
        return service.get(id, userId);
    }

    public Meal create(Meal meal, int userId) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public void delete(int id, int userId) {
        log.info("delete {}", id);
        service.delete(id, userId);
    }

    public void update(Meal user, int id, int userId) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user, userId);
    }

}