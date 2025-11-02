package com.orange.hr.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NoSuchExpertiseException extends RuntimeException{
        private String message;
}