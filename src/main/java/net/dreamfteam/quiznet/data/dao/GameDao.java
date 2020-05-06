package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Game;

public interface GameDao {

    Game createGame(Game game);

    Game getGameByAccessId(String accessId);

    Game getGame(String id);

    void startGame(String gameId);

    void updateGame(Game game);

    void removeGame(String id);

    int getGameDuration(String gameId);

    int calculateDuration(Game game);

}
