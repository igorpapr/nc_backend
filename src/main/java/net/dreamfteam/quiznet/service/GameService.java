package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.web.dto.DtoGame;
import net.dreamfteam.quiznet.web.dto.DtoGameCount;
import net.dreamfteam.quiznet.web.dto.DtoGameWinner;

import java.util.List;

public interface GameService {
    Game createGame(DtoGame game, String userId, String username);

    void updateGame(DtoGame game);

    void deleteGame(String gameId);

    Game getGameByAccessId(String accessId);

    Game getGameById(String gameId);

    void startGame(String gameId);

    Question getQuestion(String gameId);

    void rateGame(String gameSessionId, int rating, String userId);

    List<DtoGameCount> getGamesAmountForDay();

    int gameTime(String gameId);

    List<DtoGameWinner> getWinnersOfTheGame(String gameId);
}
