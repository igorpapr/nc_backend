package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.dao.ActivityDao;
import net.dreamfteam.quiznet.data.entities.FriendsActivity;
import net.dreamfteam.quiznet.data.rowmappers.ActivityMapper;
import net.dreamfteam.quiznet.web.dto.DtoActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ActivityDaoImpl implements ActivityDao {

	JdbcTemplate jdbcTemplate;

	@Autowired
	public ActivityDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<FriendsActivity> getFriendsActivitiesList(String userId) {
		try{
			return jdbcTemplate
					.query("SELECT CASE (SELECT value " +//selecting language of user
									"FROM user_settings " +
									"WHERE user_id = uuid(?) " +
									"AND setting_id = uuid(?)) " +
									"WHEN 'uk' THEN content_uk WHEN 'en' THEN content END AS content, " +
									"activity_id, datetime, ua.user_id, u.username, u.image AS image_content " +
								"FROM user_activities ua INNER JOIN users u ON ua.user_id = u.user_id " +
								"INNER JOIN activity_types at1 ON ua.type_id = at1.type_id " +
								"WHERE ua.user_id IN " + //selecting friends
													"(SELECT f.friend_id AS id " +
													"FROM friends f " +
													"WHERE f.parent_id = uuid(?) " +
													"AND f.accepted_datetime IS NOT NULL " +
													"UNION " +
													"SELECT f1.parent_id AS id " +
													"FROM friends f1 " +
													"WHERE f1.friend_id = uuid(?) " +
													"AND f1.accepted_datetime IS NOT NULL) " +
								//filtering by settings
								"AND ua.type_id IN (SELECT activity_type_id " +
													"FROM settings s INNER JOIN user_settings us " +
													"ON s.setting_id = us.setting_id " +
													"WHERE us.user_id = uuid(?) " +
													"AND value = 'true' " +
													"AND activity_type_id IS NOT NULL) " +
								"ORDER BY datetime DESC;",
					new Object[]{userId, Constants.SETTING_LANG_ID, userId, userId, userId}, new ActivityMapper());
		}catch (EmptyResultDataAccessException e){
			return null;
		}
	}

	@Override
	public void addActivity(DtoActivity activity) {
		try{
			jdbcTemplate.update("INSERT INTO user_activities (content, content_uk, type_id, user_id) " +
									"VALUES (?, ?, uuid(?))", activity.getContent(),
					activity.getContentUk(),
					activity.getActivityType().ordinal() + 1, activity.getUserId());
		}catch (DataAccessException e){
			System.err.println("Couldn't add new activity for user "+ activity.getUserId() +
					".\n Error: " + e.getMessage());
		}
	}
}
