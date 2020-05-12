package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.service.GameConnectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import reactor.core.publisher.Flux;


@Controller
@CrossOrigin
@RequestMapping(Constants.SSE_URLS)
public class SseController {

    private GameConnectionsService gameConnectionsService;


    @Autowired
    public SseController(GameConnectionsService gameConnectionsService) {
        this.gameConnectionsService = gameConnectionsService;
    }

    // handle normal "Async timeout", to avoid logging warn messages every 30s per client...
    @ExceptionHandler(value = AsyncRequestTimeoutException.class)
    public String asyncTimeout(AsyncRequestTimeoutException e) {
        return null; // "SSE timeout..OK";
    }

    @GetMapping(path = "/stream/{key}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamFlux(@PathVariable String key) {
        return gameConnectionsService.subscribe(key);
    }

}