package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.security.IAuthenticationFacade;
import net.dreamfteam.quiznet.data.dao.QuizDao;
import net.dreamfteam.quiznet.data.entities.*;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ImageService;
import net.dreamfteam.quiznet.service.QuizService;
import net.dreamfteam.quiznet.web.dto.DtoEditQuiz;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;
import net.dreamfteam.quiznet.web.dto.DtoQuizFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class QuizServiceImpl implements QuizService {

    private QuizDao quizDao;
    private ImageService imageService;


    @Autowired
    public QuizServiceImpl(QuizDao quizDao, ImageService imageService) {
        this.quizDao = quizDao;
        this.imageService = imageService;
    }

    @Override
    public Quiz saveQuiz(DtoQuiz newQuiz, String currentUserId) throws ValidationException {

        newQuiz.setCreatorId(currentUserId);
        checkQuizUniqueness(newQuiz.getTitle(), newQuiz.getCreatorId());
        Quiz quiz = Quiz.builder().title(newQuiz.getTitle()).creationDate(Calendar.getInstance().getTime()).creatorId(newQuiz.getCreatorId()).language(newQuiz.getLanguage()).description(newQuiz.getDescription()).imageRef(newQuiz.getImageRef()).validated(false).activated(false).published(false).isFavourite(false).tagIdList(newQuiz.getTagList()).categoryIdList(newQuiz.getCategoryList()).build();

        quiz = quizDao.saveQuiz(quiz);

        System.out.println("Saved in db:" + quiz.toString());
        return quiz;
    }

    @Override
    public Quiz updateQuiz(DtoEditQuiz dtoQuiz) {
        Quiz quiz = Quiz.builder().title(dtoQuiz.getNewTitle()).creationDate(Calendar.getInstance().getTime()).creatorId(getQuiz(dtoQuiz.getQuizId()).getCreatorId()).language(dtoQuiz.getNewLanguage()).description(dtoQuiz.getNewDescription()).imageRef(dtoQuiz.getNewImageRef()).validated(false).activated(false).published(false).isFavourite(false).tagIdList(dtoQuiz.getNewTagList()).categoryIdList(dtoQuiz.getNewCategoryList()).build();
        return quizDao.updateQuiz(quiz, dtoQuiz.getQuizId());
    }

    @Override
    public Quiz getQuiz(String quizId) {
        Quiz quiz = quizDao.getQuiz(quizId);
        if(quiz.getImageRef() != null) {
            quiz.setImageContent(imageService.loadImage(quiz.getImageRef()));
        }
        return quiz;
    }

    @Override
    public Quiz getQuiz(String quizId, String userId) {
        Quiz quiz = quizDao.getQuiz(quizId, userId);
        if(quiz.getImageRef() != null) {
            quiz.setImageContent(imageService.loadImage(quiz.getImageRef()));
        }
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
        quizDao.validateQuiz(quiz);
    }

    @Override
    public void deleteQuizById(DtoQuiz dtoQuiz) {
        quizDao.deleteQuizById(dtoQuiz.getQuizId());
    }

    @Override
    public void deactivateQuiz(DtoQuiz dtoQuiz) {
        quizDao.deactivateQuiz(dtoQuiz);
    }

    @Override
    public Question saveQuestion(Question newQuestion) {
        newQuestion.setId(quizDao.saveQuestion(newQuestion));
        saveAnsw(newQuestion);
        return newQuestion;
    }

    @Override
    public Question updateQuestion(Question newQuestion) {
        quizDao.deleteQuestion(newQuestion);
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
    public List<Quiz> getUserQuizList(String userId) {
        return quizDao.getUserQuizList(userId);
    }

    @Override
    public List<QuizView> getQuizzes(int startIndex, int amount) {
        return quizDao.getQuizzes(startIndex, amount);
    }

    @Override
    public List<QuizValid> getInvalidQuizzes(int startIndex, int amount, String adminId) {
        return quizDao.getInvalidQuizzes(startIndex, amount,adminId);
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
        return quizDao.setValidator(quizId,adminId);
    }

    @Override
    public List<QuizFiltered> findQuizzesByFilter(DtoQuizFilter quizFilter, int startIndex, int amount) {
        return quizDao.findQuizzesByFilter(quizFilter, startIndex, amount);
    }

    @Override
    public List<QuizFiltered> shortListOfQuizzes() {
        DtoQuizFilter quizFilter = DtoQuizFilter.builder().moreThanRating(2).orderByRating(true).build();
        List<QuizFiltered> shortList = quizDao.findQuizzesByFilter(quizFilter, 0, 10);
        return shortList;
    }

    @Override
    public void addQuizImage(String imageId, String quizId) {
        quizDao.addQuizImage(imageId, quizId);
    }

    @Override
    public void addQuestionImage(String imageId, String questionId) {
        quizDao.addQuestionImage(imageId, questionId);
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
