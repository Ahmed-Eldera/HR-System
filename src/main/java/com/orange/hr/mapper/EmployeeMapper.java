package com.orange.hr.mapper;

import com.orange.hr.dto.EmployeeHierarchyProjection;
import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Expertise;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {
    public Employee toEntity(EmployeeRequestDTO dto) {
        Employee employee = new Employee();
//        employee.setEmployeeID(dto.getEmployeeId());
        employee.setName(dto.getName());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setGraduationDate(dto.getGraduationDate());
        employee.setGender(dto.getGender());
        return employee;
    }

    public EmployeeResponseDTO toDTO(Employee entity) {
        EmployeeResponseDTO response = new EmployeeResponseDTO();
        response.setEmployeeID(entity.getEmployeeID());
        response.setName(entity.getName());
        response.setGender(entity.getGender());
        response.setSalary(entity.getSalaryHistory()
                .stream()
                .sorted((a, b) -> a.getCreatedAt().isBefore(b.getCreatedAt()) ? 1 : -1)
                .findFirst()
                .get()
                .getGross());

        response.setGraduationDate(entity.getGraduationDate());
        response.setDateOfBirth(entity.getDateOfBirth());
        if (entity.getManager() != null) {
            response.setManagerId(entity.getManager().getEmployeeID());
        }
        response.setDepartmentId(entity.getDepartment().getDepartmentId());
        response.setExpertisesIds(entity.getExpertises()
                .stream()
                .map(Expertise::getExpertiseId)
                .collect(Collectors.toList()));
        response.setTeamId(entity.getTeam().getTeamId());
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