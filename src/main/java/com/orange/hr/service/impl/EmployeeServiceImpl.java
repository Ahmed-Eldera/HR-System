package com.orange.hr.service.Impl;

import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.entity.Department;
import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Team;
import com.orange.hr.mapper.EmployeeMapper;
import com.orange.hr.repository.DepartmentRepository;
import com.orange.hr.repository.EmployeeRepository;
import com.orange.hr.repository.TeamRepository;
import com.orange.hr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeMapper employeeMapper;

    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO employee) {

        Department dept = departmentRepository.findById(employee.getDepartmentId()).orElseThrow(()->new RuntimeException());
        Team team = teamRepository.findById(employee.getTeamId()).orElseThrow(()-> new RuntimeException());
        Optional<Employee> manager = employeeRepository.findById(employee.getManagerId());
        Employee entity = employeeMapper.toEntity(employee);
        entity.setDepartment(dept);
        entity.setTeam(team);
        if(manager.isPresent()){
            entity.setManager(manager.get());
        }else if(employee.getManagerId()!=null){
            throw new RuntimeException();
        }
        if(employee.getDateOfBirth().isAfter(LocalDate.now())){
            throw new RuntimeException();
        }
        employeeRepository.save(entity);
        EmployeeResponseDTO responseDTO = employeeMapper.toDTO(entity);
        return responseDTO;
    }
}