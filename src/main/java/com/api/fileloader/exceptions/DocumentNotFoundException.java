package com.api.fileloader.exceptions;

import org.springframework.http.HttpStatus;

public class DocumentNotFoundException extends DocumentException {
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    private String message = "There is not a document with that hash.";

    public DocumentNotFoundException() {
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
