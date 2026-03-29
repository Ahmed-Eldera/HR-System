package com.orange.hr.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class InValidDateException extends HrException {
    public InValidDateException(HttpStatus statusCode, String message) {
        super(statusCode, message);
    }
}