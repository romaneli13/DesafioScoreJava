package com.serasa.desafio.models.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public class CustomException extends Exception {

    private HttpStatus httpStatus;
    private String message;

    public CustomException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public CustomException(String message, HttpStatus httpStatus, String message1) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message1;
    }

    public CustomException(String message, Throwable cause, HttpStatus httpStatus, String message1) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.message = message1;
    }

    public CustomException(Throwable cause, HttpStatus httpStatus, String message) {
        super(cause);
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus httpStatus, String message1) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpStatus = httpStatus;
        this.message = message1;
    }
}
