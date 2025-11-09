package com.orange.hr.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchExpertiseException extends MyException{
        public NoSuchExpertiseException(HttpStatus statusCode, String message){
                super(statusCode,message);
        }
}