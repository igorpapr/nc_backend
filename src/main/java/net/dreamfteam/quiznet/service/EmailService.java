package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.web.dto.Mail;
import net.dreamfteam.quiznet.data.entities.User;

import javax.mail.MessagingException;

public interface EmailService {
    Mail createRecoverMail(User user);

    Mail createBasicRegMail(User savedUser);

    void sendMailMessage(Mail mail, String templateName) throws MessagingException;
}
