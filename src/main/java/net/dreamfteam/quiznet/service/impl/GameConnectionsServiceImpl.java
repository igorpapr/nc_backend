package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.service.GameConnectionsService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;

import java.io.IOException;
import java.util.*;

@Service
public class GameConnectionsServiceImpl implements GameConnectionsService {

    private Map<String, ReplayProcessor<ServerSentEvent<String>>> sse = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Flux<ServerSentEvent<String>> subscribe(String key) {
        if (sse.get(key) == null) sse.put(key, ReplayProcessor.create());
        return sse.get(key);
    }


    @Override
    public void sendMsg(String key, String msg) {
        ServerSentEvent<String> event = ServerSentEvent.builder(msg).build();
        sse.get(key).onNext(event);
    }
}