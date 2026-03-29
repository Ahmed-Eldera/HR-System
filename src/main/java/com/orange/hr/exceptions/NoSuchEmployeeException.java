package com.orange.hr.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchEmployeeException extends HrException {
    public NoSuchEmployeeException(HttpStatus statusCode, String message) {
        super(statusCode, message);
    }
}