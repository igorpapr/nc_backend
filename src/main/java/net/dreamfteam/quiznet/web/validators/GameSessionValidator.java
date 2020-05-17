package net.dreamfteam.quiznet.web.validators;

import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoGame;
import net.dreamfteam.quiznet.web.dto.DtoGameSession;
import org.springframework.util.StringUtils;

public class GameSessionValidator {
    private static final String EMPTY_PROPERTY_EXCEPTION_MESSAGE = "Field '%s' must be provided";

    public static void validate(DtoGameSession gameSession) {
        isNotEmpty(gameSession.getDurationTime(), "duration time");
        isNotEmpty(gameSession.getScore(), "score");
        isNotEmpty(gameSession.getSessionId(), "session id");
    }

    private static void isNotEmpty(Object value, String propertyName) {
        if (value == null || StringUtils.isEmpty(value)) {
            throw new ValidationException(String.format(EMPTY_PROPERTY_EXCEPTION_MESSAGE, propertyName));
        }
    }
}
