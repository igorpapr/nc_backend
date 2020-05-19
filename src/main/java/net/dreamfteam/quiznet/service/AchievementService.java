package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.UserAchievement;

import java.util.List;

public interface AchievementService {

    void checkFirstGameOfUserAchievement(String sessionId);

    void checkPlayedTenOfDifferentQuizzes(String sessionId);

    void checkPlayedTenOfDifferentQuizzesOfCategory(String sessionId);

    List<UserAchievement> getUserAchievements(String userId);

    List<UserAchievement> getUserAchievementsLastWeek();

}
