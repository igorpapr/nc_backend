package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.token.JwtTokenProvider;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.ActivationService;
import net.dreamfteam.quiznet.service.MailService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

import static javax.management.timer.Timer.ONE_DAY;

@Service
public class ActivationServiceImpl implements ActivationService {

    private JwtTokenProvider tokenProvider;
    private UserService userService;

    @Autowired
    public ActivationServiceImpl(JwtTokenProvider tokenProvider, UserService userService, MailService mailService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }


    @Override
    public void activateUser(String activationUrl) {

        User user = userService.getByActivationUrl(activationUrl);

        if (user == null) {
            throw new ValidationException("Not Found");
        }

        if (user.isVerified()) {
            return;
        }

//        if (new Date().getTime()- user.getCreationDate().getTime() >= ONE_DAY) {
//            userService.deleteById(user.getId());
//            throw new ValidationException("Your activation link is expired. Create your account again");
//        }

        user.setVerified(true);
        user.setActivated(true);

        userService.update(user);
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
