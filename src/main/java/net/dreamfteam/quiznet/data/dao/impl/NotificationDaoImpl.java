package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.configs.constants.SqlConstants;
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
        return jdbcTemplate.query(SqlConstants.NOTIFICATIONS_GET_UNSEEN_BY_USER_ID,
                new Object[]{userId, Constants.SETTING_LANG_ID}, new NotificationMapper());
    }

    @Override
    public Notification getById(String notifId) {
        return jdbcTemplate.queryForObject(SqlConstants.NOTIFICATIONS_GET_BY_ID,
                new Object[]{notifId, Constants.SETTING_LANG_ID}, new NotificationMapper());
    }

    @Override
    public String insert(Notification notification) {
        System.out.println(notification.getLink());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SqlConstants.NOTIFICATIONS_INSERT,
                    Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, notification.getContent());
            ps.setObject(2, UUID.fromString(notification.getUserId()));
            ps.setString(3, notification.getContentUk());
            ps.setString(4,notification.getLink());
            ps.setInt(5, notification.getTypeId());
            return ps;
        }, keyHolder);

        return String.valueOf(keyHolder.getKeys().get("notif_id"));
    }

    @Override
    public void updateSeen(String userId) {
        jdbcTemplate.update(SqlConstants.NOTIFICATIONS_UPDATE_SEEN, userId);
    }

    @Override
    public void deleteHalfYear() {
        jdbcTemplate.update(SqlConstants.NOTIFICATIONS_DELETE_HALF_YEAR);
    }
}
