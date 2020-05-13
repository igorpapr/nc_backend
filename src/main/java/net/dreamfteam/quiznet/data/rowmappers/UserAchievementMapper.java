package net.dreamfteam.quiznet.data.rowmappers;

import net.dreamfteam.quiznet.data.entities.Role;
import net.dreamfteam.quiznet.data.entities.Settings;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.data.entities.UserAchievement;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAchievementMapper implements RowMapper<UserAchievement> {
	@Override
	public UserAchievement mapRow(ResultSet resultSet, int i) throws SQLException {

		UserAchievement userAchievement = UserAchievement.builder()
				.achievementId(resultSet.getInt("achievement_id"))
				.title(resultSet.getString("title"))
				.description(resultSet.getString("description"))
				.categoryId(resultSet.getString("category_id"))
				.categoryTitle(resultSet.getString("category_title"))
				.datetimeGained(resultSet.getTimestamp("datetime_gained"))
				.imageContent(resultSet.getBytes("image_content"))
				.timesGained(resultSet.getInt("times_gained"))
				.build();

		return userAchievement;
	}
}
