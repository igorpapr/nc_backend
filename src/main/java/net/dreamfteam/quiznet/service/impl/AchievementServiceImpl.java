package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.dao.AchievementDao;
import net.dreamfteam.quiznet.data.dao.GameSessionDao;
import net.dreamfteam.quiznet.data.entities.GameSession;
import net.dreamfteam.quiznet.data.entities.UserAchievement;
import net.dreamfteam.quiznet.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementServiceImpl implements AchievementService {

	private final GameSessionDao gameSessionDao;
	private final AchievementDao achievementDao;

	@Autowired
	public AchievementServiceImpl(GameSessionDao gameSessionDao, AchievementDao achievementDao) {
		this.gameSessionDao = gameSessionDao;
		this.achievementDao = achievementDao;
	}

	@Override
	public void checkFirstGameOfUserAchievement(String sessionId) {
		GameSession gameSession = gameSessionDao.getById(sessionId);
		String userId = gameSession.getUserId();
		if (userId != null){
			int numberOfSessions = gameSessionDao.getNumberOfSessionsOfUser(userId);
			if (numberOfSessions == 1){
				int resAssigning = achievementDao.assignAchievement(userId, Constants.ACHIEVEMENT_FIRST_GAME_ID);
				if (resAssigning > 0){
					//add to notifications and activities
				}
			}
		}
	}

	@Override
	public void checkPlayedTenOfDifferentQuizzes(String sessionId) {

	}

	@Override
	public void checkPlayedTenOfDifferentQuizzesOfCategory(String sessionId) {

	}

	@Override
	public List<UserAchievement> getUserAchievements(String userId) {
		return achievementDao.getUserAchievements(userId);
	}
}
