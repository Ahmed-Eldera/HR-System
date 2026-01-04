package com.orange.hr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class LeaveResponseDTO {
    Integer id;
    Integer employeeId;
    LocalDate date;
    LocalDate createdAt;
}
