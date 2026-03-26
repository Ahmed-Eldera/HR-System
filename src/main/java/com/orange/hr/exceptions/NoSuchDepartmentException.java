package com.orange.hr.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class NoSuchDepartmentException extends HrException {
    public NoSuchDepartmentException(HttpStatus statusCode, String message) {
        super(statusCode, message);
    }
}