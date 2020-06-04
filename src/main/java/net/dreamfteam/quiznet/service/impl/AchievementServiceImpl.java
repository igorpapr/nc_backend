package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.dao.AchievementDao;
import net.dreamfteam.quiznet.data.dao.GameDao;
import net.dreamfteam.quiznet.data.dao.GameSessionDao;
import net.dreamfteam.quiznet.data.dao.QuizDao;
import net.dreamfteam.quiznet.data.entities.*;
import net.dreamfteam.quiznet.service.AchievementService;
import net.dreamfteam.quiznet.service.ActivitiesService;
import net.dreamfteam.quiznet.service.NotificationService;
import net.dreamfteam.quiznet.web.dto.DtoActivity;
import net.dreamfteam.quiznet.web.dto.DtoNotification;
import net.dreamfteam.quiznet.web.dto.DtoPlayerSession;
import net.dreamfteam.quiznet.web.dto.DtoUserAchievement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AchievementServiceImpl implements AchievementService {

	private final GameSessionDao gameSessionDao;
	private final AchievementDao achievementDao;
	private final IAuthenticationFacade authenticationFacade;
	private final QuizDao quizDao;
	private final GameDao gameDao;
	private final ActivitiesService activitiesService;
	private final NotificationService notificationService;

	
	@Autowired
	public AchievementServiceImpl(GameSessionDao gameSessionDao, AchievementDao achievementDao,
								  ActivitiesService activitiesService, 
								  QuizDao quizDao, GameDao gameDao,
								  IAuthenticationFacade authenticationFacade,
	                              NotificationService notificationService) {
		this.gameSessionDao = gameSessionDao;
		this.achievementDao = achievementDao;
		this.authenticationFacade = authenticationFacade;
		this.quizDao = quizDao;
		this.gameDao = gameDao;
		this.activitiesService = activitiesService;
		this.notificationService = notificationService;
	}

	@Override
	@Async
	public void checkAftergameAchievements(String gameId) {
		List<DtoPlayerSession> sessionsMaps = gameSessionDao.getSessions(gameId);
		for (DtoPlayerSession session : sessionsMaps) {
			String userId = session.getUser_id();
			if (userId != null) {//checking if the user is not anonymous
				//Non-repeatable achievements
				checkFirstGameOfUserAchievement(userId);
				checkPlayedAmountOfDifferentQuizzes(userId);

				//Repeatable achievements
				checkPlayedTenOfDifferentQuizzesOfCategory(userId, gameId);
			}
		}

	}

	@Override
	@Async
	public void checkQuizCreationAchievements(String userId) {
		int amountCreatedValidated = quizDao.getAmountSuccessCreated(userId);

		if(amountCreatedValidated == 1){
			addAchievementForUser(userId, Constants.ACHIEVEMENT_SANDBOX_ID, false);
		} else if (amountCreatedValidated == 10){
			addAchievementForUser(userId, Constants.ACHIEVEMENT_SPECIALIST_CREATOR_ID, false);
		}
	}

	@Override
	public Integer getUserAchievementsAmount(String userId){
		return achievementDao.getUserAchievementsAmount(userId);
	}

	@Override
	public List<UserAchievement> getUserAchievements(String userId) {
		return achievementDao.getUserAchievements(userId);
	}

	@Override
	public List<UserAchievement> getUserAchievementsLastWeek() {
		return achievementDao.getUserAchievementsLastWeek(authenticationFacade.getUserId());
	}

	@Override
	@Async
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
		switch (amountOfQuizzesPlayed) {
			case 10:
				addAchievementForUser(userId, Constants.ACHIEVEMENT_FRESHMAN_ID, false);
				break;
			case 25:
				addAchievementForUser(userId, Constants.ACHIEVEMENT_CASUAL_ID, false);
				break;
			case 50:
				addAchievementForUser(userId, Constants.ACHIEVEMENT_EXPERT_ID, false);
				break;
		}
	}

	//Repeatable achievement
	private void checkPlayedTenOfDifferentQuizzesOfCategory(String userId, String gameId) {
		List<UserCategoryAchievementInfo> infoList = gameDao.getUserGamesInCategoryInfo(userId, gameId);
		if (infoList != null){
			for(UserCategoryAchievementInfo info : infoList){
				int amount = info.getAmountPlayed();
				if(amount > 0 && ((amount % 10) == 0)){ //works on every tenth game
					int achievementId = info.getAchievementId();
					addAchievementForUser(userId, achievementId,true);
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
		if (repeatable){
			resAssigning = achievementDao.assignAchievementRepeating(userId, achievementId);
		} else {
			resAssigning = achievementDao.assignAchievement(userId, achievementId);
		}
		if (resAssigning > 0){
			DtoUserAchievement userAchievement = achievementDao.getUserAchievementByIds(userId, achievementId);
			if(userAchievement != null){
				DtoActivity activity = DtoActivity.builder()
												  .activityType(ActivityType.ACHIEVEMENTS_RELATED)
												  .userId(userId)
													.linkInfo(userAchievement.getUsername())
												  .build();
				DtoNotification notification = DtoNotification.builder()
						.link(userAchievement.getUsername())  //Link for achievements
						.typeId(3)
						.userId(userId).build();
				if(userAchievement.getTimesGained() == 1){
					activity.setContent("Got the achievement: " + userAchievement.getTitle() + "!");
					activity.setContentUk("Отримав/ла досягнення: " + userAchievement.getTitleUk() + "!");
					notification.setContent("Congratulations! You've got the achievement: "
							+ userAchievement.getTitle() + "!");
					notification.setContentUk("Вітання! Ви отримали досягнення: "
							+ userAchievement.getTitleUk() + "!");
				}else{
					activity.setContent("Got the achievement: " + userAchievement.getTitle() +
							" " + userAchievement.getTimesGained()+ " times!");
					activity.setContentUk("Отримав/ла досягнення: " + userAchievement.getTitleUk() + " "
							+ userAchievement.getTimesGained() + "-ий раз!");
					notification.setContent("Congratulations! You've got the achievement: "
							+ userAchievement.getTitle() +
							" " + userAchievement.getTimesGained() + " times!");
					notification.setContentUk("Вітання! Ви отримали досягенння: "
							+ userAchievement.getTitleUk() +
							" " + userAchievement.getTimesGained() + "-ий раз!");
				}
				activitiesService.addActivityForUser(activity);
				notificationService.insert(notification);
			}
		}
	}

}
