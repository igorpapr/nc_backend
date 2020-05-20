package net.dreamfteam.quiznet.web.validators;

import net.dreamfteam.quiznet.exception.ValidationException;
import org.springframework.util.StringUtils;

public class UserQuizRatingValidator {

    private static final String EMPTY_PROPERTY_EXCEPTION_MESSAGE = "Field '%s' must be provided";

    public static void validate(String sessionId, int points) throws ValidationException {
        validateNotEmptyProperty(sessionId, "game session id");
        validateNotEmptyProperty(points, "points");
        if (points < 1 || points > 5) {
            throw new ValidationException(String.format("Rating must be between in the range of [1, 5], got %d", points));
        }

    }

    private static void validateNotEmptyProperty(Object value, String propertyName) {
        if (value == null || StringUtils.isEmpty(value)) {
            throw new ValidationException(String.format(EMPTY_PROPERTY_EXCEPTION_MESSAGE, propertyName));
        }
    }

}
