package com.orange.hr.service.impl;

import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.entity.Department;
import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Expertise;
import com.orange.hr.entity.Team;
import com.orange.hr.exceptions.*;
import com.orange.hr.mapper.EmployeeMapper;
import com.orange.hr.repository.DepartmentRepository;
import com.orange.hr.repository.EmployeeRepository;
import com.orange.hr.repository.ExpertiseRepository;
import com.orange.hr.repository.TeamRepository;
import com.orange.hr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ExpertiseRepository expertiseRepository;
    @Autowired
    private EmployeeMapper employeeMapper;

    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO employee) {
        if (employee.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new InValidDateException(HttpStatus.BAD_REQUEST, "Birth date can't be in the future");
        }
        Department dept = departmentRepository.findById(employee.getDepartmentId()).orElseThrow(() -> new NoSuchDepartmentException(HttpStatus.NOT_FOUND, "Can't find the Selected Department"));
        Team team = teamRepository.findById(employee.getTeamId()).orElseThrow(() -> new NoSuchTeamException(HttpStatus.NOT_FOUND, "Can't find the Selected Team"));
        Employee manager = null;
        if (employee.getManagerId() != null) {
            manager = employeeRepository.findById(employee.getManagerId()).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Can't find the Selected Manager"));
        }
        List<Expertise> expertises = new ArrayList<>();
        if (employee.getExpertise() != null) {
            for (Integer i : employee.getExpertise()) {
                expertises.add(expertiseRepository.findById(i).orElseThrow(()->new NoSuchExpertiseException(HttpStatus.NOT_FOUND, "Can't find the Selected Expertise")));
            }
        }
        Employee entity = employeeMapper.toEntity(employee);
        entity.setDepartment(dept);
        entity.setTeam(team);
        entity.setManager(manager);
        entity.setExpertises(expertises);
        employeeRepository.save(entity);
        return employeeMapper.toDTO(entity);
    }
}