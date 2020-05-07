package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.GameSession;

public interface GameSessionService {
    GameSession joinGame(String accessId, String userId);
}
