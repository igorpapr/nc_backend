package net.dreamfteam.quiznet.data.dao.impl;


import net.dreamfteam.quiznet.data.dao.UserDao;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.data.rowmappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
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
        return jdbcTemplate.queryForList("SELECT * FROM users", User.class);
    }

    @Override
    public User getByActivationUrl(String activationUrl) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users where activation_url = ?",
                    new Object[]{activationUrl},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?",
                    new Object[]{email},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getByUsername(String username) {

        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE username = ?",
                    new Object[]{username},
                    new UserMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public User getById(String id) {

        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = UUID(?)",
                    new Object[]{id},
                    new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User save(User user) {
        jdbcTemplate.update("INSERT INTO users (user_id, username, email, password, is_activated," +
                        "is_verified, is_online, activation_url, date_acc_creation, last_time_online) VALUES (?,?,?,?,?,?,?,?,?,?)",
                UUID.randomUUID(), user.getUsername(), user.getEmail(), user.getPassword(), user.isActivated(),
                user.isVerified(), user.isOnline(), user.getActivationUrl(), user.getCreationDate(), user.getCreationDate());

        return getByEmail(user.getEmail());

    }

    @Override
    public void deleteById(String id) {
        jdbcTemplate.update("DELETE FROM users where user_id = UUID(?)", id);
    }


    @Override
    public void update(User user) {
        jdbcTemplate.update("UPDATE users SET username = ?, email = ?, password= ?, is_activated = ?, is_verified = ?," +
                        "is_online = ?, last_time_online = ?, image = ?, about_me = ?, recovery_url = ?, recovery_sent_time = ?" +
                        "WHERE user_id = UUID(?)",
                user.getUsername(), user.getEmail(), user.getPassword(), user.isActivated(), user.isVerified(),
                user.isOnline(), user.getLastTimeOnline(), user.getImage(), user.getAboutMe(), user.getRecoveryUrl(),
                user.getRecoverySentTime(), user.getId());

    }
}
