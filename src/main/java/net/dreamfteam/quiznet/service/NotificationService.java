package net.dreamfteam.quiznet.service;

import net.dreamfteam.quiznet.data.entities.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    public SseEmitter initSseEmitters();
    public void sendSseEventsToUI(String notification);

}