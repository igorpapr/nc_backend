package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.data.entities.QuizCreatorFullStatistics;
import net.dreamfteam.quiznet.data.entities.UserCategoryAchievementInfo;

public interface GameDao {

    Game createGame(Game game);

    Game getGameByAccessId(String accessId);

    Game getGame(String id);

    void startGame(String gameId);

    void updateGame(Game game);

    Question getQuestion(String gameId);

    UserCategoryAchievementInfo getUserGamesInCategoryInfo(String userId, String gameId);

    QuizCreatorFullStatistics getAmountOfPlayedGamesCreatedByCreatorOfGame(String gameId);
}
