package com.orange.hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequestDTO {
    private String email;
    private String password;
}
