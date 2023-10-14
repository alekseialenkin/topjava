package ru.javawebinar.topjava.web.user.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.web.user.AdminRestControllerTest;

import static ru.javawebinar.topjava.Profiles.JPA;

@ActiveProfiles(JPA)
public class JpaAdminRestControllerTest extends AdminRestControllerTest {
}
