package ru.javawebinar.topjava.web.user.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.web.user.ProfileRestControllerTest;

import static ru.javawebinar.topjava.Profiles.JPA;

@ActiveProfiles(JPA)
public class JpaProfileRestControllerTest extends ProfileRestControllerTest {
}
