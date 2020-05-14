package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.dao.AchievementDao;
import net.dreamfteam.quiznet.data.dao.GameSessionDao;
import net.dreamfteam.quiznet.data.dao.QuizDao;
import net.dreamfteam.quiznet.data.entities.GameSession;
import net.dreamfteam.quiznet.data.entities.UserAchievement;
import net.dreamfteam.quiznet.data.entities.UserAchievementInfo;
import net.dreamfteam.quiznet.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementServiceImpl implements AchievementService {

	private final GameSessionDao gameSessionDao;
	private final AchievementDao achievementDao;
	private final QuizDao quizDao;

	@Autowired
	public AchievementServiceImpl(GameSessionDao gameSessionDao, AchievementDao achievementDao, QuizDao quizDao) {
		this.gameSessionDao = gameSessionDao;
		this.achievementDao = achievementDao;
		this.quizDao = quizDao;
	}

	@Override
	public void checkAftergameAchievements(String sessionId) {
		GameSession gameSession = gameSessionDao.getById(sessionId);
		String userId = gameSession.getUserId();
		if (userId != null) {
			//Non-repeatable achievements
			checkFirstGameOfUserAchievement(userId);
			checkPlayedAmountOfDifferentQuizzes(userId);

			//Repeatable achievements
			UserAchievementInfo dto = achievementDao.getCategoryAchievementInfo(userId, gameSession.getGameId());
			checkPlayedTenOfDifferentQuizzesOfCategory(userId, dto);

		}
	}

	@Override
	public void checkQuizCreationAchievements(String userId) {
		int amountCreatedValidated = quizDao.getAmountSuccessCreated(userId);

		if(amountCreatedValidated >= 1 && amountCreatedValidated <= 10){
			addAchievementForUser(userId, Constants.ACHIEVEMENT_SANDBOX_ID, false);
		} else if (amountCreatedValidated >= 10){
			addAchievementForUser(userId, Constants.ACHIEVEMENT_SPECIALIST_CREATOR_ID, false);
		}

	}

	@Override
	public List<UserAchievement> getUserAchievements(String userId) {
		return achievementDao.getUserAchievements(userId);
	}

	private void checkFirstGameOfUserAchievement(String userId) {
		int numberOfSessions = gameSessionDao.getNumberOfSessionsOfUser(userId);
		if (numberOfSessions == 1){
			addAchievementForUser(userId,Constants.ACHIEVEMENT_FIRST_BLOOD_ID, false);
		}
	}

	private void checkPlayedAmountOfDifferentQuizzes(String userId) {
		int amountOfQuizzesPlayed = gameSessionDao.getNumberOfQuizzesPlayedByUser(userId);
		//10 or more
		if (amountOfQuizzesPlayed >= 10 && amountOfQuizzesPlayed < 25){
			addAchievementForUser(userId, Constants.ACHIEVEMENT_FRESHMAN_ID, false);
		//25 or more
		} else if (amountOfQuizzesPlayed >= 25 && amountOfQuizzesPlayed < 50){
			addAchievementForUser(userId, Constants.ACHIEVEMENT_CASUAL_ID,false);
		//50 or more
		} else if (amountOfQuizzesPlayed >= 50){
			addAchievementForUser(userId, Constants.ACHIEVEMENT_EXPERT_ID,false);
		}
	}

	private void checkPlayedTenOfDifferentQuizzesOfCategory(String userId, UserAchievementInfo info) {
	}


	private void addAchievementForUser(String userId, int achievementId, boolean repeatable){
		int resAssigning = 0;
		if (repeatable){
			resAssigning = achievementDao.assignAchievementRepeating(userId, achievementId);
		} else {
			resAssigning = achievementDao.assignAchievement(userId, achievementId);
		}
		if (resAssigning > 0){
			//add to notifications and activities
		}
	}
}
