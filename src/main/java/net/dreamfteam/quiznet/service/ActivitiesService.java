package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.FriendsActivity;
import net.dreamfteam.quiznet.web.dto.DtoActivity;

import java.util.List;

public interface ActivitiesService {

	List<FriendsActivity> getFriendsActivities(String userId);

	void addActivityForUser(DtoActivity dto);

	void addWinnersActivities(String gameId);
}
