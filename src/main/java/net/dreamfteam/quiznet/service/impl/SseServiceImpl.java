package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.service.SseService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class SseServiceImpl<T> implements SseService<T> {

    private Map<String, ReplayProcessor<ServerSentEvent<T>>> sse = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Flux<ServerSentEvent<T>> subscribe(String key) {
        if (sse.get(key) == null) sse.put(key, ReplayProcessor.create());
        return sse.get(key);
    }


    @Override
    public void send(String key, T obj) {
        ServerSentEvent<T> event = ServerSentEvent.builder(obj).build();
        sse.get(key).onNext(event);
    }


}