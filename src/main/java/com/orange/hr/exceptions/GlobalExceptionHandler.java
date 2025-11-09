package com.orange.hr.exceptions;

import com.orange.hr.dto.CustomError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception) {

        CustomError error = new CustomError();

        error.setMsg( exception.getBindingResult().getAllErrors().getFirst().getDefaultMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler({ MyException.class })
    public ResponseEntity<Object> handleRuntimeException(MyException exception) {

        CustomError error = new CustomError();

        error.setMsg(exception.getMessage());
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(error);
    }
}
