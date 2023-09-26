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
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> USER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final ResultSetExtractor<List<User>> RESULT_SET_EXTRACTOR = rs -> {
        List<User> userList = new ArrayList<>();
        while (rs.next()) {
            User u = USER_ROW_MAPPER.mapRow(rs, 8);
            Objects.requireNonNull(u).setRoles(Collections.emptyList());
            String role = rs.getString("string_agg");
            List<Role> roles;
            if (role != null) {
                roles = Arrays.stream(role.split(",")).map(s -> s.equalsIgnoreCase("admin") ? Role.ADMIN : Role.USER)
                        .toList();
                u.setRoles(roles);
            }
            userList.add(u);
        }
        return userList;
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
        ValidationUtil.validate(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            jdbcTemplate.update("INSERT INTO user_role (user_id, role) VALUES (?,?)", user.id(),
                    String.join(",", user.getRoles().stream().map(String::valueOf).toList()));
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0 || jdbcTemplate.batchUpdate("""
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
