package com.orange.hr.exceptions;

import org.springframework.http.HttpStatus;

public class PayrollJobAlreadyExistsException extends HrException {
    public PayrollJobAlreadyExistsException(HttpStatus statusCode, String message) {
        super(statusCode, message);
    }
}
