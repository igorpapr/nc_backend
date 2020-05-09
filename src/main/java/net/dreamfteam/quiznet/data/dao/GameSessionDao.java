package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.GameSession;

public interface GameSessionDao {
    GameSession getSessionByAccessId(String accessId, String userId);

    GameSession getById(String sessionId);

    GameSession createSession(GameSession gameSession);

    void updateSession(GameSession gameSession);

    boolean gameHasAvailableSlots(String accessId);
}
