package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.token.JwtTokenProvider;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ActivationService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static javax.management.timer.Timer.ONE_DAY;

@Service
public class ActivationServiceImpl implements ActivationService {

    private JwtTokenProvider tokenProvider;
    private UserService userService;
    public static final String MESSAGE_ALREADY_ACTIVATED = "Already activated. Please log in" ;
    public static final String MESSAGE_LINK_EXPIRED = "Already activated. Please log in" ;
    public static final String MESSAGE_ACTIVATED = "Successfully activated. Please log in" ;

    @Autowired
    public ActivationServiceImpl(JwtTokenProvider tokenProvider, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }


    @Override
    public String activateUser(String activationUrl) {

        User user = userService.getByActivationUrl(activationUrl);

        if (user == null) {
            throw new ValidationException("Not Found");
        }

        //return to log in with message that already activated
        if (user.isVerified()) {

            return MESSAGE_ALREADY_ACTIVATED;
        }

        //return to log in with message that activation link have been expired
        if (new Date().getTime() - user.getCreationDate().getTime() >= ONE_DAY) {
            userService.deleteById(user.getId());
            return MESSAGE_LINK_EXPIRED;
        }

        user.setVerified(true);
        user.setActivated(true);
        userService.update(user);

        return MESSAGE_ACTIVATED;

    }


    @Override
    public String isUserActivated(LoginRequest loginRequest) {

        User user = userService.getByUsername(loginRequest.getUsername());

        if (user == null) {
            throw new ValidationException("User with such username not found");
        }

        if (!user.isActivated()) {
            throw new ValidationException("Your profile is not activated");
        }

        return tokenProvider.provideToken(user.getUsername());
    }


}
