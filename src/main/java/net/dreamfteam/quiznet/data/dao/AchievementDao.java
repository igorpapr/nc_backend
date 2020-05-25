package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.UserAchievement;

import java.util.List;

public interface AchievementDao {

    int assignAchievementRepeating(String userId, int achievementId);

    int assignAchievement(String userId, int achievementId);

    List<UserAchievement> getUserAchievements(String userId);

    List<UserAchievement> getUserAchievementsLastWeek(String userId);

    UserAchievement getUserAchievementByIds(String userId, int achievementId);

    Integer getUserAchievementsAmount(String userId);

}
