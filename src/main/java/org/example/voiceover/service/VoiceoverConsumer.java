package org.example.voiceover.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.voiceover.dto.VoiceoverMessageDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoiceoverConsumer {

    private final ObjectMapper objectMapper;
    private final VoiceoverProcessorService processorService;

    @RabbitListener(queues = "voiceover_queue")
    public void processMessage(String message) {
        log.info("Received message from voiceover_queue: {}", message);
        try {
            VoiceoverMessageDto dto = objectMapper.readValue(message, VoiceoverMessageDto.class);
            processorService.processVoiceoverContent(dto);
        } catch (Exception e) {
            log.error("Failed to parse message", e);
        }
    }
}