package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> USER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validate(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(user);
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password,
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            deleteRoles(user);
            insertRoles(user);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = namedParameterJdbcTemplate.query("""
                SELECT *
                FROM (
                         SELECT *
                         FROM users
                         WHERE id=:id
                    ) AS u
                         LEFT JOIN (
                    SELECT user_id,string_agg(role, ',') roles
                    FROM user_role 
                    WHERE user_id=:id
                    GROUP BY user_id) AS ur ON u.id = ur.user_id""", Map.of("id", id), USER_ROW_MAPPER);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = namedParameterJdbcTemplate.query("""
                        SELECT *
                        FROM (
                                 SELECT *
                                 FROM users
                                 WHERE email=:email                               
                             ) AS u
                                 LEFT JOIN (
                            SELECT user_id,string_agg(role, ',') roles
                            FROM user_role
                            WHERE user_id = (SELECT id FROM users WHERE email=:email)
                            GROUP BY user_id) AS ur ON u.id = ur.user_id""", Map.of("email", email),
                USER_ROW_MAPPER);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("""
                        SELECT *
                        FROM (
                                 SELECT *
                                 FROM users
                                 ORDER BY name,email
                             ) AS u
                                 LEFT JOIN (
                            SELECT user_id,string_agg(role, ',') roles
                            FROM user_role 
                            GROUP BY user_id) AS ur ON u.id = ur.user_id""",
                USER_ROW_MAPPER);
    }

    private BatchPreparedStatementSetter getSetter(User user) {
        return new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(2, String.join(",", user.getRoles().stream().map(Role::name).toList()));
                ps.setInt(1, user.id());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        };
    }

    private void deleteRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.id());
    }

    private void insertRoles(User user) {
        jdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role) VALUES (?,?)", getSetter(user));
    }
}
