package com.orange.hr.dto;

import com.orange.hr.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class EmployeeRequestDTO {
    public EmployeeRequestDTO(int employeeId, String name, LocalDate dateOfBirth, Gender gender, LocalDate graduationDate, float salary, int departmentId, Integer managerId, int teamId, List<ExpertiseRequestDTO> expertise) {
        this.employeeId = employeeId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.graduationDate = graduationDate;
        this.salary = salary;
        this.departmentId = departmentId;
        this.managerId = managerId;
        this.teamId = teamId;
        this.expertise = expertise;
    }

    private int employeeId;
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
    private LocalDate graduationDate;
    private float salary;
    private int departmentId;
    private Integer managerId;
    private int teamId;
    private List<ExpertiseRequestDTO> expertise;
}
