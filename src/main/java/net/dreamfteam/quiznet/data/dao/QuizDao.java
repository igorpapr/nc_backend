package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.data.entities.Quiz;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;

import java.util.List;
import java.util.Map;

public interface QuizDao {
    Quiz saveQuiz(Quiz quiz);

    Quiz updateQuiz(Quiz quiz, String quizId);

    Quiz getQuiz(String quizId, String userId);

    void markAsFavourite(DtoQuiz dtoQuiz);

    void markAsPublished(DtoQuiz dtoQuiz);

    void deleteQuizById(DtoQuiz dtoQuiz);

    void deactivateQuiz(DtoQuiz dtoQuiz);

    String saveQuestion(Question question);

    void saveFirstTypeAns(Question question);

    void saveSecondThirdTypeAns(Question question);

    void saveFourthTypeAns(Question question);

    void deleteQuestion(Question question);

    List<Question> getQuestionList(String quizId);

    List<Map<String, String>> getTagList();

    List<List<Object>> getCategoryList();

    List<Quiz> getUserQuizList(String userId);

    Quiz getUserQuizByTitle(String title, String username);
}
