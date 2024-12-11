package org.example.voiceover.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue voiceoverQueue() {
        return new Queue("voiceover_queue", true);
    }

}