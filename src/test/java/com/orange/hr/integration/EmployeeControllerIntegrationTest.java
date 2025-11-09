package com.orange.hr.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
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
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.sql.Types;
import java.time.*;
import java.util.ArrayList;
import java.util.List;


public class EmployeeControllerIntegrationTest extends AbstractTest {
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
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void AddEmpolyeeSuccessfully_WithFullData_ExpectCreated() throws Exception {
        prepareDB("/datasets/populateDB.xml");
        //Arrange

        List<Integer> l = new ArrayList<>();
        l.add(1);
        //act
        EmployeeRequestDTO employee = new EmployeeRequestDTO(2, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000F, 1, 1, 1, l);

        //act
        ResultActions result = mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeID").value(employee.getEmployeeId()))
                .andExpect(jsonPath("$.name").value(employee.getName()))
                .andExpect(jsonPath("$.dateOfBirth").value(employee.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.gender").value(employee.getGender().toString()))
                .andExpect(jsonPath("$.graduationDate").value(employee.getGraduationDate().toString()))
                .andExpect(jsonPath("$.salary").value(employee.getSalary()))
                .andExpect(jsonPath("$.departmentId").value(employee.getDepartmentId()))
                .andExpect(jsonPath("$.managerId").value(employee.getManagerId()))
                .andExpect(jsonPath("$.teamId").value(employee.getTeamId()))
                .andExpect(jsonPath("$.expertisesIds").value(employee.getExpertise()));
    }

    @Test
    public void AddEmpolyeeSuccessfully_WithNoManager_ExpectCreated() throws Exception {
        prepareDB("/datasets/populateDB.xml");
        //Arrange

        List<Integer> l = new ArrayList<>();
        l.add(1);
        //act
        EmployeeRequestDTO employee = new EmployeeRequestDTO(2, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000F, 1, null, 1, l);

        //act
        ResultActions result = mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeID").value(employee.getEmployeeId()))
                .andExpect(jsonPath("$.name").value(employee.getName()))
                .andExpect(jsonPath("$.dateOfBirth").value(employee.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.gender").value(employee.getGender().toString()))
                .andExpect(jsonPath("$.graduationDate").value(employee.getGraduationDate().toString()))
                .andExpect(jsonPath("$.salary").value(employee.getSalary()))
                .andExpect(jsonPath("$.departmentId").value(employee.getDepartmentId()))
                .andExpect(jsonPath("$.managerId").value(employee.getManagerId()))
                .andExpect(jsonPath("$.teamId").value(employee.getTeamId()))
                .andExpect(jsonPath("$.expertisesIds").value(employee.getExpertise()));
    }

    @Test
    public void AddEmpolyee_WithMissingData_ExpectBadRequest() throws Exception {
        prepareDB("/datasets/populateDB.xml");
        //Arrange

        List<Integer> l = new ArrayList<>();
        l.add(1);
        //act
        EmployeeRequestDTO employee = new EmployeeRequestDTO(2, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000F, null, null, 1, l);

        //act
        ResultActions result = mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void AddEmpolyee_WithDepartmentNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/populateDB.xml");
        //Arrange

        List<Integer> l = new ArrayList<>();
        l.add(1);
        //act
        EmployeeRequestDTO employee = new EmployeeRequestDTO(2, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000F, 2, null, 1, l);

        //act
        ResultActions result = mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("Can't find the Selected Department"));
    }

    @Test
    public void AddEmpolyee_WithTeamNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/populateDB.xml");
        //Arrange

        List<Integer> l = new ArrayList<>();
        l.add(1);
        //act
        EmployeeRequestDTO employee = new EmployeeRequestDTO(2, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000F, 1, null, 2, l);

        //act
        ResultActions result = mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("Can't find the Selected Team"));
    }

    @Test
    public void AddEmpolyee_WithManagerNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/populateDB.xml");
        //Arrange

        List<Integer> l = new ArrayList<>();
        l.add(1);
        //act
        EmployeeRequestDTO employee = new EmployeeRequestDTO(2, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000F, 1, 55, 1, l);

        //act
        ResultActions result = mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("Can't find the Selected Manager"));
    }

    @Test
    public void AddEmpolyee_WithBirthDateNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/populateDB.xml");
        //Arrange

        List<Integer> l = new ArrayList<>();
        l.add(1);
        //act
        EmployeeRequestDTO employee = new EmployeeRequestDTO(2, "ahmed ELdera", LocalDate.of(2993, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000F, 1, 1, 1, l);

        //act
        ResultActions result = mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("Birth date can't be in the future"));
    }


}