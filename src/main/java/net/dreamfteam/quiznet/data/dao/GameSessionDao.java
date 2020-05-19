package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.GameSession;

import java.util.List;
import java.util.Map;

public interface GameSessionDao {
    GameSession getSessionByAccessId(String accessId, String userId, String username);

    GameSession getById(String sessionId);

    List<Map<String, String>> getSessions(String gameId);

    GameSession createSession(GameSession gameSession);

    void updateSession(GameSession gameSession);

    boolean gameHasAvailableSlots(String accessId);

    String getGameId(String sessionId);

    void removePlayer(String sessionId);

    int getNumberOfSessionsOfUser(String userId);

    int getNumberOfQuizzesPlayedByUser(String userId);

    boolean isGameFinished (String gameId);

    int setWinnersForTheGame(String gameId);
}
