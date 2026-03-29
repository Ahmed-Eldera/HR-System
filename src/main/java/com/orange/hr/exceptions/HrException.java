package com.orange.hr.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@AllArgsConstructor
public class HrException extends RuntimeException {
    private HttpStatus statusCode;
    private String message;
}
