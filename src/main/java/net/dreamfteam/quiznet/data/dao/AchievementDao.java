package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.UserAchievement;

import java.util.List;

public interface AchievementDao {

	int assignAchievement(String userId, int achievementId);

	List<UserAchievement> getUserAchievements(String userId);

}
