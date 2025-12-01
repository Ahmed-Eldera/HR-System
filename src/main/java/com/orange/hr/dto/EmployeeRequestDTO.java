package com.orange.hr.dto;

import com.orange.hr.enums.Gender;
import com.orange.hr.validationGroups.Always;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequestDTO {

    @NotNull(message = "employeeId can't be null")
    private Integer employeeId;

    @NotBlank(message = "Name can't be null")
    private String name;

    @NotNull(message = "Date of birth can't be null")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender can't be null")
    private Gender gender;

    @NotNull(message = "Graduation Date can't be null")
    private LocalDate graduationDate;

    @Min(value = 500, groups = {Default.class, Always.class})
    @NotNull(message = "Salary can't be null")
    private Float salary;

    @NotNull(message = "Department can't be null")
    private Integer departmentId;

    private Optional<Integer> managerId;

    @NotNull(message = "Team can't be null")
    private Integer teamId;
    private List<Integer> expertise;
}