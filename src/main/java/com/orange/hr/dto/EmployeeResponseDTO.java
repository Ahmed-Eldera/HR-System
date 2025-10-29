package com.orange.hr.dto;

import com.orange.hr.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EmployeeResponseDTO {
    public EmployeeResponseDTO() {
    }

    public EmployeeResponseDTO(int employeeID, String name, LocalDate dateOfBirth, Gender gender, LocalDate graduationDate, float salary, DepartmentDTO department, Integer managerId, TeamDTO team, List<ExpertiseRequestDTO> expertise) {
        this.employeeID = employeeID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.graduationDate = graduationDate;
        this.salary = salary;
        this.department = department;
        this.managerId = managerId;
        this.team = team;
        this.expertise = expertise;

    }

    private int employeeID;
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
    private LocalDate graduationDate;
    private float salary;
    private DepartmentDTO department;
    private Integer managerId;
    private TeamDTO team;
    private List<ExpertiseRequestDTO> expertise;
}
