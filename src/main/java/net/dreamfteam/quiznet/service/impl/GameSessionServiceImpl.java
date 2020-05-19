package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.security.AuthenticationFacade;
import net.dreamfteam.quiznet.data.dao.GameSessionDao;
import net.dreamfteam.quiznet.data.entities.GameSession;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.GameService;
import net.dreamfteam.quiznet.service.GameSessionService;
import net.dreamfteam.quiznet.service.SseService;
import net.dreamfteam.quiznet.web.dto.DtoGameSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GameSessionServiceImpl implements GameSessionService {

    private final GameSessionDao gameSessionDao;
    private final SseService sseService;
    private final GameService gameService;
    private final AuthenticationFacade authenticationFacade;


    @Autowired
    public GameSessionServiceImpl(GameSessionDao gameSessionDao, SseService sseService, GameService gameService,
                                  AuthenticationFacade authenticationFacade) {
        this.gameSessionDao = gameSessionDao;
        this.sseService = sseService;
        this.gameService = gameService;
        this.authenticationFacade = authenticationFacade;
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
                //set activities

            }
            //check achievements
        }

    }
}
