package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.dao.AchievementDao;
import net.dreamfteam.quiznet.data.dao.GameDao;
import net.dreamfteam.quiznet.data.dao.GameSessionDao;
import net.dreamfteam.quiznet.data.dao.QuizDao;
import net.dreamfteam.quiznet.data.entities.*;
import net.dreamfteam.quiznet.service.AchievementService;
import net.dreamfteam.quiznet.service.ActivitiesService;
import net.dreamfteam.quiznet.web.dto.DtoActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AchievementServiceImpl implements AchievementService {

	private final GameSessionDao gameSessionDao;
	private final AchievementDao achievementDao;
	private final QuizDao quizDao;
	private final GameDao gameDao;
	private final ActivitiesService activitiesService;

	@Autowired
	public AchievementServiceImpl(GameSessionDao gameSessionDao, AchievementDao achievementDao,
	                              QuizDao quizDao, GameDao gameDao, ActivitiesService activitiesService) {
		this.gameSessionDao = gameSessionDao;
		this.achievementDao = achievementDao;
		this.quizDao = quizDao;
		this.gameDao = gameDao;
		this.activitiesService = activitiesService;
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
			checkPlayedTenOfDifferentQuizzesOfCategory(userId, gameSession.getGameId());

		}
	}

	@Override
	public void checkQuizCreationAchievements(String userId) {
		int amountCreatedValidated = quizDao.getAmountSuccessCreated(userId);

		if(amountCreatedValidated == 1){
			addAchievementForUser(userId, Constants.ACHIEVEMENT_SANDBOX_ID, false);
		} else if (amountCreatedValidated == 10){
			addAchievementForUser(userId, Constants.ACHIEVEMENT_SPECIALIST_CREATOR_ID, false);
		}

	}



	@Override
	public List<UserAchievement> getUserAchievements(String userId) {
		return achievementDao.getUserAchievements(userId);
	}

	@Override
	public void checkOnStartGameAchievements(String gameId) {
		checkPlayedAmountOfGamesCreatedByUser(gameId);
	}

	private void checkFirstGameOfUserAchievement(String userId) {
		int numberOfSessions = gameSessionDao.getNumberOfSessionsOfUser(userId);
		if (numberOfSessions == 1){
			addAchievementForUser(userId,Constants.ACHIEVEMENT_FIRST_BLOOD_ID, false);
		}
	}

	private void checkPlayedAmountOfDifferentQuizzes(String userId) {
		int amountOfQuizzesPlayed = gameSessionDao.getNumberOfQuizzesPlayedByUser(userId);
		if (amountOfQuizzesPlayed == 10){
			addAchievementForUser(userId, Constants.ACHIEVEMENT_FRESHMAN_ID, false);
		} else if (amountOfQuizzesPlayed == 25){
			addAchievementForUser(userId, Constants.ACHIEVEMENT_CASUAL_ID, false);
		} else if (amountOfQuizzesPlayed == 50){
			addAchievementForUser(userId, Constants.ACHIEVEMENT_EXPERT_ID, false);
		}
	}

	//Repeatable achievement
	private void checkPlayedTenOfDifferentQuizzesOfCategory(String userId, String gameId) {
		UserCategoryAchievementInfo info = gameDao.getUserGamesInCategoryInfo(userId, gameId);
		if (info != null){
			int amount = info.getAmountPlayed();
			if(amount > 0 && ((amount % 10) == 0)){ //works on every tenth game
				String catTitle = info.getCategoryTitle();
				if (catTitle.equals("Geography")){
					addAchievementForUser(userId, Constants.ACHIEVEMENT_GEOGRAPHY_CATEGORY_ID,true);
				} else if (catTitle.equals("Ukraine")){
					addAchievementForUser(userId, Constants.ACHIEVEMENT_UKRAINE_CATEGORY_ID, true);
				} else if (catTitle.equals("History")){
					addAchievementForUser(userId, Constants.ACHIEVEMENT_HISTORY_CATEGORY_ID, true);
				} else if (catTitle.equals("Science")){
					addAchievementForUser(userId, Constants.ACHIEVEMENT_SCIENCE_CATEGORY_ID, true);
				} else if (catTitle.equals("Other")){
					addAchievementForUser(userId, Constants.ACHIEVEMENT_OTHERS_CATEGORY_ID, true);
				}
			}
		}
	}


	private void checkPlayedAmountOfGamesCreatedByUser(String gameId){
		QuizCreatorFullStatistics stats = gameDao.getAmountOfPlayedGamesCreatedByCreatorOfGame(gameId);
		if (stats != null){
			int amount = stats.getAmountGamesPlayedAllQuizzes();
			String creatorId = stats.getCreatorId();
			if (amount == 15){
				addAchievementForUser(creatorId, Constants.ACHIEVEMENT_POPULAR_CREATOR_ID, false);
			} else if (amount == 30){
				addAchievementForUser(creatorId, Constants.ACHIEVEMENT_EXTREMELY_POPULAR_ID, false);
			} else if (amount == 50){
				addAchievementForUser(creatorId, Constants.ACHIEVEMENT_MASTERPIECE_CREATOR_ID, false);
			}
		}
	}

	private void addAchievementForUser(String userId, int achievementId, boolean repeatable){
		int resAssigning = 0;
		if (repeatable){//For future use
			resAssigning = achievementDao.assignAchievementRepeating(userId, achievementId);
		} else {
			resAssigning = achievementDao.assignAchievement(userId, achievementId);
		}
		if (resAssigning > 0){
			//adding to notifications and activities
			UserAchievement userAchievement = achievementDao.getUserAchievementByIds(userId, achievementId);
			if(userAchievement != null){
				DtoActivity activity = DtoActivity.builder()
						.activityType(ActivityType.ACHIEVEMENTS_RELATED)
						.userId(userId)
						.build();
				if(userAchievement.getTimesGained() == 1){
					activity.setContent("Got achievement: " + userAchievement.getTitle() + "!");
				}else{
					activity.setContent("Got achievement: " + userAchievement.getTitle() +
							" " + userAchievement.getTimesGained()+ " times!");
				}
				activitiesService.addActivityForUser(activity);
			}
		}
	}

}
