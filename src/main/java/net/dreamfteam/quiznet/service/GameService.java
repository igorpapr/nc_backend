package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.web.dto.DtoGame;

public interface GameService{
    Game createGame(DtoGame game, String userId);

    void updateGame(DtoGame game);

    Game getGameByAccessId(String accessId);

    Game getGameById(String gameId);

    void startGame(String gameId);

    Question getQuestion(String gameId);


}
