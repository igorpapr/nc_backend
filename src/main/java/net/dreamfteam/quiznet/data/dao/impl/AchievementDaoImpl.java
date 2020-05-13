package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.AchievementDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
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
						con.prepareStatement("INSERT INTO users_achievements " +
												  "(user_id, achievement_id, datetime_gained) " +
												  "VALUES (?,?,CURRENT_TIMESTAMP ) " +
												  "ON CONFLICT (user_id, achievement_id) DO UPDATE " +
												  "SET times_gained = times_gained + 1;");
				ps.setObject(1, UUID.fromString(userId));
				ps.setInt(2, achievementId);
				return ps;
			});
		}catch (EmptyResultDataAccessException e){
			return 0;
		}
	}
}
