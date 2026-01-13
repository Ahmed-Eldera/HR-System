package com.orange.hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class BonusResponseDTO {
    Integer id;
    Integer employeeId;
    Double amount;
    LocalDate createdAt;
}
