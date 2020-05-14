package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.UserAchievement;

import java.util.List;

public interface AchievementService {

	void checkAftergameAchievements(String sessionId);

	void checkQuizCreationAchievements(String userId);

	List<UserAchievement> getUserAchievements(String userId);

	void checkOnStartGameAchievements(String gameId);

}
