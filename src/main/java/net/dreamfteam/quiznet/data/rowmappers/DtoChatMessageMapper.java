package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.web.dto.DtoChatMessage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DtoChatMessageMapper implements RowMapper<DtoChatMessage> {
    @Override
    public DtoChatMessage mapRow(ResultSet resultSet, int i) throws SQLException {
        return DtoChatMessage.builder()
                .authorId(resultSet.getString("user_id"))
                .authorUsername(resultSet.getString("username"))
                .content(resultSet.getString("content"))
                .sentDate(resultSet.getTimestamp("datetime_sent"))
                .build();
    }
}
