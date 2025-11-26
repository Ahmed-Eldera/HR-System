package com.orange.hr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeNodeDTO {
    private Integer id;
    private String name;
    private List<EmployeeNodeDTO> subordinates;
}
