package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.Notification;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationMapper implements RowMapper<Notification> {
    @Override
    public Notification mapRow(ResultSet resultSet, int i) throws SQLException {
        return Notification.builder()
                .content(resultSet.getString("content"))
                .link(resultSet.getString("link"))
                .id(resultSet.getString("notif_id"))
                .date(resultSet.getDate("date_time"))
                .seen(resultSet.getBoolean("seen"))
                .userId(resultSet.getString("user_id"))
                .build();
    }
}
