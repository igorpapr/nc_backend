package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.UserDao;
import net.dreamfteam.quiznet.data.entities.GameHistory;
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
    public int userRating(String userId) {
        try {
            return jdbcTemplate.queryForObject("SELECT SUM(gained_rating) FROM user_activities  WHERE user_id = UUID(?)",
                    new Object[]{userId},
                    Integer.class);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public List<GameHistory> getUserGameHistory(String userId) {
        try {
        return jdbcTemplate.query("SELECT game_session_id, q.quiz_id, g.game_id, score, is_winner, datetime_start, round_duration, q.title quiz_title, i.image image_content FROM ((users_games ug INNER JOIN games g ON ug.game_id = g.game_id) INNER JOIN quizzes q ON q.quiz_id = g.quiz_id) LEFT JOIN images i ON i. WHERE user_id = uuid(?)", new Object[]{userId},
                (rs, i) -> GameHistory.builder()
                        .gameSessionId(rs.getString("game_session_id"))
                        .quizId(rs.getString("quiz_id"))
                        .gameId(rs.getString("game_id"))
                        .score(rs.getInt("score"))
                        .winner(rs.getBoolean("is_winner"))
                        .datetimeStart(rs.getDate("datetime_start"))
                        .roundDuration(rs.getInt("round_duration"))
                        .quizTitle(rs.getString("quiz_title"))
                        .quizImage(rs.getBytes("image_content"))
                        .build()
        );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
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
