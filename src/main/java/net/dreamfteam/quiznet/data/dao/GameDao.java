package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.data.entities.Question;

public interface GameDao {

    Game createGame(Game game);

    Game getGameByAccessId(String accessId);

    Game getGame(String id);

    void startGame(String gameId);

    void updateGame(Game game);

    Question getQuestion(String gameId);
}
