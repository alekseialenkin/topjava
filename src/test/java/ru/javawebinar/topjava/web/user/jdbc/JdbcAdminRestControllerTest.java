package ru.javawebinar.topjava.web.user.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.web.user.AdminRestControllerTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcAdminRestControllerTest extends AdminRestControllerTest {

}
