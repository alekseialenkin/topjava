package ru.javawebinar.topjava.service.mealservicetests.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.mealservicetests.MealServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcMealServiceTest extends MealServiceTest {
}