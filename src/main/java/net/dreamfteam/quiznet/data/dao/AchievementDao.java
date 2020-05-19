package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.UserAchievement;
import net.dreamfteam.quiznet.data.entities.UserCategoryAchievementInfo;

import java.util.List;

public interface AchievementDao {

	int assignAchievementRepeating(String userId, int achievementId);

	int assignAchievement(String userId, int achievementId);

	List<UserAchievement> getUserAchievements(String userId);

	UserAchievement getUserAchievementByIds(String userId, int achievementId);
}
