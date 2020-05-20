package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.token.JwtTokenProvider;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ActivationService;
import net.dreamfteam.quiznet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static javax.management.timer.Timer.ONE_DAY;

@Service
public class ActivationServiceImpl implements ActivationService {

    final private JwtTokenProvider tokenProvider;
    final private UserService userService;
    private static final String MESSAGE_ALREADY_ACTIVATED = "Already activated. Please, log in";
    private static final String MESSAGE_LINK_EXPIRED = "Your activation link is expired. Please, create a new account";
    private static final String MESSAGE_ACTIVATED = "Successfully activated. Please, log in";

    @Autowired
    public ActivationServiceImpl(JwtTokenProvider tokenProvider, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }


    @Override
    public String verifyUser(String activationUrl) {

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
    public String isUserVerified(User user) {

        if (user == null) {
            throw new ValidationException("User with such username not found");
        }

        if (!user.isVerified()) {
            throw new ValidationException("Your profile is not activated");
        }

        user.setLastTimeOnline(new Date());
        userService.update(user);

        return tokenProvider.provideToken(user.getUsername());
    }

    @Override
    public String getAnonymToken(String username) {
        return tokenProvider.provideTokenForAnonym(username);
    }


}
