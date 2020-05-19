package net.dreamfteam.quiznet.service;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;

import java.util.Map;

public interface SseService {

    Flux<ServerSentEvent> subscribe(String key);

    void send(String key, String event, String id);

    void send(String key, String event);

    void remove(String key);

    Map<String, ReplayProcessor<ServerSentEvent>> getSseMap();

}