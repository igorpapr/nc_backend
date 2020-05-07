package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.GameSession;

public interface GameSessionDao {
    GameSession getSessionByAccessId(String accessId, String userId);

    GameSession createSession(GameSession gameSession);

    void updateDurationTime(int durationTime, String gameId);

    boolean gameHasAvailableSlots(String accessId);
}
