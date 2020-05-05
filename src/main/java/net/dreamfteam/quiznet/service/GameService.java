package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.web.dto.DtoGame;

public interface GameService{
    Game createGame(DtoGame game);

    Game getGameByAccessId(String accessId);

    Game getGameById(String gameId);

    void startGame(String gameId);

}
