package com.orange.hr.dto;

import com.orange.hr.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDTO {
    private Integer employeeID;
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
    private LocalDate graduationDate;
    private Float salary;
    private Integer departmentId;
    private Integer managerId;
    private Integer teamId;
    private List<Integer> expertisesIds;
}