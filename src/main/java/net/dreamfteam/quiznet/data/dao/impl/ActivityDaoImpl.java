package net.dreamfteam.quiznet.data.dao.impl;

import net.dreamfteam.quiznet.data.dao.ActivityDao;
import net.dreamfteam.quiznet.data.entities.FriendsActivity;
import net.dreamfteam.quiznet.data.rowmappers.ActivityMapper;
import net.dreamfteam.quiznet.data.rowmappers.UserFriendInvitationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
			return jdbcTemplate.query("SELECT activity_id, content, datetime, ua.user_id, u.username, i.image AS image_content " +
											"FROM user_activities ua INNER JOIN users u ON ua.user_id = u.user_id " +
											"LEFT JOIN images i ON uuid(u.image) = i.image_id " +
											"WHERE ua.user_id IN " +
																"(SELECT f.friend_id AS id " +
																"FROM friends f " +
																"WHERE f.parent_id = uuid(?) " +
																"AND f.accepted_datetime IS NOT NULL " +
																"UNION " +
																"SELECT f1.parent_id AS id " +
																"FROM friends f1 " +
																"WHERE f1.friend_id = uuid(?) " +
																"AND f1.accepted_datetime IS NOT NULL)",
					new Object[]{userId, userId}, new ActivityMapper());
		}catch (EmptyResultDataAccessException e){
			return null;
		}
	}

	@Override
	public void addActivity() {

	}
}
