package net.dreamfteam.quiznet.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.constants.Constants;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.RecoveringService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.DtoForgotPassword;
import net.dreamfteam.quiznet.web.dto.DtoMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import java.util.Date;

import static javax.management.timer.Timer.ONE_DAY;

@Slf4j
@Service
public class RecoveringServiceImpl implements RecoveringService {

    @Value("${recover.secret.key}")
    private String recoverSecret;

    @Value("${recover.template}")
    private String recoverNameTemplate;

    final private UserService userService;
    final private EmailServiceImpl mailService;
    final private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RecoveringServiceImpl(UserService userService, EmailServiceImpl mailService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void sendRecoveringUrl(DtoMail userMail) {

        User user = userService.getByEmail(userMail.getEmail());

        if (user == null) {
            throw new ValidationException(Constants.USER_NOT_FOUND_WITH_EMAIL + userMail.getEmail());
        }

        user.setRecoveryUrl(passwordEncoder.encode(userMail.getEmail() + recoverSecret));
        user.setRecoverySentTime(new Date());
        userService.update(user);

        try {
            mailService.sendMailMessage(mailService.createRecoverMail(user), recoverNameTemplate);
        } catch (MessagingException e) {
            log.error(String.format(Constants.RECOVERY_MAIL_NOT_SENT, user.getUsername()), e);
        }

    }

    @Override
    public String confirmRecovery(String recoverUrl) {
        User user = userService.getByRecoverUrl(recoverUrl);

        if (user == null) {
            throw new ValidationException(Constants.USER_NOT_FOUND_WITH_RECOVER_URL + recoverUrl);
        }

        if (new Date().getTime() - user.getRecoverySentTime().getTime() >= ONE_DAY) {
            user.setRecoveryUrl(null);
            user.setRecoverySentTime(null);
            userService.update(user);
            throw new ValidationException(Constants.RECOVER_LINK_EXPIRED);
        }

        return recoverUrl;
    }

    @Override
    public void changePassword(@RequestBody DtoForgotPassword passwordDto) {
        User user = userService.getByRecoverUrl(passwordDto.getRecoverUrl());

        if (user == null) {
            throw new ValidationException(Constants.USER_NOT_FOUND_WITH_RECOVER_URL + passwordDto.getRecoverUrl());
        }

        if (new Date().getTime() - user.getRecoverySentTime().getTime() >= ONE_DAY) {
            user.setRecoveryUrl(null);
            user.setRecoverySentTime(null);
            userService.update(user);
            throw new ValidationException(Constants.RECOVER_LINK_EXPIRED);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRecoveryUrl(null);
        user.setRecoverySentTime(null);
        userService.update(user);
    }

}
