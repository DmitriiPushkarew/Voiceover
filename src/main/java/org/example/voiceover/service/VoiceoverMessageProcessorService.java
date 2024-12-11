package org.example.voiceover.service;

import org.example.voiceover.dto.ScenarioMessageForVoiceoverDto;

public interface VoiceoverMessageProcessorService {

    void processScenarioMessageContent(ScenarioMessageForVoiceoverDto dto);

}