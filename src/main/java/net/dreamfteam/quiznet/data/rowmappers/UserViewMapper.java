package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.UserView;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserViewMapper implements RowMapper<UserView> {

	@Override
	public UserView mapRow(ResultSet resultSet, int i) throws SQLException {

		return UserView.builder()
				.id(resultSet.getString("user_id"))
				.username(resultSet.getString("username"))
				.lastTimeOnline(resultSet.getTimestamp("last_time_online"))
				.imageContent(resultSet.getBytes("image_content"))
				.build();
	}
}
