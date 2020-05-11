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
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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
    public void insert(Notification notification) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO user_notifications " +
                    "(content, user_id, image) " +
                    "VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, notification.getContent());
            ps.setObject(2, java.util.UUID.fromString(notification.getUserId()));
            ps.setBytes(3, notification.getImage());
            return ps;
        });
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
