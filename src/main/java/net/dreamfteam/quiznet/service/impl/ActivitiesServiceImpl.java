package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.dao.ActivityDao;
import net.dreamfteam.quiznet.data.dao.GameDao;
import net.dreamfteam.quiznet.data.entities.ActivityType;
import net.dreamfteam.quiznet.data.entities.FriendsActivity;
import net.dreamfteam.quiznet.service.ActivitiesService;
import net.dreamfteam.quiznet.web.dto.DtoActivity;
import net.dreamfteam.quiznet.web.dto.DtoGameWinner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivitiesServiceImpl implements ActivitiesService {

	private final ActivityDao activityDao;
	private final GameDao gameDao;

	@Autowired
	public ActivitiesServiceImpl(ActivityDao activityDao,GameDao gameDao) {
		this.activityDao = activityDao;
		this.gameDao = gameDao;
	}

	@Override
	public List<FriendsActivity> getFriendsActivities(String userId) {
		return activityDao.getFriendsActivitiesList(userId);
	}

	@Override
	public void addActivityForUser(DtoActivity dto) {
		activityDao.addActivity(dto);
	}

	@Override
	@Async
	public void addWinnersActivities(String gameId){
		List<DtoGameWinner> winners = gameDao.getWinnersOfTheGame(gameId);
		if(winners != null){
			for (DtoGameWinner winner : winners) {
				if (winner.getUserId() != null){
					DtoActivity activity = DtoActivity.builder()
							.userId(winner.getUserId())
							.activityType(ActivityType.GAMEPLAY_RELATED)
							.content("Won the game while playing the quiz: \"" + winner.getQuizTitle() + "\"")
							.contentUk("Виграв/ла гру граючи квіз: \"" + winner.getQuizTitle() + "\"")
							.linkInfo(gameId)
							.build();
					addActivityForUser(activity);
				}
			}
		}
	}
}
