package com.orange.hr.mapper;

import com.orange.hr.dto.EmployeeHierarchyProjection;
import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.entity.Department;
import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Expertise;
import com.orange.hr.entity.Team;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {
    public Employee toEntity(EmployeeRequestDTO dto, Department department, Team team, Employee manager, List<Expertise> expertises) {
        Employee employee = new Employee();
//        employee.setEmployeeID(dto.getEmployeeId());
        employee.setName(dto.getName());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setGraduationDate(dto.getGraduationDate());
        employee.setGender(dto.getGender());
        employee.setDepartment(department);
        employee.setTeam(team);
        employee.setManager(manager);
        employee.setExpertises(expertises);
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
        response.setDepartmentId(entity.getDepartment().getDepartmentId());
        response.setTeamId(entity.getTeam().getTeamId());
        if (entity.getManager() != null) {
            response.setManagerId(entity.getManager().getEmployeeID());
        }
        response.setExpertisesIds(entity.getExpertises()
                .stream()
                .map(Expertise::getExpertiseId)
                .collect(Collectors.toList()));
        return response;
    }

    public List<EmployeeResponseDTO> projectionToDTO(List<EmployeeHierarchyProjection> rows) {
        Map<Integer, EmployeeResponseDTO> map = new LinkedHashMap<>();

        for (EmployeeHierarchyProjection row : rows) {
            map.putIfAbsent(row.getEmployeeId(),
                    new EmployeeResponseDTO(
                            row.getEmployeeId(),
                            row.getName(),
                            row.getDateOfBirth(),
                            row.getGender(),
                            row.getGraduationDate(),
                            row.getSalary(),
                            row.getDepartmentId(),
                            row.getManagerId(),
                            row.getTeamId(),
                            new ArrayList<>()
                    )
            );

            if (row.getExpertiseId() != null) {
                map.get(row.getEmployeeId()).getExpertisesIds()
                        .add(row.getExpertiseId());
            }
        }

        return new ArrayList<>(map.values());
    }

}