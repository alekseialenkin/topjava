package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> USER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final BeanPropertyRowMapper<Role> ROLE_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Role.class);

    private static final ResultSetExtractor<List<User>> RESULT_SET_EXTRACTOR = rs -> {
        List<User> list = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String password = rs.getString("password");
            int caloriesPerDay = rs.getInt("calories_per_day");
            boolean enabled = rs.getBoolean("enabled");
            Date registered = rs.getDate("registered");
            String role = rs.getString("string_agg");
            List<Role> roles = Collections.emptyList();
            Role r;
            User u = new User(id, name, email, password, caloriesPerDay, enabled, registered, roles);
            if (role != null && role.contains(",")) {
                roles = Arrays.stream(role.split(",")).map(Role::valueOf).toList();
                u.setRoles(roles);
            } else if (role != null) {
                r = Role.valueOf(role);
                u.setRoles(List.of(r));
            }
            list.add(u);
        }
        return list;
    };

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
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            jdbcTemplate.update("INSERT INTO user_role (user_id, role) VALUES (?,?)", user.id(),
                    String.join(",", user.getRoles().stream().map(String::valueOf).toList()));
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0 && namedParameterJdbcTemplate.update("UPDATE user_role SET role=:roles WHERE user_id=:id",
                parameterSource) == 0 || jdbcTemplate.batchUpdate("""
                   UPDATE user_role SET role=? WHERE user_id=?
                """, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, String.join(",", user.getRoles().stream().map(String::valueOf).toList()));
                ps.setInt(2, user.id());
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        }).length == 0) {
            return null;
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
        List<User> users = jdbcTemplate.query("""
                        SELECT *
                        FROM (
                                 SELECT *
                                 FROM users
                                 WHERE id=?
                             ) AS u
                                 LEFT JOIN (
                            SELECT user_id,string_agg(role, ',') FROM user_role WHERE user_id=?
                             group by user_id) AS ur ON u.id = ur.user_id""",
                RESULT_SET_EXTRACTOR, id, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("""
                        SELECT *
                        FROM (
                                 SELECT *
                                 FROM users
                                 WHERE email=?
                             ) AS u
                                 LEFT JOIN (
                            SELECT user_id,string_agg(role, ',') FROM user_role
                             GROUP BY user_id) AS ur ON u.id = ur.user_id""",
                RESULT_SET_EXTRACTOR, email);
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
                            SELECT user_id,string_agg(role, ',') FROM user_role  group by user_id) AS ur ON u.id = ur.user_id""",
                RESULT_SET_EXTRACTOR);
    }
}
