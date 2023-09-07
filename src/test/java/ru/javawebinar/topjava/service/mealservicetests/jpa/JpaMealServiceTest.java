package ru.javawebinar.topjava.service.mealservicetests.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.mealservicetests.MealServiceTest;

import static ru.javawebinar.topjava.Profiles.JPA;

@ActiveProfiles(JPA)
public class JpaMealServiceTest extends MealServiceTest {

}
