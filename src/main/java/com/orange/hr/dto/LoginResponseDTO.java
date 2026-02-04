package com.orange.hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
//@NoArgsConstructor
public class LoginResponseDTO {
    private String token;

    private long expiresIn;

}
