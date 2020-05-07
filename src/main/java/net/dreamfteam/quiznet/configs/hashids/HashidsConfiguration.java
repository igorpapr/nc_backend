package net.dreamfteam.quiznet.configs.hashids;

import org.hashids.Hashids;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HashidsConfiguration {
    @Bean
    public Hashids setHashids() {
        return new Hashids("soltability");
    }
}
