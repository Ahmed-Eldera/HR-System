package com.orange.hr.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.orange.hr.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
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
    JsonNode
}