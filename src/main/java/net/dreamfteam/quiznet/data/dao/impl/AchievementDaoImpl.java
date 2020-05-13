package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.AchievementDao;
import net.dreamfteam.quiznet.data.entities.UserAchievement;
import net.dreamfteam.quiznet.data.rowmappers.UserAchievementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

@Repository
public class AchievementDaoImpl implements AchievementDao {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public AchievementDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int assignAchievement(String userId, int achievementId) {
		try{
			return jdbcTemplate.update(con -> {
				PreparedStatement ps =
						con.prepareStatement("INSERT INTO users_achievements AS t" +
												  "(user_id, achievement_id, datetime_gained) " +
												  "VALUES (?,?,CURRENT_TIMESTAMP) " +
												  "ON CONFLICT (user_id, achievement_id) DO UPDATE " +
												  "SET times_gained = t.times_gained + 1;");
				ps.setObject(1, UUID.fromString(userId));
				ps.setInt(2, achievementId);
				return ps;
			});
		}catch (EmptyResultDataAccessException e){
			return 0;
		}
	}

	@Override
	public List<UserAchievement> getUserAchievements(String userId) {
		try {
			return jdbcTemplate
					.query("SELECT a.achievement_id, a.title, a.description, a.image_content, a.category_id, c.title AS category_title, ua.datetime_gained, ua.times_gained " +
								"FROM achievements a INNER JOIN users_achievements ua ON a.achievement_id = ua.achievement_id " +
								"LEFT JOIN categories c ON a.category_id = c.category_id " +
								"WHERE ua.user_id = uuid(?);",
							new Object[]{userId}, new UserAchievementMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
