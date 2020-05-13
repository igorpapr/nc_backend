package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.GameSession;
import net.dreamfteam.quiznet.web.dto.DtoGameSession;

import java.util.List;
import java.util.Map;

public interface GameSessionService {
    GameSession joinGame(String accessId, String userId);

    void setResult(DtoGameSession dtoGameSession);

    List<Map<String,String>> getSessions(String gameId);
}
