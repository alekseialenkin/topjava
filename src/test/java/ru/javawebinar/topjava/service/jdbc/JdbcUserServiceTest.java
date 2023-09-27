package ru.javawebinar.topjava.service.jdbc;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.List;

import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.UserTestData.getUpdated;

@ActiveProfiles(JDBC)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void a1update() {
        User updated = getUpdated();
        service.update(updated);
        USER_MATCHER.assertMatch(service.getAll(), admin, guest, getUpdated());
    }

    @Test
    public void a2getAll() {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, admin, guest, user);
    }
}