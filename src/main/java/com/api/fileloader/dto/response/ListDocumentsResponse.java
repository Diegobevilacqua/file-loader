package com.api.fileloader.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListDocumentsResponse {
    private String fileName;
    private String hashSha256;
    private String hashSha512;
    private LocalDateTime lastUpload;
}
