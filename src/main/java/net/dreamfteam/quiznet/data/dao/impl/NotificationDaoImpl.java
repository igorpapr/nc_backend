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
import java.util.UUID;

@Repository
public class NotificationDaoImpl implements NotificationDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NotificationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Notification> getUnseenByUserId(String userId) {
        return jdbcTemplate.query(
                "SELECT CASE value WHEN 'uk' THEN content_uk WHEN 'en' THEN content END AS content, " +
                        "notif_id, n.user_id, date_time, seen, link " +
                        "FROM user_notifications n INNER JOIN user_settings s ON n.user_id = s.user_id " +
                        "WHERE seen = false AND n.user_id = UUID(?) " +
                        "AND setting_id = 'e8449301-6d6f-4376-8247-b7d1f8df6416'",
                new Object[]{userId}, new NotificationMapper());
    }

    @Override
    public Notification getById(String notifId) {
        return jdbcTemplate.queryForObject(
                "SELECT CASE value WHEN 'uk' THEN content_uk WHEN 'en' THEN content END AS content, " +
                        "notif_id, n.user_id, date_time, seen, link " +
                        "FROM user_notifications n INNER JOIN user_settings s ON n.user_id = s.user_id " +
                        "WHERE notif_id = UUID(?) AND setting_id = 'e8449301-6d6f-4376-8247-b7d1f8df6416';",
                new Object[]{notifId}, new NotificationMapper());
    }

    @Override
    public String insert(Notification notification) {
        System.out.println(notification.getLink());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO user_notifications " +
                    "(content, user_id, content_uk, link) " +
                    "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, notification.getContent());
            ps.setObject(2, UUID.fromString(notification.getUserId()));
            ps.setString(3, notification.getContentUk());
            ps.setString(4,notification.getLink());
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
