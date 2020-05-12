package net.dreamfteam.quiznet.web.controllers;

import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.security.AuthenticationFacade;
import net.dreamfteam.quiznet.service.impl.GameConnectionsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;


@Controller
@CrossOrigin
@RequestMapping(Constants.SSE_URLS)
public class SseController {

    private GameConnectionsServiceImpl IGameConnectionsServiceImpl;


    @Autowired
    public SseController(GameConnectionsServiceImpl IGameConnectionsServiceImpl) {
        this.IGameConnectionsServiceImpl = IGameConnectionsServiceImpl;
    }

    // handle normal "Async timeout", to avoid logging warn messages every 30s per client...
    @ExceptionHandler(value = AsyncRequestTimeoutException.class)
    public String asyncTimeout(AsyncRequestTimeoutException e){
        return null; // "SSE timeout..OK";
    }

    @GetMapping(path = "/stream/{key}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamFlux(@PathVariable String key) {
        return IGameConnectionsServiceImpl.subscribe(key);
    }

}