package org.example.voiceover.service;

public interface AutomaticVoiceoverService {
    String processVoiceover(String text);
    void closeBrowser();
}