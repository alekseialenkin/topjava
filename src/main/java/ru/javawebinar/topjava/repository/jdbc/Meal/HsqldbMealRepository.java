package ru.javawebinar.topjava.repository.jdbc.Meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Profile("hsqldb")
@Repository
public class HsqldbMealRepository extends JdbcMealRepository {

    @Autowired
    public HsqldbMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected Date getDate(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
