package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.dao.GameDao;
import net.dreamfteam.quiznet.data.dao.GameSessionDao;
import net.dreamfteam.quiznet.data.entities.ActivityType;
import net.dreamfteam.quiznet.data.entities.GameSession;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.AchievementService;
import net.dreamfteam.quiznet.service.ActivitiesService;
import net.dreamfteam.quiznet.service.GameSessionService;
import net.dreamfteam.quiznet.service.SseService;
import net.dreamfteam.quiznet.web.dto.DtoActivity;
import net.dreamfteam.quiznet.web.dto.DtoGameSession;
import net.dreamfteam.quiznet.web.dto.DtoGameWinner;
import net.dreamfteam.quiznet.web.dto.DtoPlayerSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class GameSessionServiceImpl implements GameSessionService {

    private final GameSessionDao gameSessionDao;
    private final GameDao gameDao;
    private final SseService sseService;
    private final ActivitiesService activitiesService;
    private final AchievementService achievementService;


    @Autowired
    public GameSessionServiceImpl(GameSessionDao gameSessionDao, SseService sseService,
                                  ActivitiesService activitiesService,
                                  GameDao gameDao,
                                  AchievementService achievementService) {
        this.gameSessionDao = gameSessionDao;
        this.sseService = sseService;
        this.activitiesService = activitiesService;
        this.gameDao = gameDao;
        this.achievementService = achievementService;
    }

    @Override
    public GameSession joinGame(String accessId, String userId, String username) {
        GameSession gameSession = gameSessionDao.getSessionByAccessId(accessId, userId, username);

        String name = username;

        //IF SESSION CREATED FOR USER
        if (gameSession != null && Objects.equals(gameSession.getId(), userId)) {
            return gameSession;
        }
        //IF GAME CONTAINS PLAYER SESSION WITH SAME NAME
        else if (gameSession != null) {
            name = name + "(1)";
        }

        if (!gameSessionDao.gameHasAvailableSlots(accessId)) {
            throw new ValidationException("Sorry, no slots are available");
        }


        String gameId = gameDao.getGameByAccessId(accessId).getId();


        gameSession = GameSession.builder()
                .userId(userId.startsWith("-") ? null : userId)
                .username(name)
                .gameId(gameId)
                .score(0)
                .winner(false)
                .creator(gameSessionDao.isCreator(gameId))
                .savedByUser(!userId.startsWith("-"))
                .durationTime(0)
                .build();

        gameSession = gameSessionDao.createSession(gameSession);

        sseService.send(gameId, "join", name);

        return gameSession;

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
    public List<DtoPlayerSession> getSessions(String gameId) {
        return gameSessionDao.getSessions(gameId);
    }

    @Override
    public void removePlayer(String sessionId) {
        String gameId = gameSessionDao.getGameId(sessionId);
        gameSessionDao.removePlayer(sessionId);
        checkForGameOver(gameId);
    }

    @Override
    public String getGameIdBySessionId(String sessionId) {
        return gameSessionDao.getGameId(sessionId);
    }

    private void checkForGameOver(String gameId) {
        if (gameSessionDao.isGameFinished(gameId)) {
            if (gameSessionDao.setWinnersForTheGame(gameId) > 0) {   //setting activities
                List<DtoGameWinner> winners = gameDao.getWinnersOfTheGame(gameId);
                for (DtoGameWinner winner : winners) {
                    DtoActivity activity = DtoActivity.builder()
                            .userId(winner.getUserId())
                            .activityType(ActivityType.GAMEPLAY_RELATED)
                            .content("Won the game while playing the quiz: \"" + winner.getQuizTitle() + "\"")
                            .contentUk("Виграв/ла гру граючи квіз: \"" + winner.getQuizTitle() + "\"")
                            .linkInfo(gameId)
                            .build();
                    activitiesService.addActivityForUser(activity);
                }
            }

            //checking achievements
            List<DtoPlayerSession> sessionsMaps = getSessions(gameId);
            for (DtoPlayerSession session : sessionsMaps) {
                achievementService.checkAftergameAchievements(session.getGame_session_id());
            }

            //sending message event to subscribers
            sseService.send(gameId, "finished", gameId);
            sseService.remove(gameId);
        }
    }
}
