package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.FriendsActivity;
import net.dreamfteam.quiznet.web.dto.DtoActivity;

import java.util.List;

public interface ActivityDao {

	List<FriendsActivity> getFriendsActivitiesList(String userId);

	void addActivity(DtoActivity activity);
}
