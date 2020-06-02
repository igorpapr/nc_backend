package net.dreamfteam.quiznet.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.dreamfteam.quiznet.configs.mail.Mail;
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
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static javax.management.timer.Timer.ONE_DAY;

@Slf4j
@Service
public class RecoveringServiceImpl implements RecoveringService {

    @Value("${recover.mail.url}")
    private String recoverMailUrl;

    @Value("${md5.secret.key}")
    private String secretMd5;

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
            throw new ValidationException("Not found user with such email");
        }

        user.setRecoveryUrl(md5(userMail.getEmail() + secretMd5));
        user.setRecoverySentTime(new Date());
        userService.update(user);

        Mail mail = new Mail();
        mail.setTo("vasilakur@gmail.com");
        mail.setSubject("QuizNet Forgot password");

        Map<String, String> model = new HashMap<>();
        model.put("link", recoverMailUrl + user.getRecoveryUrl());
        model.put("name", user.getUsername());
        mail.setModel(model);

        try {
            mailService.sendSimpleMessage(mail, recoverNameTemplate);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String confirmRecovery(String recoverUrl) {
        User user = userService.getByRecoverUrl(recoverUrl);

        if (user == null) {
            throw new ValidationException("User with such recover URL not found");
        }

        //return to form with message that recovery link have been expired
        if (new Date().getTime() - user.getRecoverySentTime().getTime() >= ONE_DAY) {
            user.setRecoveryUrl(null);
            user.setRecoverySentTime(null);
            userService.update(user);
            throw new ValidationException("Your recover link is expired. Try again");
        }

        return recoverUrl;
    }

    @Override
    public void changePassword(@RequestBody DtoForgotPassword passwordDto) {
        User user = userService.getByRecoverUrl(passwordDto.getRecoverUrl());

        if (user == null) {
            throw new ValidationException("Not found user with such recover url");
        }

        if (new Date().getTime() - user.getRecoverySentTime().getTime() >= ONE_DAY) {
            user.setRecoveryUrl(null);
            user.setRecoverySentTime(null);
            userService.update(user);
            throw new ValidationException("Your recover link is expired. Try again");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRecoveryUrl(null);
        user.setRecoverySentTime(null);
        userService.update(user);
    }

    private String md5(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("No Such Algorithm", e);
        }
        md.update(str.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toLowerCase();

    }

}
