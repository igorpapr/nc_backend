package net.dreamfteam.quiznet.data.rowmappers;


import net.dreamfteam.quiznet.web.dto.DtoChatUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DtoChatUserMapper implements RowMapper<DtoChatUser> {
    @Override
    public DtoChatUser mapRow(ResultSet resultSet, int i) throws SQLException {
        return DtoChatUser.builder()
                .id(resultSet.getString("user_id"))
                .username(resultSet.getString("username"))
                .image(resultSet.getBytes("image"))
                .joinedToChatDate(resultSet.getTimestamp("datetime_joined"))
                .build();
    }
}
