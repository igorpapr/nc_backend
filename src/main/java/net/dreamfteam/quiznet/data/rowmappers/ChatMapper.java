package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.Chat;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatMapper implements RowMapper<Chat> {
    @Override
    public Chat mapRow(ResultSet resultSet, int i) throws SQLException {
        return Chat.builder()
                .id(resultSet.getString("chat_id"))
                .isCreator(resultSet.getBoolean("is_creator"))
                .joinedDate(resultSet.getTimestamp("datetime_joined"))
                .isPersonal(resultSet.getBoolean("is_personal"))
                .image(resultSet.getBytes("image"))
                .title(resultSet.getString("title"))
                .build();

    }
}
