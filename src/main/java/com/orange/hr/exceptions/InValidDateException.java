package com.orange.hr.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
public class InValidDateException extends MyException{
      public InValidDateException(HttpStatus statusCode, String message){
              super(statusCode,message);
      }
}