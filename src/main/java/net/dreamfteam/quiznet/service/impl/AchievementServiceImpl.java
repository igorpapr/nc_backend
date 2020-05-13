package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.dao.GameSessionDao;
import net.dreamfteam.quiznet.data.entities.GameSession;
import net.dreamfteam.quiznet.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AchievementServiceImpl implements AchievementService {

	private final GameSessionDao gameSessionDao;

	@Autowired
	public AchievementServiceImpl(GameSessionDao gameSessionDao) {
		this.gameSessionDao = gameSessionDao;
	}

	@Override
	public void checkFirstGameOfUserAchievement(String sessionId) {
		GameSession gameSession = gameSessionDao.getById(sessionId);
		String userId = gameSession.getUserId();
		if (userId != null){
			int numberOfSessions = gameSessionDao.getNumberOfSessionsOfUser(userId);
			if (numberOfSessions == 1){

			}
		}
	}
}
