package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.GameSession;
import net.dreamfteam.quiznet.web.dto.DtoGameSession;

public interface GameSessionService {
    GameSession joinGame(String accessId, String userId);

    void setResult(DtoGameSession dtoGameSession);
}
