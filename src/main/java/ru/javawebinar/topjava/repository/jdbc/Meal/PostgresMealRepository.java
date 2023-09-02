package ru.javawebinar.topjava.repository.jdbc.Meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Profile("postgres")
@Repository
public class PostgresMealRepository extends JdbcMealRepository {

    @Autowired
    public PostgresMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected LocalDateTime getDate(LocalDateTime localDateTime) {
        return localDateTime;
    }
}
