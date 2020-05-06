package net.dreamfteam.quiznet.web.validators;

import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoGame;
import org.springframework.util.StringUtils;

public class GameValidator {

    private static final String EMPTY_PROPERTY_EXCEPTION_MESSAGE = "Field '%s' must be provided";

    public static void validate(DtoGame game) {
        isNotEmpty(game.getBreakTime(), "break time");
        isNotEmpty(game.getMaxUsersCount(), "maximum users number");
        isNotEmpty(game.getNumberOfQuestions(), "maximum question number");
        isNotEmpty(game.getRoundDuration(), "round duration");
        isNotEmpty(game.isAdditionalPoints(), "additional points");
        isNotEmpty(game.getQuizId(), "quiz id");
    }

    public static void validateUpdate(DtoGame game) {
        isNotEmpty(game.getStartDatetime(), "start datetime");
        isNotEmpty(game.getId(), "game id");
        isNotEmpty(game.getBreakTime(), "break time");
        isNotEmpty(game.getMaxUsersCount(), "maximum users number");
        isNotEmpty(game.getNumberOfQuestions(), "maximum question number");
        isNotEmpty(game.getRoundDuration(), "round duration");
        isNotEmpty(game.isAdditionalPoints(), "additional points");
        isNotEmpty(game.getQuizId(), "quiz id");
    }


    private static void isNotEmpty(Object value, String propertyName) {
        if (value == null || StringUtils.isEmpty(value)) {
            throw new ValidationException(String.format(EMPTY_PROPERTY_EXCEPTION_MESSAGE, propertyName));
        }
    }


}
