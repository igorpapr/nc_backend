package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.UserAchievement;
import net.dreamfteam.quiznet.web.dto.DtoUserAchievement;

import java.util.List;

public interface AchievementDao {

    int assignAchievementRepeating(String userId, int achievementId);

    int assignAchievement(String userId, int achievementId);

    List<UserAchievement> getUserAchievements(String userId);

    List<UserAchievement> getUserAchievementsLastWeek(String userId);

    DtoUserAchievement getUserAchievementByIds(String userId, int achievementId);

    Integer getUserAchievementsAmount(String userId);

}
