package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.FriendsActivity;
import net.dreamfteam.quiznet.web.dto.DtoActivity;

import java.util.List;

public interface ActivitiesService {

	List<FriendsActivity> getFriendsActivitiesPage(String userId, int startIndex, int amount);

	void addActivityForUser(DtoActivity dto);

	void addWinnersActivities(String gameId);

	int getFriendsActivitiesTotalSize(String userId);
}
