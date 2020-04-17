package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.data.entities.Quiz;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;

import java.util.List;

public interface QuizService {

    Quiz saveQuiz(DtoQuiz newQuiz) throws ValidationException;

    Quiz updateQuiz(DtoQuiz quiz);

    Quiz getQuiz(DtoQuiz quiz);

    Question saveQuestion(Question newQuestion);

    Question updateQuestion(Question newQuestion);

    void deleteQuestion(Question question);

    List<Question> getQuestionList(Question question);
}
