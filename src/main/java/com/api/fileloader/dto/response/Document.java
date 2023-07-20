package com.api.fileloader.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Document {
    private String fileName;
    private String hash;
    private LocalDateTime lastUpload;
}
