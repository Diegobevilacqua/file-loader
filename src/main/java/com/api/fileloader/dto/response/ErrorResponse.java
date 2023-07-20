package com.api.fileloader.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
public class ErrorResponse {
    private Instant timestamp;
    private HttpStatus httpStatus;
    private String message;
    private String path;
}
