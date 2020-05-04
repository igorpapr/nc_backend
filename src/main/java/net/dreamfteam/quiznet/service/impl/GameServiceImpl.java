package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.dao.GameDao;
import net.dreamfteam.quiznet.data.dao.QuizDao;
import net.dreamfteam.quiznet.data.entities.Game;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.GameService;
import net.dreamfteam.quiznet.web.dto.DtoGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private final GameDao gameDao;
    private final QuizDao quizDao;

    @Autowired
    public GameServiceImpl(GameDao gameDao, QuizDao quizDao) {
        this.gameDao = gameDao;
        this.quizDao = quizDao;
    }

    @Override
    public Game createGame(DtoGame dtoGame) {
        checkQuizExistance(dtoGame.getQuizId());

        Game game = Game.builder()
                .roundDuration(dtoGame.getRoundDuration())
                .numberOfQuestions(dtoGame.getNumberOfQuestions())
                .maxUsersCount(dtoGame.getMaxUsersCount())
                .additionalPoints(dtoGame.isAdditionalPoints())
                .breakTime(dtoGame.getBreakTime())
                .quizId(dtoGame.getQuizId())
                .build();

        return gameDao.createGame(game);
    }

    @Override
    public Game getGameByAccessId(String accessId) {
        return gameDao.getGameByAccessId(accessId);
    }

    @Override
    public void startGame(String gameId) {
        gameDao.startGame(gameId);
    }

    private void checkQuizExistance(String quizId){
        if(quizDao.getQuiz(quizId) == null){
            throw new ValidationException("Quiz with id: "+quizId+" not exists");
        }
    }
}
