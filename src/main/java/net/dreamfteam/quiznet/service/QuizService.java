package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.*;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface QuizService {

    Quiz saveQuiz(DtoQuiz newQuiz, String currentUserId, MultipartFile image, String language)
            throws ValidationException;

    Quiz updateQuiz(DtoEditQuiz quiz, MultipartFile image, String language);

    Quiz getQuiz(String quizId, String userId, String language);

    void markAsFavourite(DtoQuiz quiz);

    void markAsPublished(DtoQuiz quiz);

    void validateQuiz(DtoQuiz quiz);

    void deleteQuizById(String id);

    void deactivateQuiz(String id);

    Question saveQuestion(Question newQuestion, MultipartFile image);

    Question updateQuestion(Question newQuestion, MultipartFile image);

    void deleteQuestion(Question question);

    List<Question> getQuestionList(String quizId);

    List<Question> getQuestionsInPage(int startIndex, int amount , String quizId);

    List<Map<String, String>> getTagList();

    List<List<Object>> getCategoryList(String language);

    List<Quiz> getUserQuizList(String userId, String thisUserId);

    List<QuizView> getQuizzes(int startIndex, int amount);

    List<DtoQuizValid> getInvalidQuizzes(int startIndex, int amount, String adminId);

    List<DtoQuizValid> getValidQuizzes(int startIndex, int amount, String adminId);

    List<QuizFiltered> findQuizzesByFilter(DtoQuizFilter quizFilter, int startIndex, int amount);

    int findQuizzesFilterSize(DtoQuizFilter quizFilter);

    List<QuizFiltered> shortListOfQuizzes();

    List<DtoQuizRates> getUserQuizzesRating();

    List<QuizFiltered> getSuggestionsQuizList(String userId, int amount);

    int getQuestionsAmountInQuiz(String quizId);

    int getQuizzesTotalSize();

    int getInvalidQuizzesTotalSize();

    int getValidQuizzesTotalSize(String adminId);

    Quiz setValidator(String quizId, String adminId, String language);

    List<Quiz> getUserFavouriteList(String userId);

    List<DtoQuizLastPlayed> getLastPlayedQuizzes();

    List<DtoPopularQuiz> getMostPopularQuizzesForLastWeek(int amount);

    Rating getUserQuizRating(String quizId, String userId);

    int countValidatedQuizzesByAdmin();

    int countValidatedQuizzesByModerator();

    DtoQuizzesStatuses getQuizStatusesData();

    Integer getUserQuizListAmount(String userId);

    Integer getUserFavQuizListAmount(String userId);
}
