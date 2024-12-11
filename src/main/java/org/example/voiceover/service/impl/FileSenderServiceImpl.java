package org.example.voiceover.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.voiceover.dto.FileMessageDto;
import org.example.voiceover.service.FileSenderService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileSenderServiceImpl implements FileSenderService {
    private final AmqpTemplate amqpTemplate;

    private final ObjectMapper objectMapper;

    @Value("${voiceover.output.exchange}")
    private String outputExchange;

    @Value("${voiceover.output.routing-key}")
    private String outputRoutingKey;

    @Override
    public void sendFileMessage(String correlationId, String fileUrl) {
        try {
            FileMessageDto dto = new FileMessageDto();
            dto.setCorrelationId(correlationId);
            dto.setFileType("voiceover");
            dto.setFileUrl(fileUrl);

            String message = objectMapper.writeValueAsString(dto);
            amqpTemplate.convertAndSend(outputExchange, outputRoutingKey, message);

            log.info("Sent file message to exchange: correlationId={}, fileUrl={}", correlationId, fileUrl);
        } catch (Exception e) {
            log.error("Failed to send file message", e);
        }
    }

}
