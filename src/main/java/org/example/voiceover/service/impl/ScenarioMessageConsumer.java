package org.example.voiceover.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.voiceover.dto.ScenarioMessageForVoiceoverDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScenarioMessageConsumer {

    private final ObjectMapper objectMapper;
    private final ScenarioMessageProcessorServiceImpl processorService;

    @RabbitListener(queues = "voiceover_queue")
    public void processMessage(String message) {
        log.info("Received message from voiceover_queue: {}", message);
        try {
            ScenarioMessageForVoiceoverDto dto = objectMapper.readValue(message, ScenarioMessageForVoiceoverDto.class);
            processorService.processScenarioMessageContent(dto);
        } catch (Exception e) {
            log.error("Failed to parse message", e);
        }
    }
}