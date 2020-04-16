package net.dreamfteam.quiznet.web.validators;


import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.web.dto.LoginRequest;
import org.springframework.util.StringUtils;

public class LoginRequestValidator {

    private static final String EMPTY_PROPERTY_EXCEPTION_MESSAGE = "LoginRequest field parameter '%s' must be provided";

    public static void validate(LoginRequest loginRequest) throws ValidationException {
        validateNotEmptyProperty(loginRequest.getUsername(),"username");
        validateNotEmptyProperty(loginRequest.getPassword(),"password");
    }

    private static void validateNotEmptyProperty(Object value, String propertyName) {
        if (value == null || StringUtils.isEmpty(value)) {
            throw new ValidationException(String.format(EMPTY_PROPERTY_EXCEPTION_MESSAGE, propertyName));
        }
    }
}
