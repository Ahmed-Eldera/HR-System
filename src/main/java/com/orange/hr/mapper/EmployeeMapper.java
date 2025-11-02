package com.orange.hr.mapper;

import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public Employee toEntity(EmployeeRequestDTO dto){
        return new Employee();
    }
    public EmployeeResponseDTO toDTO(Employee entity){
        return new EmployeeResponseDTO();
    }
}