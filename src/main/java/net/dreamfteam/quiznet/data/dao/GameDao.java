package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Answer;
import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.data.entities.Question;

public interface GameDao {

    Game createGame(Game game);

    Game getGameByAccessId(String accessId);

    Game getGame(String id);

    void startGame(String gameId);

    void updateGame(Game game);

    void removeGame(String id);

    Question getQuestion(String gameId);

    void saveAnswer(Answer answer);
    int getGameDuration(String gameId);

    int calculateDuration(Game game);

}
