package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.exception.ValidationException;
import net.dreamfteam.quiznet.service.MailService;
import net.dreamfteam.quiznet.service.RecoveringService;
import net.dreamfteam.quiznet.service.UserService;
import net.dreamfteam.quiznet.web.dto.DtoChangePassword;
import net.dreamfteam.quiznet.web.dto.DtoMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import static javax.management.timer.Timer.ONE_DAY;

@Service
public class RecoveringServiceImpl implements RecoveringService {

    @Value("${recover.mail.url}")
    private String RECOVER_MAIL_URL;

    final private UserService userService;
    final private MailService mailService;
    final private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RecoveringServiceImpl(UserService userService, MailService mailService, BCryptPasswordEncoder passwordEncoder) {
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

        user.setRecoveryUrl(md5(userMail.getEmail() + Constants.SECRET_MD5));
        user.setRecoverySentTime(new Date());
        userService.update(user);

        mailService.sendMail(userMail.getEmail(), Constants.RECOVER_MAIL_SUBJECT, Constants.RECOVER_MAIL_SUBJECT,
                Constants.RECOVER_MAIL_MESSAGE + RECOVER_MAIL_URL + user.getRecoveryUrl());

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
    public void changePassword(@RequestBody DtoChangePassword passwordDto) {
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

    private static String md5(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(str.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toLowerCase();

    }

}
