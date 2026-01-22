package com.orange.hr.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RaiseRequestDTO {
    @Positive
    private Double ratio;
}
