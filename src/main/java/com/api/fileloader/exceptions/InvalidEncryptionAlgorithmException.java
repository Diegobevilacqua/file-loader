package com.api.fileloader.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidEncryptionAlgorithmException extends DocumentException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String message = "The selected encryption algorithm is not valid.";

    public InvalidEncryptionAlgorithmException() {
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
