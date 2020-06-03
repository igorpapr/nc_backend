package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.web.dto.Mail;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String from;

    @Value("${recover.mail.subject}")
    private String recoverMailSubject;

    @Value("${recover.mail.url}")
    private String recoverMailUrl;

    @Value("${reg.url.activate}")
    private String regUrlActivate;

    @Value("${reg.mail.subject}")
    private String regMailSubject;


    final private JavaMailSender emailSender;

    final private SpringTemplateEngine templateEngine;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender, SpringTemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    public void sendMailMessage(Mail mail, String templateName) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariables(mail.getModel());
        String html = templateEngine.process(templateName, context);

        helper.setTo(mail.getTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(from);

        emailSender.send(message);
    }

    @Override
    public Mail createRecoverMail(User user){
        Mail mail = new Mail();
        mail.setTo(user.getEmail());
        mail.setSubject(recoverMailSubject);

        Map<String, Object> model = new HashMap<>();
        model.put("link", recoverMailUrl + user.getRecoveryUrl());
        model.put("username", user.getUsername());
        mail.setModel(model);
        return mail;
    }

    @Override
    public Mail createBasicRegMail(User user) {
        Mail mail = new Mail();
        mail.setTo(user.getEmail());
        mail.setSubject(regMailSubject);

        Map<String, Object> model = new HashMap<>();
        model.put("username", user.getUsername());
        model.put("link", regUrlActivate + user.getActivationUrl());

        mail.setModel(model);
        return mail;
    }
}
