package ru.javawebinar.topjava.service.userservicetests.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.userservicetests.UserServiceTest;

import static ru.javawebinar.topjava.Profiles.JPA;

@ActiveProfiles(JPA)
public class JpaUserServiceTest extends UserServiceTest {
}
