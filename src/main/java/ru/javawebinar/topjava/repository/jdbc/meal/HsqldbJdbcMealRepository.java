package ru.javawebinar.topjava.repository.jdbc.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import static ru.javawebinar.topjava.Profiles.HSQL_DB;

@Profile(HSQL_DB)
@Repository
public class HsqldbJdbcMealRepository extends JdbcMealRepository {

    @Autowired
    public HsqldbJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected Date getDate(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
