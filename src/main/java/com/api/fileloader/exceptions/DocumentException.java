package com.api.fileloader.exceptions;

import org.springframework.http.HttpStatus;

public class DocumentException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
