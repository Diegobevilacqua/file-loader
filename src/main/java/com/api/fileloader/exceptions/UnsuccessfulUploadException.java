package com.api.fileloader.exceptions;

import org.springframework.http.HttpStatus;

public class UnsuccessfulUploadException extends DocumentException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String message = "An error occurred uploading the file.";

    public UnsuccessfulUploadException() {
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
