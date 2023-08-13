package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(1, USER_ID);
        assertEquals(meal, TEST_MEAL);
    }

    @Test
    public void delete() {
        service.delete(1, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(1, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND_MEAL_ID, NOT_FOUND_MEAL_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertArrayEquals(FILTERED_MEALS.toArray(), service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 30),
                        LocalDate.of(2020, Month.JANUARY, 30), USER_ID).stream()
                .sorted(Comparator.comparing(Meal::getDateTime)
                        .reversed())
                .toArray());
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertArrayEquals(USER_MEALS.toArray(), all.toArray());
    }

    @Test
    public void update() {
        Meal updatedMeal = getUpdatedMeal();
        service.update(updatedMeal, USER_ID);
        assertEquals(service.get(updatedMeal.getId(), USER_ID), updatedMeal);
    }

    @Test
    public void updateMealOfOtherUser() {
        Meal mealOfOtherUser = service.get(USER_MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.update(mealOfOtherUser, ADMIN_ID));
    }

    @Test
    public void deleteMealOfOtherUser() {
        int mealIdOfOtherUser = service.get(USER_MEAL_ID, USER_ID).getId();
        assertThrows(NotFoundException.class, () -> service.delete(mealIdOfOtherUser, ADMIN_ID));
    }

    @Test
    public void getMealOfOtherUser() {
        int mealIdOfOtherUser = service.get(USER_MEAL_ID, USER_ID).getId();
        assertThrows(NotFoundException.class, () -> service.get(mealIdOfOtherUser, ADMIN_ID));
    }

    @Test
    public void duplicateDateTimeCreate() {
        service.create(getNewMeal(), USER_ID);
        assertThrows(DuplicateKeyException.class, () -> service.create(getNewMeal(), USER_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNewMeal(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNewMeal();
        newMeal.setId(newId);
        assertEquals(newMeal, created);
    }
}