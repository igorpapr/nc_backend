package net.dreamfteam.quiznet.data.dao;

import net.dreamfteam.quiznet.data.entities.*;
import net.dreamfteam.quiznet.web.dto.*;

import java.util.List;
import java.util.Map;

public interface QuizDao {
    Quiz saveQuiz(Quiz quiz, String language);

    Quiz updateQuiz(Quiz quiz, String quizId, String language);

    Quiz getQuiz(String quizId, String userId, String language);

    Quiz getQuiz(String quizId, String language);

    void markAsFavourite(DtoQuiz dtoQuiz);

    void markAsPublished(DtoQuiz dtoQuiz);

    void deleteQuizById(String id);

    void deactivateQuiz(String id);

    String saveQuestion(Question question);

    void saveFirstTypeAns(Question question);

    void saveSecondThirdTypeAns(Question question);

    void saveFourthTypeAns(Question question);

    void deleteQuestion(Question question);

    List<Question> getQuestionList(String quizId);

    List<Map<String, String>> getTagList();

    List<List<Object>> getCategoryList(String language);

    List<Quiz> getUserQuizList(String userId, String thisUserId);

    List<Quiz> getUserFavouriteList(String userId);

    List<QuizView> getQuizzes(int startIndex, int amount);

    List<QuizFiltered> getSuggestionsQuizListByCategoriesAndTags(String userId, int amount);

    List<DtoQuizValid> getInvalidQuizzes(int startIndex, int amount, String adminId);

    int getQuizzesTotalSize();

    int getQuestionsAmountInQuiz(String quizId);

    List<Question> getQuestionsInPage(int startIndex, int amount, String quizId);

    Quiz getUserQuizByTitle(String title, String username);

    int validateQuiz(DtoQuiz dtoQuiz);

    List<QuizFiltered> findQuizzesByFilter(DtoQuizFilter quizFilter, int startIndex, int amount);

    int findQuizzesFilterSize(DtoQuizFilter quizFilter);

    void removeQuestionImage(String questionId);

    List<DtoQuizValid> getValidQuizzes(int startIndex, int amount, String adminId);

    int getInvalidQuizzesTotalSize();

    int getValidQuizzesTotalSize(String adminId);

    Quiz setValidator(String quizId, String adminId, String language);

    Question loadAnswersForQuestion(Question question, int i);

    List<DtoQuizRates> getUserQuizzesRating(String userId);

    int getAmountSuccessCreated(String userId);

    List<DtoQuizLastPlayed> getLastPlayedQuizzes(String userId);

    List<DtoPopularQuiz> getMostPopularQuizzesForLastWeek(int amount);

    List<Rating> getUserQuizRating(String quizId, String userId);

    void rateQuiz(String sessionId, int ratingPoints, String userId);

    int countValidatedQuizzesByAdmin();

    int countValidatedQuizzesByModerator();

    int getQuizzesNumber();

    int getActivatedNumber();

    int getPublishedNumber();

    int getRejectedNumber();

    int getUnvalidatedNumber();

    Integer getUserQuizListAmount(String userId);

    Integer getUserFavQuizListAmount(String userId);

}
