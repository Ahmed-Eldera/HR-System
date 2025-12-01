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
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Transactional
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
    @Autowired
    private EntityManager entityManager;


    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO employee) {
        // validating the input data
        if (employee.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new InValidDateException(HttpStatus.BAD_REQUEST, "Birth date can't be in the future");
        }

        Department dept = departmentRepository.findById(employee.getDepartmentId()).orElseThrow(() -> new NoSuchDepartmentException(HttpStatus.NOT_FOUND, "Can't find the Selected Department"));
        Team team = teamRepository.findById(employee.getTeamId()).orElseThrow(() -> new NoSuchTeamException(HttpStatus.NOT_FOUND, "Can't find the Selected Team"));

        Employee manager = null;
        if (employee.getManagerId() != null) {
            if (employee.getManagerId().isPresent()) {
                manager = employeeRepository.findById(employee.getManagerId().get()).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Can't find the Selected Manager"));
            }
        }

        List<Expertise> expertises = expertiseRepository.findAllById(employee.getExpertise());
        if (expertises.size() != employee.getExpertise().size()) {
            throw new NoSuchExpertiseException(HttpStatus.NOT_FOUND, "Can't find the Selected Expertise");
        }

        //saving the employee
        Employee entity = employeeMapper.toEntity(employee);
        entity.setDepartment(dept);
        entity.setTeam(team);
        entity.setManager(manager);
        entity.setExpertises(expertises);
        employeeRepository.save(entity);
        return employeeMapper.toDTO(entity);
    }

    public EmployeeResponseDTO modifyEmployee(Integer id, EmployeeRequestDTO dto) {

        Employee entity = employeeRepository.findById(id).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Employee Not Found"));
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getGender() != null) {
            entity.setGender(dto.getGender());
        }

        if (dto.getSalary() != null) {
            if(dto.getSalary()>=500) {
                entity.setSalary(dto.getSalary());
            }else{
                throw new MyException(HttpStatus.BAD_REQUEST,"Minimum salary is 500");
            }
        }

        if (dto.getExpertise() != null) {
            List<Expertise> expertises = expertiseRepository.findAllById(dto.getExpertise());
            if (expertises.size() != dto.getExpertise().size()) {
                throw new NoSuchExpertiseException(HttpStatus.NOT_FOUND, "Can't find the Selected Expertise");
            }
            entity.setExpertises(expertises);
        }

        if (dto.getGraduationDate() != null) {
            entity.setGraduationDate(dto.getGraduationDate());
        }

        if (dto.getDateOfBirth() != null) {
            entity.setDateOfBirth(dto.getDateOfBirth());
        }

        if (dto.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(dto.getDepartmentId()).orElseThrow(() -> new NoSuchDepartmentException(HttpStatus.NOT_FOUND, "Can't find the Selected Department"));
            entity.setDepartment(dept);
        }

        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId()).orElseThrow(() -> new NoSuchTeamException(HttpStatus.NOT_FOUND, "Can't find the Selected Team"));
            entity.setTeam(team);
        }

        if (dto.getManagerId() != null) {
            if (dto.getManagerId().isPresent()) {
                entity.setManager(employeeRepository.findById(dto.getManagerId().get()).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Can't find the Selected Manager")));
            } else {
                entity.setManager(null);
            }
        }

        employeeRepository.save(entity);
        return employeeMapper.toDTO(entity);
    }

    @Override
    public void deleteEmployeeAndReassignSubordinates(Integer id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Can't find Such Employee"));
        if (employee.getManager() == null) {
            throw new MyException(HttpStatus.CONFLICT, "Can't delete a super manager");
        }
        Integer newManagerId = employee.getManager().getEmployeeID();
        //reassign his subordinates to his manager before deleting him
        employeeRepository.reassignSubordinates(id,newManagerId);
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeResponseDTO getEmployee(Integer id) {
        Employee entity = employeeRepository.findById(id).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "Employee Not Found"));
        EmployeeResponseDTO dto = employeeMapper.toDTO(entity);
        return dto;
    }
}