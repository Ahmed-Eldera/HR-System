package com.orange.hr.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NoSuchEmployeeException extends RuntimeException{
        private String message;
}