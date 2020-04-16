package net.dreamfteam.quiznet.service;

public interface MailService {

    void sendMail(String userEmail, String subject, String article, String messageText);

}
