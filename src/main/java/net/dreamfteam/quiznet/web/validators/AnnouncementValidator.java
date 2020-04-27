package net.dreamfteam.quiznet.web.validators;

import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoAnnouncement;
import net.dreamfteam.quiznet.web.dto.DtoEditAnnouncement;
import org.springframework.util.StringUtils;

public class AnnouncementValidator {

    private static final String EMPTY_PROPERTY_EXCEPTION_MESSAGE = "Field '%s' must be provided";

    public static void validate(DtoAnnouncement announcement) {
        isNotEmpty(announcement.getTitle(), "title");
        isNotEmpty(announcement.getCreatorId(), "creator id");
        isNotEmpty(announcement.getTextContent(), "text content");
    }


    public static void validateForEdit(DtoEditAnnouncement announcement) {
        isNotEmpty(announcement.getTitle(), "title");
        isNotEmpty(announcement.getCreatorId(), "creator id");
        isNotEmpty(announcement.getAnnouncementId(), "announcement id");
        isNotEmpty(announcement.getTextContent(), "text content");
    }

    private static void isNotEmpty(Object value, String propertyName) {
        if (value == null || StringUtils.isEmpty(value)) {
            throw new ValidationException(String.format(EMPTY_PROPERTY_EXCEPTION_MESSAGE, propertyName));
        }
    }
}
