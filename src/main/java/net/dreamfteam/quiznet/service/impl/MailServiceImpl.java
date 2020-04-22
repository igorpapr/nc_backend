package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

    private JavaMailSender emailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendMail(String userEmail, String subject, String article, String messageText) {

        //TODO throw wrapped exception then mail not exist, cannot send message for this address

        new Thread(() -> {
            try {
                String content = "<h3>" + article + "</h3>" +
                        "<p style='margin-bottom:15px'>" + messageText + "</p>" +
                        "<img src='https://bsmedia.business-standard.com/media-handler.php?mediaPath=https://bsmedia.business-standard.com/_media/bs/img/article/2019-11/03/full/1572796865-0693.jpg&width=1200'>";

                MimeMessage message = emailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
                message.setContent(content, "text/html");
                helper.setTo(userEmail);
                helper.setSubject(subject);
                emailSender.send(message);
            }

            catch (MessagingException e) {
                e.printStackTrace();
            }

        }).start();
    }

}
