package com.orange.hr.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchExpertiseException extends HrException {
    public NoSuchExpertiseException(HttpStatus statusCode, String message) {
        super(statusCode, message);
    }
}