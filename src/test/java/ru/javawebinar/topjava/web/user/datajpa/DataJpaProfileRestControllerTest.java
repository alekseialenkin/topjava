package ru.javawebinar.topjava.web.user.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.web.user.ProfileRestControllerTest;

import static ru.javawebinar.topjava.Profiles.DATAJPA;

@ActiveProfiles(DATAJPA)
public class DataJpaProfileRestControllerTest extends ProfileRestControllerTest {
}
