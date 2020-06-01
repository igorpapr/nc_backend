package net.dreamfteam.quiznet.data.dao.impl;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.configs.constants.SqlConstants;
import net.dreamfteam.quiznet.data.dao.ActivityDao;
import net.dreamfteam.quiznet.data.entities.FriendsActivity;
import net.dreamfteam.quiznet.data.rowmappers.FriendsActivityMapper;
import net.dreamfteam.quiznet.web.dto.DtoActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
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
					.query(SqlConstants.ACTIVITY_GET_FRIENDS_ACTIVITIES_LIST,
					new Object[]{userId, Constants.SETTING_LANG_ID, userId, userId, userId},
					new FriendsActivityMapper());
		}catch (EmptyResultDataAccessException | NullPointerException e){
			log.error("Couldn't get friends activities list.\n Exception: " + e.getMessage());
			return null;
		}
	}

	@Override
	public void addActivity(DtoActivity activity) {
		try{
			jdbcTemplate.update(SqlConstants.ACTIVITY_ADD, activity.getContent(),
					activity.getContentUk(),
					activity.getActivityType().ordinal() + 1, activity.getUserId(),
					activity.getLinkInfo());
		}catch (DataAccessException | NullPointerException e){
			log.error("Couldn't add new activity for the given user.\n Exception: " + e.getMessage());
		}
	}
}
