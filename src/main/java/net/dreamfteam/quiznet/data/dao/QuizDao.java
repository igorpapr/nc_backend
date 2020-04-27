package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.data.entities.Quiz;
import net.dreamfteam.quiznet.data.entities.QuizFiltered;
import net.dreamfteam.quiznet.data.entities.QuizView;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;
import net.dreamfteam.quiznet.web.dto.DtoQuizFilter;

import java.util.List;
import java.util.Map;

public interface QuizDao {
    Quiz saveQuiz(Quiz quiz);

    Quiz updateQuiz(Quiz quiz, String quizId);

    Quiz getQuiz(String quizId, String userId);

    void markAsFavourite(DtoQuiz dtoQuiz);

    void markAsPublished(DtoQuiz dtoQuiz);

    void deleteQuizById(String id);

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

    List<QuizView> getQuizzes(int startIndex, int amount);

    List<QuizView> getInvalidQuizzes(int startIndex, int amount);

    int getQuizzesTotalSize();

    Quiz getUserQuizByTitle(String title, String username);

    void validateQuiz(DtoQuiz dtoQuiz);

    List<QuizFiltered> findQuizzesByFilter(DtoQuizFilter quizFilter, int startIndex, int amount);

    void addQuizImage(String imageId, String quizId);

    void addQuestionImage(String imageId, String questionId);


}
