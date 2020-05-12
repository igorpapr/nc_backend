package net.dreamfteam.quiznet.service;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface GameConnectionsService {

    public Flux<ServerSentEvent<String>> subscribe(String key);
    public void sendMsg(String key, String notification);

}