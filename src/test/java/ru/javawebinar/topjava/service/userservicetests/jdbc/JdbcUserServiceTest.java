package ru.javawebinar.topjava.service.userservicetests.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.userservicetests.UserServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends UserServiceTest {
}
