package org.example.voiceover.service;

import org.example.voiceover.dto.ScenarioMessageForVoiceoverDto;

public interface ScenarioMessageProcessorService {
    void processScenarioMessageContent(ScenarioMessageForVoiceoverDto dto);
}