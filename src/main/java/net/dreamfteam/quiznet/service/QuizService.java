package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.*;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;
import net.dreamfteam.quiznet.web.dto.DtoQuizFilter;

import java.util.List;
import java.util.Map;

public interface QuizService {

    Quiz saveQuiz(DtoQuiz newQuiz) throws ValidationException;

    Quiz updateQuiz(DtoQuiz quiz);

    Quiz getQuiz(String quizId, String userId);

    void markAsFavourite(DtoQuiz quiz);

    void markAsPublished(DtoQuiz quiz);

    void validateQuiz(DtoQuiz quiz);

    void deleteQuizById(DtoQuiz dtoQuiz);

    void deactivateQuiz(DtoQuiz dtoQuiz);

    Question saveQuestion(Question newQuestion);

    Question updateQuestion(Question newQuestion);

    void deleteQuestion(Question question);

    List<Question> getQuestionList(String quizId);

    List<Map<String, String>> getTagList();

    List<List<Object>> getCategoryList();

    List<Quiz> getUserQuizList(String userId);

    List<QuizView> getQuizzes(int startIndex, int amount);

    List<QuizValid> getInvalidQuizzes(int startIndex, int amount, String adminId);

    List<QuizValid> getValidQuizzes(int startIndex, int amount, String adminId);

    List<QuizFiltered> findQuizzesByFilter(DtoQuizFilter quizFilter, int startIndex, int amount);

    List<QuizFiltered> shortListOfQuizzes();

    void addQuizImage(String imageId, String quizId);

    void addQuestionImage(String imageId, String questionId);

    int getQuizzesTotalSize();

    int getInvalidQuizzesTotalSize();

    int getValidQuizzesTotalSize(String adminId);

    Quiz setValidator(String quizId, String adminId);

}
