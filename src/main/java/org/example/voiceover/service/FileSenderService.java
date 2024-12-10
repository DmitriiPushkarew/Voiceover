package org.example.voiceover.service;

import com.example.videocreator.dto.FileMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileSenderService {

    private final AmqpTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    @Value("${voiceover.output.exchange}")
    private String fileExchange;

    @Value("${voiceover.output.routingKey}")
    private String videoRoutingKey;

    public void sendFileMessage(String correlationId, String fileUrl) {
        try {
            FileMessageDto dto = new FileMessageDto();
            dto.setCorrelationId(correlationId);
            dto.setFileType("voiceover");
            dto.setFileUrl(fileUrl);

            String message = objectMapper.writeValueAsString(dto);
            amqpTemplate.convertAndSend(fileExchange, videoRoutingKey, message);
            log.info("Sent file message to video_exchange: correlationId={}, fileUrl={}", correlationId, fileUrl);
        } catch (Exception e) {
            log.error("Failed to send file message", e);
        }
    }
}
