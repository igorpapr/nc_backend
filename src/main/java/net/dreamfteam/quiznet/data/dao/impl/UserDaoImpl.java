package net.dreamfteam.quiznet.data.dao.impl;


import net.dreamfteam.quiznet.data.dao.UserDao;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.data.rowmappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


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
    public User getByHashedId(String hashedId) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users where md5(id) = ?",
                    new Object[]{hashedId},
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
    public User getById(Long id) {

        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?",
                    new Object[]{id},
                    new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User save(User user) {
        jdbcTemplate.update("INSERT INTO users (username, email, password, activated) VALUES (?,?,?,?)",
                user.getUsername(), user.getEmail(), user.getPassword(), user.isActivated());

        return getByEmail(user.getEmail());

    }

    @Override
    public void deleteById(Long id) {

    }


    @Override
    public void update(User user) {
        jdbcTemplate.update("UPDATE users SET username = ?, email = ?," +
                        "password= ?, activated = ?, creation_date = ? WHERE id = ?",
                user.getUsername(), user.getEmail(), user.getPassword(),
                user.isActivated(), user.getCreationDate(), user.getId());

    }
}
