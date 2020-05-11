package net.dreamfteam.quiznet.web.validators;

import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoAnnouncement;
import net.dreamfteam.quiznet.web.dto.DtoEditAnnouncement;
import net.dreamfteam.quiznet.web.dto.DtoNotification;
import org.springframework.util.StringUtils;

public class NotificationValidator {
    private static final String EMPTY_PROPERTY_EXCEPTION_MESSAGE = "Field '%s' must be provided";

    public static void validate(DtoNotification notification) {
        isNotEmpty(notification.getContent(), "content");
        isNotEmpty(notification.getUserId(), "user id");
    }


    private static void isNotEmpty(Object value, String propertyName) {
        if (value == null || StringUtils.isEmpty(value)) {
            throw new ValidationException(String.format(EMPTY_PROPERTY_EXCEPTION_MESSAGE, propertyName));
        }
    }
}
