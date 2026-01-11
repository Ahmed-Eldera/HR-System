package com.orange.hr.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class LeaveResponseDTO {
    Integer id;
    Integer employeeId;
    LocalDate date;
    LocalDateTime createdAt;
}
