package org.example.voiceover.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class VoiceoverMessageDto {

    @JsonProperty("answerId")
    private Long answerId;

    @JsonProperty("answerContent")
    private String answerContent;
}