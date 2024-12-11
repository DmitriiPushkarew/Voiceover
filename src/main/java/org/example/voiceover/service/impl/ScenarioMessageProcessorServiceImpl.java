package org.example.voiceover.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.voiceover.dto.ScenarioMessageForVoiceoverDto;
import org.example.voiceover.service.AutomaticVoiceoverService;
import org.example.voiceover.service.FileSenderService;
import org.example.voiceover.service.ScenarioMessageProcessorService;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ScenarioMessageProcessorServiceImpl implements ScenarioMessageProcessorService {
    private final AutomaticVoiceoverService automaticVoiceoverService;
    private final FileSenderService fileSender;

    public void processScenarioMessageContent(ScenarioMessageForVoiceoverDto dto) {
        Long answerId = dto.getAnswerId();
        String content = dto.getAnswerContent();
        log.info("Processing content for answerId: {}", answerId);
        String filePath = automaticVoiceoverService.processVoiceover(content);
        if (filePath != null) {
            fileSender.sendFileMessage(answerId.toString(), filePath);
        } else {
            log.warn("No file was downloaded for answerId {}", answerId);
        }
    }
}
