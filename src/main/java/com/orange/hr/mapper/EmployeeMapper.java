package com.orange.hr.mapper;

import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.entity.Department;
import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Team;

public class EmployeeMapper {
    public Employee toEntity(EmployeeRequestDTO dto){
//        Employee entity = new Employee(dto.getEmployeeId(),dto.getName(),dto.getDateOfBirth(),dto.getGender(),dto.getGraduationDate(),dto.getSalary(),dept,manager,team);
        return new Employee();
    }
    public EmployeeResponseDTO toDTO(Employee entity){
        return new EmployeeResponseDTO();
    }
}
