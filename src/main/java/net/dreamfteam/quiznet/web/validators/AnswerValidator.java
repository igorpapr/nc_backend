package net.dreamfteam.quiznet.web.validators;

import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoAnswer;
import org.springframework.util.StringUtils;

public class AnswerValidator {
    private static final String EMPTY_PROPERTY_EXCEPTION_MESSAGE = "Field '%s' must be provided";

    public static void validate(DtoAnswer dtoAnswer) {
        System.out.println(dtoAnswer.toString());
        isNotEmpty(dtoAnswer.getSessionId(), "game session id");
        if (dtoAnswer.getAnswer().size() == 0) {
            throw new ValidationException("Field answer's size must be greater than zero ");

        }
        isNotEmpty(dtoAnswer.getTimeOfAnswer(), "time of answer");
        isNotEmpty(dtoAnswer.getTypeId(), "type of question");

    }

    private static void isNotEmpty(Object value, String propertyName) {
        if (value == null || StringUtils.isEmpty(value)) {
            throw new ValidationException(String.format(EMPTY_PROPERTY_EXCEPTION_MESSAGE, propertyName));
        }
    }
}
