package net.dreamfteam.quiznet.data.dao.impl;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.configs.constants.SqlConstants;
import net.dreamfteam.quiznet.data.dao.NotificationDao;
import net.dreamfteam.quiznet.data.entities.Notification;
import net.dreamfteam.quiznet.data.rowmappers.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Repository
public class NotificationDaoImpl implements NotificationDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NotificationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Notification> getUnseenByUserId(String userId) {
        try{
            List<Notification> result = jdbcTemplate.query(SqlConstants.NOTIFICATIONS_GET_UNSEEN_BY_USER_ID,
                    new Object[]{userId, Constants.SETTING_LANG_ID}, new NotificationMapper());
            log.info("Get unseen notifications for user: "+userId);
            return result;
        }catch (Exception e){
            log.error("Error while getting unseen notifications for user: "+userId+"\n"+e.getMessage());
            return new ArrayList<>();
        }

    }

    @Override
    public Notification getById(String notifId) {
        try{
            Notification result = jdbcTemplate.queryForObject(SqlConstants.NOTIFICATIONS_GET_BY_ID,
                    new Object[]{notifId, Constants.SETTING_LANG_ID}, new NotificationMapper());
            log.info("Get notification by id: "+notifId);
            return result;
        }catch (Exception e){
            log.error("Error while getting notification by id: "+notifId+"\n"+e.getMessage());
            return null;
        }

    }

    @Override
    public String insert(Notification notification) {
        try {
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

            log.info("Insert notification: "+notification);

            return String.valueOf(Objects.requireNonNull(keyHolder.getKeys()).get("notif_id"));
        }catch (DataAccessException e){
            log.error("Error while getting notification: "+notification.getContent()+"\n"+e.getMessage());
            return "";
        }

    }

    @Override
    public void updateSeen(String userId) {
        try{
            jdbcTemplate.update(SqlConstants.NOTIFICATIONS_UPDATE_SEEN, userId);
            log.info("Updated seen notifications for user: "+userId);
        }catch (DataAccessException e){
            log.error("Error while update seen notifications for user: "+userId+"\n"+e.getMessage());
        }
    }

    @Override
    public void deleteHalfYear() {
        try {
            jdbcTemplate.update(SqlConstants.NOTIFICATIONS_DELETE_HALF_YEAR);
        } catch (DataAccessException e) {
            log.error("Error while deleting half year notifications\n" + e.getMessage());
        }
    }
}
