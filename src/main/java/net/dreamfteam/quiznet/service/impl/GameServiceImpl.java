package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.dao.GameDao;
import net.dreamfteam.quiznet.data.dao.GameSessionDao;
import net.dreamfteam.quiznet.data.dao.QuizDao;
import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.data.entities.GameSession;
import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.AchievementService;
import net.dreamfteam.quiznet.service.GameService;
import net.dreamfteam.quiznet.service.SseService;
import net.dreamfteam.quiznet.web.dto.DtoGame;
import net.dreamfteam.quiznet.web.dto.DtoGameCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private final GameDao gameDao;
    private final QuizDao quizDao;
    private final GameSessionDao gameSessionDao;
    private final SseService sseService;
    private final AchievementService achievementService;

    @Autowired
    public GameServiceImpl(GameDao gameDao, QuizDao quizDao, GameSessionDao gameSessionDao, SseService sseService,
                           AchievementService achievementService) {
        this.gameDao = gameDao;
        this.quizDao = quizDao;
        this.gameSessionDao = gameSessionDao;
        this.sseService = sseService;
        this.achievementService = achievementService;
    }

    @Override
    public Game createGame(DtoGame dtoGame, String userId, String username) {
        checkQuizExistance(dtoGame.getQuizId());

        Game game = Game.builder()
                .roundDuration(dtoGame.getRoundDuration())
                .numberOfQuestions(dtoGame.getNumberOfQuestions())
                .maxUsersCount(dtoGame.getMaxUsersCount())
                .additionalPoints(dtoGame.isAdditionalPoints())
                .breakTime(dtoGame.getBreakTime())
                .quizId(dtoGame.getQuizId())
                .build();

        game = gameDao.createGame(game);

        // CREATING SESSION OF CREATOR
        GameSession gameSession = GameSession.builder()
                .userId(userId.startsWith("-") ? null : userId)
                .username(username)
                .gameId(game.getId())
                .score(0)
                .winner(false)
                .creator(true)
                .savedByUser(!userId.startsWith("-"))
                .durationTime(0)
                .build();

        gameSessionDao.createSession(gameSession);

        return game;
    }

    @Override
    public void updateGame(DtoGame dtoGame) {
        Game game = Game.builder()
                .id(dtoGame.getId())
                .startDatetime(dtoGame.getStartDatetime())
                .roundDuration(dtoGame.getRoundDuration())
                .numberOfQuestions(dtoGame.getNumberOfQuestions())
                .maxUsersCount(dtoGame.getMaxUsersCount())
                .additionalPoints(dtoGame.isAdditionalPoints())
                .breakTime(dtoGame.getBreakTime())
                .quizId(dtoGame.getQuizId())
                .build();

        gameDao.updateGame(game);
    }

    @Override
    public Game getGameByAccessId(String accessId) {
        return gameDao.getGameByAccessId(accessId);
    }

    @Override
    public Game getGameById(String gameId) {
        return gameDao.getGame(gameId);
    }

    @Override
    public void startGame(String gameId) {

        gameDao.startGame(gameId);
        sseService.send(gameId,"start");

        //checking for achievements
        achievementService.checkOnStartGameAchievements(gameId);
    }

    private void checkQuizExistance(String quizId) {
        if (quizDao.getQuiz(quizId) == null) {
            throw new ValidationException("Quiz with id: " + quizId + " not exists");
        }
    }

    public Question getQuestion(String gameId) {
        Question question = gameDao.getQuestion(gameId);
        if (question == null) {
            throw new NullPointerException("No questions for " + gameId);
        }
        return quizDao.loadAnswersForQuestion(gameDao.getQuestion(gameId), 0);
    }

    @Override
    public void rateGame(String gameSessionId, int rating, String userId) {
        if(getGameById(gameSessionId) == null){
            throw new ValidationException("No game with such id found, cannot rate the quiz");
        }
        quizDao.rateQuiz(gameSessionId, rating, userId);

    }

    @Override
    public List<DtoGameCount> getGamesAmountForDay() {
        return gameDao.getGamesAmountForDay();
    }


}
