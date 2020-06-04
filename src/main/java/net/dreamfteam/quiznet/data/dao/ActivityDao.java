package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.FriendsActivity;
import net.dreamfteam.quiznet.web.dto.DtoActivity;

import java.util.List;

public interface ActivityDao {

	List<FriendsActivity> getFriendsActivitiesListByPage(String userId, int startIndex, int amount);

	void addActivity(DtoActivity activity);

	int getFriendsActivitiesTotalSize(String userId);
}
