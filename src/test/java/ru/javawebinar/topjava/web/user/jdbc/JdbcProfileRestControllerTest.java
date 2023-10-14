package ru.javawebinar.topjava.web.user.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.web.user.ProfileRestControllerTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcProfileRestControllerTest extends ProfileRestControllerTest {
}
