package com.orange.hr.exceptions;

import com.orange.hr.dto.CustomError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception) {

        CustomError error = new CustomError();
        StringBuffer msg = new StringBuffer();
        List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        for(ObjectError e: errors){
            msg.append(e.getDefaultMessage() + " ,");
        }

        error.setMsg(msg.toString());
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
