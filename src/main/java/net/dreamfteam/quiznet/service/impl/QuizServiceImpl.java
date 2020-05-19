package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.dao.QuizDao;
import net.dreamfteam.quiznet.data.entities.*;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ActivitiesService;
import net.dreamfteam.quiznet.service.NotificationService;
import net.dreamfteam.quiznet.service.QuizService;
import net.dreamfteam.quiznet.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizDao quizDao;
    private final NotificationService notificationService;
    private final ActivitiesService activitiesService;


    @Autowired
    public QuizServiceImpl(QuizDao quizDao, NotificationService notificationService,
                           ActivitiesService activitiesService) {
        this.quizDao = quizDao;
        this.notificationService = notificationService;
        this.activitiesService = activitiesService;
    }

    @Override
    public Quiz saveQuiz(DtoQuiz newQuiz, String currentUserId, MultipartFile image) throws ValidationException {

        newQuiz.setCreatorId(currentUserId);
        checkQuizUniqueness(newQuiz.getTitle(), newQuiz.getCreatorId());
        Quiz quiz = Quiz.builder()
                .title(newQuiz.getTitle())
                .creationDate(Calendar.getInstance().getTime())
                .creatorId(newQuiz.getCreatorId())
                .language(newQuiz.getLanguage())
                .description(newQuiz.getDescription())
                .imageRef(newQuiz.getImageRef())
                .validated(false).activated(false)
                .published(false)
                .isFavourite(false)
                .tagIdList(newQuiz.getTagList())
                .categoryIdList(newQuiz.getCategoryList())
                .build();

        if (image != null) {
            try {
                quiz.setImageContent(image.getBytes());
            } catch (IOException e) {
                throw new ValidationException("Broken image");
            }
        } else {
            quiz.setImageContent(null);
        }

        quiz = quizDao.saveQuiz(quiz);

        System.out.println("Saved in db:" + quiz.toString());
        return quiz;
    }

    @Override
    public Quiz updateQuiz(DtoEditQuiz dtoQuiz, MultipartFile image) {
        Quiz quiz = Quiz.builder()
                .title(dtoQuiz.getNewTitle())
                .creationDate(Calendar.getInstance().getTime())
                .creatorId(getQuiz(dtoQuiz.getQuizId()).getCreatorId())
                .language(dtoQuiz.getNewLanguage())
                .description(dtoQuiz.getNewDescription())
                .validated(false)
                .activated(false)
                .published(false)
                .isFavourite(false)
                .tagIdList(dtoQuiz.getNewTagList())
                .categoryIdList(dtoQuiz.getNewCategoryList())
                .build();

        if (image != null) {
            try {
                quiz.setImageContent(image.getBytes());
            } catch (IOException e) {
                throw new ValidationException("Broken image");
            }
        } else {
            quiz.setImageContent(null);
        }

        return quizDao.updateQuiz(quiz, dtoQuiz.getQuizId());
    }

    @Override
    public Quiz getQuiz(String quizId) {
        Quiz quiz = quizDao.getQuiz(quizId);
        return quiz;
    }

    @Override
    public Quiz getQuiz(String quizId, String userId) {
        Quiz quiz = quizDao.getQuiz(quizId, userId);
        return quiz;
    }

    @Override
    public void markAsFavourite(DtoQuiz quiz) {
        quizDao.markAsFavourite(quiz);
    }

    @Override
    public void markAsPublished(DtoQuiz quiz) {
        quizDao.markAsPublished(quiz);
    }

    @Override
    public void validateQuiz(DtoQuiz quiz) {


        notificationService.insert(DtoNotification.builder()
                                                  .content("Your quiz "+ quiz.getTitle()+" was validated")
                                                  .userId(quiz.getCreatorId())
                                                  .build());
        if(quizDao.validateQuiz(quiz) > 0 && quiz.isValidated()){
            DtoActivity activity = DtoActivity.builder()
                    .content("Successfully created a quiz - \"" + quiz.getTitle() +"\". It is playable now.")
                    .activityType(ActivityType.VALIDATION_RELATED)
                    .userId(quiz.getCreatorId())
                    .build();
            activitiesService.addActivityForUser(activity);
        }
    }

    @Override
    public void deleteQuizById(String id) {
        quizDao.deleteQuizById(id);
    }

    @Override
    public void deactivateQuiz(String id) {
        quizDao.deactivateQuiz(id);
    }

    @Override
    public Question saveQuestion(Question newQuestion, MultipartFile image) {
        if(image != null){
            try {
                newQuestion.setImageContent(image.getBytes());
            } catch (IOException e) {
                throw new ValidationException("Broken image");
            }
        }else{
            newQuestion.setImageContent(null);
        }

        newQuestion.setId(quizDao.saveQuestion(newQuestion));

        saveAnsw(newQuestion);
        return newQuestion;
    }

    @Override
    public Question updateQuestion(Question newQuestion, MultipartFile image) {
        quizDao.deleteQuestion(newQuestion);

        if(image != null){
            try {
                newQuestion.setImageContent(image.getBytes());
            } catch (IOException e) {
                throw new ValidationException("Broken image");
            }
        }else{
            newQuestion.setImageContent(null);
        }

        newQuestion.setId(quizDao.saveQuestion(newQuestion));

        saveAnsw(newQuestion);

        return newQuestion;
    }

    @Override
    public void deleteQuestion(Question question) {
        quizDao.deleteQuestion(question);
    }

    @Override
    public List<Question> getQuestionList(String quizId) {
        return quizDao.getQuestionList(quizId);
    }

    @Override
    public List<Question> getQuestionsInPage(int startIndex, int amount, String quizId) {
        return quizDao.getQuestionsInPage(startIndex, amount, quizId);
    }

    @Override
    public List<Map<String, String>> getTagList() {
        return quizDao.getTagList();
    }

    @Override
    public List<List<Object>> getCategoryList() {
        return quizDao.getCategoryList();
    }

    @Override
    public List<Quiz> getUserQuizList(String userId, String thisUserId) {
        return quizDao.getUserQuizList(userId, thisUserId);
    }

    @Override
    public List<QuizView> getQuizzes(int startIndex, int amount) {
        return quizDao.getQuizzes(startIndex, amount);
    }

    @Override
    public List<QuizValid> getInvalidQuizzes(int startIndex, int amount, String adminId) {
        return quizDao.getInvalidQuizzes(startIndex, amount, adminId);
    }

    @Override
    public List<QuizValid> getValidQuizzes(int startIndex, int amount, String adminId) {
        return quizDao.getValidQuizzes(startIndex, amount, adminId);
    }

    @Override
    public int getQuizzesTotalSize() {
        return quizDao.getQuizzesTotalSize();
    }

    @Override

    public int getInvalidQuizzesTotalSize() {
        return quizDao.getInvalidQuizzesTotalSize();
    }

    @Override
    public int getValidQuizzesTotalSize(String adminId) {
        return quizDao.getValidQuizzesTotalSize(adminId);
    }

    @Override
    public Quiz setValidator(String quizId, String adminId) {
        Quiz quiz = quizDao.setValidator(quizId, adminId);
        return quiz;
    }

    @Override
    public List<Quiz> getUserFavouriteList(String userId) {
        return quizDao.getUserFavouriteList(userId);
    }

    @Override
    public List<QuizFiltered> findQuizzesByFilter(DtoQuizFilter quizFilter, int startIndex, int amount) {
        return quizDao.findQuizzesByFilter(quizFilter, startIndex, amount);
    }

    @Override
    public int findQuizzesFilterSize(DtoQuizFilter quizFilter) {
        return quizDao.findQuizzesFilterSize(quizFilter);
    }

    @Override
    public List<QuizFiltered> shortListOfQuizzes() {
        DtoQuizFilter quizFilter = DtoQuizFilter.builder().moreThanRating(2).orderByRating(true).build();
        return quizDao.findQuizzesByFilter(quizFilter, 0, 5);
    }

    @Override
    public List<QuizView> getSuggestionsQuizList(String userId, int amount) {
        return quizDao.getSuggestionsQuizListByCategoriesAndTags(userId, amount);
    }

    @Override
    public int getQuestionsAmountInQuiz(String quizId) {
        return quizDao.getQuestionsAmountInQuiz(quizId);
    }

    private void checkQuizUniqueness(String title, String creatorId) {
        if (quizDao.getUserQuizByTitle(title, creatorId) != null) {
            throw new ValidationException("Quiz with current name already exist for this user");
        }
    }

    private void saveAnsw(Question question) {
        switch (question.getTypeId()) {
            case (1): // One of four options question, can be multiple right answers
                quizDao.saveFirstTypeAns(question);
                break;
            case (2): // True-false question
            case (3):
                quizDao.saveSecondThirdTypeAns(question);
                break;
            case (4):
                quizDao.saveFourthTypeAns(question);
                break;
            default:
                throw new ValidationException("Question type should be in range of [1 - 4]");
        }
    }
}