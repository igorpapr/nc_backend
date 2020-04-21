package net.dreamfteam.quiznet.web.validators;


import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.DtoAdminActivation;
import net.dreamfteam.quiznet.web.dto.DtoAdminSignUp;
import net.dreamfteam.quiznet.web.dto.DtoUserSignUp;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    //Messages
    private static final String EMPTY_PROPERTY_EXCEPTION_MESSAGE = "User field parameter '%s' must be provided";
    private static final String REGEX_EXCEPTION_MESSAGE = "User field parameter '%s' must match these parameters: '%s'";

    //Regex
    private static final String REGEX_PASSWORD = "^(?=.*\\d).{4,28}$";
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    private static final String REGEX_USERNAME = "^[a-zA-Z0-9][a-zA-Z0-9-_]{2,18}$";
    private static final String REGEX_ROLE = "^ROLE_[A-Z]+$";


    public static void validate(DtoUserSignUp user) throws ValidationException {
        validateNotEmptyProperty(user.getUsername(), "username");
        validateNotEmptyProperty(user.getPassword(), "password");
        validateNotEmptyProperty(user.getEmail(), "email");
        validateWithRegularExpression(user.getUsername(), REGEX_USERNAME, "username");
        validateWithRegularExpression(user.getPassword(), REGEX_PASSWORD, "password");
        validateWithRegularExpression(user.getEmail(), REGEX_EMAIL, "email");
    }

    public static void validate(DtoAdminActivation admin) throws ValidationException {
        validateNotEmptyProperty(admin.getId(), "id");
        validateNotEmptyProperty(admin.isActivated(),"activated");

    }


    public static void validate(DtoAdminSignUp admin) throws ValidationException {
        validateNotEmptyProperty(admin.getUsername(), "username");
        validateNotEmptyProperty(admin.getPassword(), "password");
        validateNotEmptyProperty(admin.getEmail(), "email");
        validateNotEmptyProperty(admin.getRole(), "role");
        validateWithRegularExpression(admin.getRole(), REGEX_ROLE, "role");
        validateWithRegularExpression(admin.getUsername(), REGEX_USERNAME, "username");
        validateWithRegularExpression(admin.getPassword(), REGEX_PASSWORD, "password");
        validateWithRegularExpression(admin.getEmail(), REGEX_EMAIL, "email");
    }


    private static void validateNotEmptyProperty(Object value, String propertyName) {
        if (value == null || StringUtils.isEmpty(value)) {
            throw new ValidationException(String.format(EMPTY_PROPERTY_EXCEPTION_MESSAGE, propertyName));
        }
    }

    private static void validateWithRegularExpression(Object value, String regex, String propertyName) {

        Matcher matcher = Pattern.compile(regex).matcher(String.valueOf(value));
        if (!matcher.matches()) {
            throw new ValidationException(String.format(REGEX_EXCEPTION_MESSAGE, propertyName, regex));
        }

    }
}
