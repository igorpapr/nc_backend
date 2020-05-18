package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.service.SseService;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class SseServiceImpl implements SseService {

    private static final int HISTORY_AMOUNT_REPLAY_PROCESSOR = 10;
    private final Map<String, ReplayProcessor<ServerSentEvent>> sse = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Flux<ServerSentEvent> subscribe(String key) {
        return getOrCreate(key);
    }


    @Override
    public void send(String key, String event, String id) {
        ServerSentEvent ssEvent = ServerSentEvent.builder(id)
                                                 .event(event)
                                                 .build();
        getOrCreate(key).onNext(ssEvent);
    }

    @Override
    public void send(String key, String event) {
        ServerSentEvent ssEvent = ServerSentEvent.builder(event)
                                                 .event(event)
                                                 .build();
        getOrCreate(key)
           .onNext(ssEvent);
    }

    @Override
    public void remove(String key) {
        if (sse.get(key) != null)
            sse.remove(key);
    }

    private ReplayProcessor<ServerSentEvent> getOrCreate(String key) {
        if (sse.get(key) == null)
            sse.put(key, ReplayProcessor.create(SseServiceImpl.HISTORY_AMOUNT_REPLAY_PROCESSOR));
        return sse.get(key);
    }


}