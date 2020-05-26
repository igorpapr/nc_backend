package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.GameSession;
import net.dreamfteam.quiznet.web.dto.DtoPlayerSession;

import java.util.List;
import java.util.Map;

public interface GameSessionDao {
    GameSession getSessionByAccessId(String accessId, String userId, String username);

    GameSession getById(String sessionId);

    List<DtoPlayerSession> getSessions(String gameId);

    GameSession createSession(GameSession gameSession);

    void updateSession(GameSession gameSession);

    boolean gameHasAvailableSlots(String accessId);

    String getGameId(String sessionId);

    void removePlayer(String sessionId);

    int getNumberOfSessionsOfUser(String userId);

    int getNumberOfQuizzesPlayedByUser(String userId);

    boolean isGameFinished (String gameId);

    int setWinnersForTheGame(String gameId);

    boolean isCreator(String gameId);

}
