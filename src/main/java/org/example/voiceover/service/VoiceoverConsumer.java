package org.example.voiceover.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VoiceoverConsumer {

    @RabbitListener(queues = "voiceover_queue")
    public void processMessage(String message) {
        log.info("Received message voiceover_queue: {}", message);
    }
}
