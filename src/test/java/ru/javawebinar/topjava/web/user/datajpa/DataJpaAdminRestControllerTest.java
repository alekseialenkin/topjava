package ru.javawebinar.topjava.web.user.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.web.user.AdminRestControllerTest;

import static ru.javawebinar.topjava.Profiles.DATAJPA;

@ActiveProfiles(DATAJPA)
public class DataJpaAdminRestControllerTest extends AdminRestControllerTest {
}
