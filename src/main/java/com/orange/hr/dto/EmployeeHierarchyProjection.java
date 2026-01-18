package com.orange.hr.dto;

import com.orange.hr.enums.Gender;

import java.time.LocalDate;

public interface EmployeeHierarchyProjection {
    Integer getEmployeeId();

    String getName();

    LocalDate getDateOfBirth();

    Gender getGender();

    LocalDate getGraduationDate();

    Double getSalary();

    Integer getDepartmentId();

    Integer getTeamId();

    Integer getManagerId();

    // Expertise columns
    Integer getExpertiseId();

    String getExpertiseName();
}
