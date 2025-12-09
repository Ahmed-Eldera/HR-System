package com.orange.hr.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class NoSuchTeamException extends MyException {
    public NoSuchTeamException(HttpStatus statusCode, String message) {
        super(statusCode, message);
    }
}