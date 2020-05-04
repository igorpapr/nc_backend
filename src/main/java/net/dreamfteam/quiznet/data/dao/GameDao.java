package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Game;

public interface GameDao {

    Game createGame(Game game);

    Game getGameByAccessId(String accessId);

    void startGame(String gameId);

    Game updateGame(Game game);

    void removeGame(String id);

    Game getGame(String id);


}
