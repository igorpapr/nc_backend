package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.UserDao;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.data.rowmappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                SELECT_QUERY,
                new UserMapper()

        );
    }

    @Override
    public User getByActivationUrl(String activationUrl) {
        try {
            return jdbcTemplate.queryForObject(SELECT_QUERY +
                            "WHERE activation_url = ?",
                    new Object[]{activationUrl},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getByRecoverUrl(String recoverUrl) {
        try {
            return jdbcTemplate.queryForObject(SELECT_QUERY +
                            "WHERE recovery_url = ?",
                    new Object[]{recoverUrl},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public List<User> getAllByRoleUser() {
        return jdbcTemplate.query(SELECT_QUERY +
                        "WHERE roles.role_id = 1",
                new UserMapper()
        );
    }

    @Override
    public List<User> getBySubStr(String str) {
        return jdbcTemplate.query(SELECT_QUERY +
                        "WHERE LOWER(username) LIKE LOWER(?)",
                new UserMapper(),
                str + "%");
    }

    @Override
    public List<User> getBySubStrAndRoleUser(String str) {
        return jdbcTemplate.query(SELECT_QUERY +
                        "WHERE LOWER(username) LIKE LOWER(?)" +
                        "AND roles.role_id = 1 ",
                new UserMapper(),
                str + "%");
    }


    @Override
    public User getByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(SELECT_QUERY +
                            "WHERE email = ?",
                    new Object[]{email},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getByUsername(String username) {

        try {
            return jdbcTemplate.queryForObject(SELECT_QUERY +
                            "WHERE username=?",
                    new Object[]{username},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getById(String id) {

        try {
            return jdbcTemplate.queryForObject(SELECT_QUERY +
                            " WHERE user_id = UUID(?)",
                    new Object[]{id},
                    new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User save(User user) {
        jdbcTemplate.update(SAVE_QUERY,
                UUID.randomUUID(), user.getUsername(), user.getEmail(), user.getPassword(), user.isActivated(),
                user.isVerified(), user.isOnline(), user.getActivationUrl(), user.getCreationDate(), user.getCreationDate(),
                user.getRole().ordinal() + 1
        );

        return getByEmail(user.getEmail());

    }

    @Override
    public void deleteById(String id) {
        jdbcTemplate.update(DELETE_QUERY + " WHERE user_id = UUID( ?)", id);
    }

    @Override
    public int deleteIfLinkExpired() {
        return jdbcTemplate
                .update(DELETE_QUERY +
                        " WHERE is_verified = 'false' and CURRENT_TIMESTAMP - date_acc_creation >= '1 DAY'");
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(UPDATE_QUERY +
                        "WHERE user_id = UUID(?)",
                user.getUsername(), user.getEmail(), user.getPassword(), user.isActivated(), user.isVerified(),
                user.isOnline(), user.getLastTimeOnline(), user.getImage(), user.getAboutMe(), user.getRecoveryUrl(),
                user.getRecoverySentTime(), user.getRole().ordinal() + 1, user.getId());

    }
}
