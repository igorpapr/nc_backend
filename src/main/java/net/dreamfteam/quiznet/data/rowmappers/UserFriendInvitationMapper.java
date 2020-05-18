package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.UserFriendInvitation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserFriendInvitationMapper implements RowMapper<UserFriendInvitation> {

	@Override
	public UserFriendInvitation mapRow(ResultSet resultSet, int i) throws SQLException {
		return UserFriendInvitation.builder()
				.id(resultSet.getString("user_id"))
				.username(resultSet.getString("username"))
				.invitationDatetime(resultSet.getTimestamp("invite_datetime"))
				.imageContent(resultSet.getBytes("image_content"))
				.build();
	}
}
