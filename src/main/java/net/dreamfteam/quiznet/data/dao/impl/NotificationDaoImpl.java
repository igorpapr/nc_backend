package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.NotificationDao;
import net.dreamfteam.quiznet.data.entities.Notification;
import net.dreamfteam.quiznet.data.rowmappers.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class NotificationDaoImpl implements NotificationDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NotificationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Notification> getUnseenByUserId(String userId) {
        List<Notification> res = jdbcTemplate.query(
                "SELECT * " +
                        "FROM user_notifications " +
                        "WHERE seen = false", new NotificationMapper());
        return res;
    }

    @Override
    public Notification getById(String notifId) {
        return jdbcTemplate.queryForObject(
                "SELECT * " +
                        "FROM user_notifications " +
                        "WHERE notif_id = UUID(?);", new Object[]{notifId}, new NotificationMapper());
    }

    @Override
    public String insert(Notification notification) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO user_notifications " +
                    "(content, user_id) " +
                    "VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, notification.getContent());
            ps.setObject(2, java.util.UUID.fromString(notification.getUserId()));
            return ps;
        }, keyHolder);

        return String.valueOf(keyHolder.getKeys().get("notif_id"));
    }

    @Override
    public void updateSeen(String userId) {
        jdbcTemplate.update("UPDATE user_notifications " +
                "SET seen = true " +
                "WHERE user_id = UUID(?)",
                userId);
    }

    @Override
    public void deleteHalfYear() {
        jdbcTemplate.update("DELETE FROM user_notifications " +
                "WHERE date_time < CURRENT_TIMESTAMP  - interval '181' day ");
    }
}
