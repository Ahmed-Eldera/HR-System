package com.orange.hr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class BonusResponseDTO {
    Integer id;
    Integer employeeId;
    Double amount;
    LocalDate createdAt;
}
