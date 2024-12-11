package org.example.voiceover.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileMessageDto {
    @JsonProperty("correlationId")
    private String correlationId;

    @JsonProperty("fileType")
    private String fileType;

    @JsonProperty("fileUrl")
    private String fileUrl;

}