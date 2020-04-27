package net.dreamfteam.quiznet.web.validators;

import net.dreamfteam.quiznet.data.entities.Question;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoQuiz;
import org.springframework.util.StringUtils;


public class QuizValidator {
    //EXCEPTION MESSAGES
    private static final String EMPTY_PROPERTY_EXCEPTION_MESSAGE = "Field '%s' must be provided";

    public static void validate(DtoQuiz quiz) {
        isNotEmpty(quiz.getTitle(), "title");
        isNotEmpty(quiz.getLanguage(), "language");
        isNotEmpty(quiz.getDescription(), "description");
        if(quiz.getTagList().size() == 0 || quiz.getCategoryList().size() == 0) {
            throw new ValidationException(String.format(EMPTY_PROPERTY_EXCEPTION_MESSAGE, "category/tag"));
        }
    }

    public static void validateForEdit(DtoQuiz quiz) {
        isNotEmpty(quiz.getNewTitle(), "new title");
        isNotEmpty(quiz.getNewLanguage(), "new language");
        isNotEmpty(quiz.getNewDescription(), "new description");
        isNotEmpty(quiz.getQuizId(), "quiz id");
    }

    public static void validateQuestion(Question question) {
        isNotEmpty(question.getTypeId(), "type");
        isNotEmpty(question.getQuizId(), "quiz id");
        isNotEmpty(question.getTitle(),"title");
        isNotEmpty(question.getContent(),"content");
        isNotEmpty(question.getPoints(),"points");
        for(int i = 0; i < question.getRightOptions().size(); i++) {
            isNotEmpty(question.getRightOptions().get(i), "answer");
        }
        for(int i = 0; i < question.getOtherOptions().size(); i++) {
            isNotEmpty(question.getOtherOptions().get(i), "answer");
        }
    }

    private static void isNotEmpty(Object value, String propertyName) {
        if (value == null || StringUtils.isEmpty(value)) {
            throw new ValidationException(String.format(EMPTY_PROPERTY_EXCEPTION_MESSAGE, propertyName));
        }
    }
}
