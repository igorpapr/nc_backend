package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.UserAchievement;

import java.util.List;

public interface AchievementService {

	void checkAftergameAchievements(String gameId);

	void checkQuizCreationAchievements(String userId);

    List<UserAchievement> getUserAchievements(String userId);

    List<UserAchievement> getUserAchievementsLastWeek();

	void checkOnStartGameAchievements(String gameId);

	Integer getUserAchievementsAmount(String userId);

}
