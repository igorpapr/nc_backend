package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.entities.User;
import net.dreamfteam.quiznet.service.SseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class SseServicesBeans {

    @Bean(name = "gameConnector")
    public SseService<User> gameConnector() {
        return new SseServiceImpl<>();
    }

    @Bean(name = "readyForGame")
    public SseService<User> readyForGame() {
        return new SseServiceImpl<>();
    }

}
