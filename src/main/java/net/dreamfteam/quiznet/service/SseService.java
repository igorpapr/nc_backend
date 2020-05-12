package net.dreamfteam.quiznet.service;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface SseService<T> {

    public Flux<ServerSentEvent<T>> subscribe(String key);
    public void send(String key, T obj);

}