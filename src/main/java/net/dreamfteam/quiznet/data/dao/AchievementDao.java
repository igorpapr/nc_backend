package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.UserAchievement;
import net.dreamfteam.quiznet.data.entities.UserAchievementInfo;

import java.util.List;

public interface AchievementDao {

	int assignAchievementRepeating(String userId, int achievementId);

	int assignAchievement(String userId, int achievementId);

	List<UserAchievement> getUserAchievements(String userId);

	UserAchievementInfo getCategoryAchievementInfo(String userId, String gameId);

}
