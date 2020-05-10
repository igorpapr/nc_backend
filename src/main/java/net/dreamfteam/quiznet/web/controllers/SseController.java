package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.service.impl.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(Constants.SSE_URLS)
public class SseController {

    private NotificationServiceImpl INotificationServiceImpl;

    @Autowired
    public SseController(NotificationServiceImpl INotificationServiceImpl) {
        this.INotificationServiceImpl = INotificationServiceImpl;
    }

    @GetMapping("/stream")
    public SseEmitter stream() throws IOException {
        return INotificationServiceImpl.initSseEmitters();
    }
}