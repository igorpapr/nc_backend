package net.dreamfteam.quiznet.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface GameConnectionsService {

    public SseEmitter initSseEmitters();
    public void sendSseEventsToUI(String notification);

}