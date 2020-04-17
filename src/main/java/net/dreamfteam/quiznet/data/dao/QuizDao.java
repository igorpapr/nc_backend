package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.data.entities.Quiz;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;

import java.util.List;

public interface QuizDao {
    Quiz saveQuiz(Quiz quiz);

    Quiz updateQuiz(DtoQuiz dtoQuiz);

    Quiz getQuiz(DtoQuiz dtoQuiz);

    void deleteQuizById(Long id);

    long saveQuestion(Question question);

    void saveFirstTypeAns(Question question);

    void saveSecondThirdTypeAns(Question question);

    void saveFourthTypeAns(Question question);

    void deleteQuestion(Question question);

    List<Question> getQuestionList(Question question);

    Quiz getUserQuizByTitle(String title, long username);
}
