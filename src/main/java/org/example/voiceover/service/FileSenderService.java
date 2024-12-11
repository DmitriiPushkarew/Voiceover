package org.example.voiceover.service;

public interface FileSenderService {
    void sendFileMessage(String correlationId, String fileUrl);
}