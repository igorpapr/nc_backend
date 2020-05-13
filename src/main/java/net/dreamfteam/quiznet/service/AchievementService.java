package net.dreamfteam.quiznet.service;

public interface AchievementService {

	void checkFirstGameOfUserAchievement(String sessionId);

	void checkPlayedTenOfDifferentQuizzes(String sessionId);

	void checkPlayedTenOfDifferentQuizzesOfCategory(String sessionId);



}
