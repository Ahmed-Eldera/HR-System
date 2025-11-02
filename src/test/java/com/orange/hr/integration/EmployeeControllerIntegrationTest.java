package com.orange.hr.integration;


import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.entity.Department;
import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Expertise;
import com.orange.hr.entity.Team;
import com.orange.hr.enums.Gender;
import com.orange.hr.repository.DepartmentRepository;
import com.orange.hr.repository.EmployeeRepository;
import com.orange.hr.repository.ExpertiseRepository;
import com.orange.hr.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmployeeControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ExpertiseRepository expertiseRepository;

    @Test
    public void AddEmpolyeeSuccessfully() throws Exception {

        //Arrange
        Department department = new Department(1, "Developers");
        departmentRepository.save(department);
        Team team = new Team(1, "Project-X");
        teamRepository.save(team);
        Employee emp = new Employee();
        emp.setEmployeeID(1);
        emp.setName("Ahmed");
        emp.setDateOfBirth(LocalDate.of(1999, 5, 12));
        emp.setGender(Gender.MALE);
        emp.setGraduationDate(LocalDate.of(2021, 7, 1));
        emp.setSalary(500F);
        teamRepository.save(team);
        emp.setTeam(team);
        employeeRepository.save(emp);
        Expertise expertise = new Expertise(null, "java", null);
        expertise = expertiseRepository.save(expertise);
        List<Integer> l = new ArrayList<>();
        l.add(expertise.getExpertiseId());
        //act
        EmployeeRequestDTO employee = new EmployeeRequestDTO(2, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000F, 1, 1, 1, l);

        //act
        ResultActions result = mockMvc.perform(post("/employees/{id}", employee));
        //assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(employee.getEmployeeId()))
                .andExpect(jsonPath("$.name").value(employee.getName()))
                .andExpect(jsonPath("$.dateOfBirth").value(employee.getDateOfBirth()))
                .andExpect(jsonPath("$.gender").value(employee.getGender()))
                .andExpect(jsonPath("$.graduationDate").value(employee.getGraduationDate()))
                .andExpect(jsonPath("$.salary").value(employee.getSalary()))
                .andExpect(jsonPath("$.departmentId").value(employee.getDepartmentId()))
                .andExpect(jsonPath("$.managerId").value(employee.getManagerId()))
                .andExpect(jsonPath("$.teamId").value(employee.getDepartmentId()))
                .andExpect(jsonPath("$.expertisesIds").value(employee.getExpertise()));
    }
}