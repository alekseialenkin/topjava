package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final MealService service;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAll");
        return MealsUtil.getFilteredByTime(service.getAll(SecurityUtil.getAuthUserId(),
                        startDate == null ? LocalDate.MIN : startDate, endDate == null ? LocalDate.MAX : endDate),
                SecurityUtil.authUserCaloriesPerDay(),
                startTime == null ? LocalTime.MIN : startTime,
                endTime == null ? LocalTime.MAX : endTime);
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.getAuthUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, SecurityUtil.getAuthUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.getAuthUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.getAuthUserId());
    }

}