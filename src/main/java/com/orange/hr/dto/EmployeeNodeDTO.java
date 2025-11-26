package com.orange.hr.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeNodeDTO {
    private Integer id;
    private String name;
    private List<EmployeeNodeDTO> subordinates;
}
