package org.example.voiceover.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.voiceover.dto.VoiceoverMessageDto;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class VoiceoverProcessorService {
    private final VoiceoverAutomationService automationService;
    private final FileSenderService fileSender;

    public void processVoiceoverContent(VoiceoverMessageDto dto) {
        Long answerId = dto.getAnswerId();
        String content = dto.getAnswerContent();
        log.info("Processing content for answerId: {}", answerId);
        String filePath = automationService.processVoiceover(content);
        if (filePath != null) {
            fileSender.sendFileMessage(answerId.toString(), filePath);
        } else {
            log.warn("No file was downloaded for answerId {}", answerId);
        }
    }
}
