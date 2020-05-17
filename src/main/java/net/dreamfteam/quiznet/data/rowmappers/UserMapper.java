package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {

        User user = User.builder()
                .id(resultSet.getString("user_id"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .username(resultSet.getString("username"))
                .lastTimeOnline(resultSet.getTimestamp("last_time_online"))
                .aboutMe(resultSet.getString("about_me"))
                .online(resultSet.getBoolean("is_online"))
                .activated(resultSet.getBoolean("is_activated"))
                .verified(resultSet.getBoolean("is_verified"))
                .creationDate(resultSet.getTimestamp("date_acc_creation"))
                .activationUrl(resultSet.getString("activation_url"))
                .recoveryUrl(resultSet.getString("recovery_url"))
                .recoverySentTime(resultSet.getTimestamp("recovery_sent_time"))
                .role(Role.valueOf(resultSet.getString("role")))
                .image(resultSet.getBytes("image"))
                .build();

        return user;
    }
}
