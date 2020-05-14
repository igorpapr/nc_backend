package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.FriendsActivity;

import java.util.List;

public interface ActivitiesService {

	List<FriendsActivity> getFriendsActivities(String userId);

}
