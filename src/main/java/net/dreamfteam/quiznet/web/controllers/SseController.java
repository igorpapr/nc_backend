package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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


    private final SseService<User> gameConnectorSseService;
    private final SseService<User> readyForGameSseService;


    @Autowired
    public SseController(ApplicationContext context) {
        this.gameConnectorSseService = (SseService<User>) context.getBean("gameConnector");
        this.readyForGameSseService = (SseService<User>) context.getBean("readyForGame");
    }


    // handle normal "Async timeout", to avoid logging warn messages every 30s per client...
    @ExceptionHandler(value = AsyncRequestTimeoutException.class)
    public String asyncTimeout(AsyncRequestTimeoutException e) {
        return null; // "SSE timeout..OK";
    }

    @GetMapping(path = "/stream/game-connector/{key}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<User>> gameConnectorFlux(@PathVariable String key) {
        return gameConnectorSseService.subscribe(key);
    }

    @GetMapping(path = "/stream/users-ready/{key}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<User>> readyForGameFlux(@PathVariable String key) {
        return readyForGameSseService.subscribe(key);
    }

}