package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.security.AuthenticationFacade;
import net.dreamfteam.quiznet.data.dao.GameDao;
import net.dreamfteam.quiznet.data.dao.GameSessionDao;
import net.dreamfteam.quiznet.data.entities.ActivityType;
import net.dreamfteam.quiznet.data.entities.GameSession;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.*;
import net.dreamfteam.quiznet.web.dto.DtoActivity;
import net.dreamfteam.quiznet.web.dto.DtoGameSession;
import net.dreamfteam.quiznet.web.dto.DtoGameWinner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GameSessionServiceImpl implements GameSessionService {

    private final GameSessionDao gameSessionDao;
    private final GameDao gameDao;
    private final SseService sseService;
    private final GameService gameService;
    private final AuthenticationFacade authenticationFacade;
    private final ActivitiesService activitiesService;
    private final AchievementService achievementService;


    @Autowired
    public GameSessionServiceImpl(GameSessionDao gameSessionDao, SseService sseService, GameService gameService,
                                  AuthenticationFacade authenticationFacade, ActivitiesService activitiesService,
                                  GameDao gameDao,
                                  AchievementService achievementService) {
        this.gameSessionDao = gameSessionDao;
        this.sseService = sseService;
        this.gameService = gameService;
        this.authenticationFacade = authenticationFacade;
        this.activitiesService = activitiesService;
        this.gameDao = gameDao;
        this.achievementService = achievementService;
    }

    @Override
    public GameSession joinGame(String accessId, String userId, String username) {
        return gameSessionDao.getSessionByAccessId(accessId, userId, username);
    }

    @Override
    public void setResult(DtoGameSession dtoGameSession) {

        GameSession gameSession =
                GameSession.builder()
                        .score(dtoGameSession.getScore())
                        .winner(false)
                        .durationTime(dtoGameSession.getDurationTime())
                        .id(dtoGameSession.getSessionId())
                        .gameId(getGameIdBySessionId(dtoGameSession.getSessionId()))
                        .build();

        gameSessionDao.updateSession(gameSession);
        checkForGameOver(gameSession.getGameId());
    }

    @Override
    public List<Map<String, String>> getSessions(String gameId) {
        return gameSessionDao.getSessions(gameId);
    }

    @Override
    public void removePlayer(String sessionId) {
        gameSessionDao.removePlayer(sessionId);
        String gameId = gameSessionDao.getGameId(sessionId);
        checkForGameOver(gameId);
    }

    @Override
    public GameSession getSession(String sessionId) {
        return gameSessionDao.getById(sessionId);
    }

    @Override
    public String getGameIdBySessionId(String sessionId) {
        return gameSessionDao.getGameId(sessionId);
    }

    private void checkForGameOver(String gameId){
        boolean isGameFinished = gameSessionDao.isGameFinished(gameId);
        if (isGameFinished) {
            int res = gameSessionDao.setWinnersForTheGame(gameId);
            sseService.send(gameId, "finished", gameId);
            if(res > 0){//setting activities
                List<DtoGameWinner> winners = gameDao.getWinnersOfTheGame(gameId);
                for (DtoGameWinner winner : winners){
                    DtoActivity activity = DtoActivity.builder()
                            .userId(winner.getUserId())
                            .activityType(ActivityType.GAMEPLAY_RELATED)
                            .content("Won the game while playing the quiz: \""+winner.getQuizTitle()+"\"")
                            .build();
                    activitiesService.addActivityForUser(activity);
                }
            }
            //checking achievements
            List<Map<String, String>> sessionsMaps = getSessions(gameId);
            for (Map<String,String> session : sessionsMaps) {
                achievementService.checkAftergameAchievements(session.get("game_session_id"));
            }

            //sending message event to subscribers
            sseService.send(gameId, "finished", gameId);
        }
    }
}
