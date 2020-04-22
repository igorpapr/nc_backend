package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.data.entities.Quiz;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;

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
}
