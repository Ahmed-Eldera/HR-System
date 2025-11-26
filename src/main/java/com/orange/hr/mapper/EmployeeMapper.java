package com.orange.hr.mapper;

import com.orange.hr.dto.EmployeeNodeDTO;
import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Expertise;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {
    public Employee toEntity(EmployeeRequestDTO dto) {
        Employee employee = new Employee();
//        employee.setEmployeeID(dto.getEmployeeId());
        employee.setName(dto.getName());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setGraduationDate(dto.getGraduationDate());
        employee.setSalary(dto.getSalary());
        employee.setGender(dto.getGender());

        return employee;
    }

    public EmployeeResponseDTO toDTO(Employee entity) {
        EmployeeResponseDTO response = new EmployeeResponseDTO();
        response.setEmployeeID(entity.getEmployeeID());
        response.setName(entity.getName());
        response.setGender(entity.getGender());
        response.setSalary(entity.getSalary());
        response.setGraduationDate(entity.getGraduationDate());
        response.setDateOfBirth(entity.getDateOfBirth());
        if(entity.getManager()!=null) {
            response.setManagerId(entity.getManager().getEmployeeID());
        }
        response.setDepartmentId(entity.getDepartment().getDepartment_Id());
        response.setExpertisesIds(entity.getExpertises()
                .stream()
                .map(Expertise::getExpertiseId)
                .collect(Collectors.toList()));
        response.setTeamId(entity.getTeam().getTeamId());
        return response;
    }
    public EmployeeNodeDTO toEmployeeNodeDTO(Employee entity){
        EmployeeNodeDTO employeeNodeDTO = new EmployeeNodeDTO();
        employeeNodeDTO.setId(entity.getEmployeeID());
        employeeNodeDTO.setName(entity.getName());
        if(!entity.getSubordinates().isEmpty()) {
            employeeNodeDTO.setSubordinates(
                    toEmployeeNodeDTOList(entity.getSubordinates())
            );
        }
        return employeeNodeDTO;
    }
    public List<EmployeeNodeDTO> toEmployeeNodeDTOList(List<Employee> entityList){
        List<EmployeeNodeDTO> employeeNodeDTOList = new ArrayList<>();
        for(Employee entity:entityList){
            employeeNodeDTOList.add(toEmployeeNodeDTO(entity));
        }
        return employeeNodeDTOList;
    }
}