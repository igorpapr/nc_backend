package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.User;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {

        User user = User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .username(resultSet.getString("username"))
                .activated(resultSet.getBoolean("activated"))
                .creationDate(resultSet.getDate("creation_date"))
                .build();

        return user;
    }
}
