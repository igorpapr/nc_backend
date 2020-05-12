package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.service.impl.GameConnectionsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(Constants.SSE_URLS)
public class SseController {

    private GameConnectionsServiceImpl INotificationServiceImpl;

    @Autowired
    public SseController(GameConnectionsServiceImpl INotificationServiceImpl) {
        this.INotificationServiceImpl = INotificationServiceImpl;
    }

    @GetMapping("/stream")
    public SseEmitter stream() throws IOException {
        return INotificationServiceImpl.initSseEmitters();
    }
}