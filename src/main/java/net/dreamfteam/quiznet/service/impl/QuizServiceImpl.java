package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.dao.QuizDao;
import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.data.entities.Quiz;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.QuizService;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class QuizServiceImpl implements QuizService {

    private QuizDao quizDao;

    @Autowired
    public QuizServiceImpl(QuizDao quizDao) {
        this.quizDao = quizDao;
    }

    @Override
    public Quiz saveQuiz(DtoQuiz newQuiz) throws ValidationException {
        checkQuizUniqueness(newQuiz.getTitle(), newQuiz.getCreatorId());
        Quiz quiz = Quiz.builder().title(newQuiz.getTitle()).creationDate(Calendar.getInstance().getTime()).creatorId(newQuiz.getCreatorId()).language(newQuiz.getLanguage()).description(newQuiz.getDescription()).imageRef(newQuiz.getImageRef()).validated(false).activated(false).published(false).isFavourite(false).tagIdList(newQuiz.getTagList()).categoryIdList(newQuiz.getCategoryList()).build();

        quiz = quizDao.saveQuiz(quiz);

        System.out.println("Saved in db:" + quiz.toString());
        return quiz;
    }

    @Override
    public Quiz updateQuiz(DtoQuiz dtoQuiz) {
        Quiz quiz = Quiz.builder().title(dtoQuiz.getNewTitle()).creationDate(Calendar.getInstance().getTime()).creatorId(dtoQuiz.getCreatorId()).language(dtoQuiz.getNewLanguage()).description(dtoQuiz.getNewDescription()).imageRef(dtoQuiz.getNewImageRef()).validated(false).activated(false).published(false).isFavourite(false).tagIdList(dtoQuiz.getNewTagList()).categoryIdList(dtoQuiz.getNewCategoryList()).build();

        return quizDao.updateQuiz(quiz, dtoQuiz.getQuizId());
    }

    @Override
    public Quiz getQuiz(String quizId, String userId) {
        return quizDao.getQuiz(quizId, userId);
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
