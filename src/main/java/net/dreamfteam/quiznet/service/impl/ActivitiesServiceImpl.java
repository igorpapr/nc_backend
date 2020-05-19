package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.dao.ActivityDao;
import net.dreamfteam.quiznet.data.entities.FriendsActivity;
import net.dreamfteam.quiznet.service.ActivitiesService;
import net.dreamfteam.quiznet.web.dto.DtoActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivitiesServiceImpl implements ActivitiesService {

	private final ActivityDao activityDao;

	@Autowired
	public ActivitiesServiceImpl(ActivityDao activityDao) {
		this.activityDao = activityDao;
	}

	@Override
	public List<FriendsActivity> getFriendsActivities(String userId) {
		return activityDao.getFriendsActivitiesList(userId);
	}

	@Override
	public void addActivityForUser(DtoActivity dto) {
		activityDao.addActivity(dto);
	}
}
