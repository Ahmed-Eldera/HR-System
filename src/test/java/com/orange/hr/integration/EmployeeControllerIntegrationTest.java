package com.orange.hr.integration;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.entity.Employee;
import com.orange.hr.enums.Gender;
import com.orange.hr.repository.DepartmentRepository;
import com.orange.hr.repository.EmployeeRepository;
import com.orange.hr.repository.ExpertiseRepository;
import com.orange.hr.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeControllerIntegrationTest extends AbstractTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final int EXISTING_EMPLOYEE_ID = 1;
    private static final int NEW_EMPLOYEE_ID = 2;
    private static final String EXISTING_EMPLOYEE_NAME = "Ahmed";
    private static final String NEW_EMPLOYEE_NAME = "Ahmed Eldera";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(2003, 2, 18);
    private static final LocalDate NEW_DATE_OF_BIRTH = LocalDate.of(2004, 2, 18);
    private static final LocalDate FUTURE_DATE_OF_BIRTH = LocalDate.of(2999, 2, 18);
    private static final LocalDate GRADUATION_DATE = LocalDate.of(2026, 2, 18);
    private static final LocalDate NEW_GRADUATION_DATE = LocalDate.of(2029, 2, 18);
    private static final float SALARY = 500F;
    private static final float NEW_SALARY = 99F;
    private static final int DEPARTMENT_ID = 1;
    private static final int DEPARTMENT_ID2 = 2;
    private static final int NON_EXISTENT_DEPARTMENT_ID = 9876;
    private static final int TEAM_ID = 1;
    private static final int TEAM_ID2 = 2;
    private static final int NON_EXISTENT_TEAM_ID = 9876;
    private static final Optional<Integer> MANAGER_ID = Optional.of(1);
    private static final Optional<Integer> MANAGER_ID2 = Optional.of(2);
    private static final Optional<Integer> NON_EXISTENT_MANAGER_ID = Optional.of(99);
    private static final int EXPERTISE_ID = 1;
    private static final int EXPERTISE_ID2 = 1;
    private static final int NON_EXISTENT_EXPERTISE_ID = 123;


    @Test
    public void AddEmpolyeeSuccessfully_WithFullData_ExpectCreated() throws Exception {
        prepareDB("/datasets/AddEmployeeDataset.xml");
        //Arrange
        List<Integer> expertises = new ArrayList<>();
        expertises.add(EXPERTISE_ID);
        EmployeeRequestDTO employee = new EmployeeRequestDTO(
                NEW_EMPLOYEE_ID,
                NEW_EMPLOYEE_NAME,
                DATE_OF_BIRTH,
                Gender.MALE,
                GRADUATION_DATE,
                SALARY,
                DEPARTMENT_ID,
                MANAGER_ID,
                TEAM_ID,
                expertises
        );

        //act
        ResultActions result = mockMvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
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
                .andExpect(jsonPath("$.managerId").value(employee.getManagerId().get()))
                .andExpect(jsonPath("$.teamId").value(employee.getTeamId()))
                .andExpect(jsonPath("$.expertisesIds").value(employee.getExpertise()));
    }

    @Test
    public void AddEmpolyeeSuccessfully_WithNoManager_ExpectCreated() throws Exception {
        prepareDB("/datasets/AddEmployeeDataset.xml");
        //Arrange
        List<Integer> expertises = new ArrayList<>();
        expertises.add(EXPERTISE_ID);
        EmployeeRequestDTO employee = new EmployeeRequestDTO(
                NEW_EMPLOYEE_ID,
                NEW_EMPLOYEE_NAME,
                DATE_OF_BIRTH,
                Gender.MALE,
                GRADUATION_DATE,
                SALARY,
                DEPARTMENT_ID,
                Optional.empty(),
                TEAM_ID,
                expertises
        );

        //act
        ResultActions result = mockMvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeID").isNumber())
                .andExpect(jsonPath("$.name").value(employee.getName()))
                .andExpect(jsonPath("$.dateOfBirth").value(employee.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.gender").value(employee.getGender().toString()))
                .andExpect(jsonPath("$.graduationDate").value(employee.getGraduationDate().toString()))
                .andExpect(jsonPath("$.salary").value(employee.getSalary()))
                .andExpect(jsonPath("$.departmentId").value(employee.getDepartmentId()))
                .andExpect(jsonPath("$.managerId").isEmpty())
                .andExpect(jsonPath("$.teamId").value(employee.getTeamId()))
                .andExpect(jsonPath("$.expertisesIds").value(employee.getExpertise()));
    }

    @Test
    public void AddEmpolyee_WithMissingData_ExpectBadRequest() throws Exception {
        prepareDB("/datasets/AddEmployeeDataset.xml");
        //Arrange
        List<Integer> expertises = new ArrayList<>();
        expertises.add(EXPERTISE_ID);
        EmployeeRequestDTO employee = new EmployeeRequestDTO(
                NEW_EMPLOYEE_ID,
                null,  //missing name
                DATE_OF_BIRTH,
                Gender.MALE,
                GRADUATION_DATE,
                SALARY,
                DEPARTMENT_ID,
                null,
                TEAM_ID,
                expertises
        );
        //act
        ResultActions result = mockMvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void AddEmpolyee_WithDepartmentNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/AddEmployeeDataset.xml");
        //Arrange
        List<Integer> expertises = new ArrayList<>();
        expertises.add(EXPERTISE_ID);
        EmployeeRequestDTO employee = new EmployeeRequestDTO(
                NEW_EMPLOYEE_ID,
                NEW_EMPLOYEE_NAME,
                DATE_OF_BIRTH,
                Gender.MALE,
                GRADUATION_DATE,
                SALARY,
                NON_EXISTENT_DEPARTMENT_ID,
                MANAGER_ID,
                TEAM_ID,
                expertises
        );
        //act
        ResultActions result = mockMvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("Can't find the Selected Department"));
    }

    @Test
    public void AddEmpolyee_WithTeamNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/AddEmployeeDataset.xml");
        //Arrange
        List<Integer> expertises = new ArrayList<>();
        expertises.add(EXPERTISE_ID);
        EmployeeRequestDTO employee = new EmployeeRequestDTO(
                NEW_EMPLOYEE_ID,
                NEW_EMPLOYEE_NAME,
                DATE_OF_BIRTH,
                Gender.MALE,
                GRADUATION_DATE,
                SALARY,
                DEPARTMENT_ID,
                MANAGER_ID,
                NON_EXISTENT_TEAM_ID,
                expertises
        );
        //act
        ResultActions result = mockMvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("Can't find the Selected Team"));
    }

    @Test
    public void AddEmpolyee_WithManagerNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/AddEmployeeDataset.xml");
        //Arrange
        List<Integer> expertises = new ArrayList<>();
        expertises.add(EXPERTISE_ID);
        EmployeeRequestDTO employee = new EmployeeRequestDTO(
                NEW_EMPLOYEE_ID,
                NEW_EMPLOYEE_NAME,
                DATE_OF_BIRTH,
                Gender.MALE,
                GRADUATION_DATE,
                SALARY,
                DEPARTMENT_ID,
                NON_EXISTENT_MANAGER_ID,
                TEAM_ID,
                expertises
        );
        //act
        ResultActions result = mockMvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("Can't find the Selected Manager"));
    }

    @Test
    public void AddEmpolyee_WithBirthDateNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/AddEmployeeDataset.xml");
        //Arrange
        List<Integer> expertises = new ArrayList<>();
        expertises.add(EXPERTISE_ID);
        EmployeeRequestDTO employee = new EmployeeRequestDTO(
                NEW_EMPLOYEE_ID,
                NEW_EMPLOYEE_NAME,
                FUTURE_DATE_OF_BIRTH,
                Gender.MALE,
                GRADUATION_DATE,
                SALARY,
                DEPARTMENT_ID,
                MANAGER_ID,
                TEAM_ID,
                expertises
        );
        //act
        ResultActions result = mockMvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("Birth date can't be in the future"));
    }

    @Test
    public void AddEmpolyee_WithExpertiseNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/AddEmployeeDataset.xml");
        //Arrange
        List<Integer> expertises = new ArrayList<>();
        expertises.add(NON_EXISTENT_EXPERTISE_ID);
        EmployeeRequestDTO employee = new EmployeeRequestDTO(
                NEW_EMPLOYEE_ID,
                NEW_EMPLOYEE_NAME,
                DATE_OF_BIRTH,
                Gender.MALE,
                GRADUATION_DATE,
                SALARY,
                DEPARTMENT_ID,
                MANAGER_ID,
                TEAM_ID,
                expertises
        );
        //act
        ResultActions result = mockMvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("Can't find the Selected Expertise"));
    }

    @Test
    public void modifyEmployee_WithValidData_ExpectOK() throws Exception {
        prepareDB("/datasets/ModifyEmployeeDataset.xml");
        //arrange
        List<Integer> expertises = new ArrayList<>();
        expertises.add(EXPERTISE_ID2);
        EmployeeRequestDTO employee = new EmployeeRequestDTO(
                null,
                NEW_EMPLOYEE_NAME,
                NEW_DATE_OF_BIRTH,
                Gender.FEMALE,
                NEW_GRADUATION_DATE,
                NEW_SALARY,
                DEPARTMENT_ID2,
                MANAGER_ID2,
                TEAM_ID2,
                expertises
        );
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //act
        ResultActions result = mockMvc.perform(patch("/employee/" + EXISTING_EMPLOYEE_ID).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.employeeID").value(EXISTING_EMPLOYEE_ID))
                .andExpect(jsonPath("$.name").value(employee.getName()))
                .andExpect(jsonPath("$.dateOfBirth").value(employee.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.gender").value(employee.getGender().toString()))
                .andExpect(jsonPath("$.graduationDate").value(employee.getGraduationDate().toString()))
                .andExpect(jsonPath("$.salary").value(employee.getSalary()))
                .andExpect(jsonPath("$.departmentId").value(employee.getDepartmentId()))
                .andExpect(jsonPath("$.managerId").value(employee.getManagerId().get()))
                .andExpect(jsonPath("$.teamId").value(employee.getTeamId()))
                .andExpect(jsonPath("$.expertisesIds").value(employee.getExpertise()));

    }

    @Test
    public void modifyEmployee_RemoveManager_ExpectOK() throws Exception {
        prepareDB("/datasets/ModifyEmployeeDataset.xml");
        //arrange
        List<Integer> expertises = new ArrayList<>();
        expertises.add(EXPERTISE_ID);
        EmployeeRequestDTO employee = new EmployeeRequestDTO();
        employee.setManagerId(Optional.empty());
        //act
        ResultActions result = mockMvc.perform(patch("/employee/" + EXISTING_EMPLOYEE_ID).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.employeeID").value(EXISTING_EMPLOYEE_ID))
                .andExpect(jsonPath("$.name").value(EXISTING_EMPLOYEE_NAME)) //assert the change happened
                .andExpect(jsonPath("$.dateOfBirth").value(DATE_OF_BIRTH.toString()))
                .andExpect(jsonPath("$.gender").value(Gender.MALE.toString()))
                .andExpect(jsonPath("$.graduationDate").value(GRADUATION_DATE.toString()))
                .andExpect(jsonPath("$.salary").value(SALARY))
                .andExpect(jsonPath("$.departmentId").value(DEPARTMENT_ID))
                .andExpect(jsonPath("$.managerId").isEmpty())
                .andExpect(jsonPath("$.teamId").value(TEAM_ID))
                .andExpect(jsonPath("$.expertisesIds").value(expertises));

    }

    @Test
    public void modifyEmployee_PartialUpdateLeaveManagerAsItIs_ExpectOK() throws Exception {
        prepareDB("/datasets/ModifyEmployeeDataset.xml");
        //arrange
        objectMapper = new ObjectMapper();
        List<Integer> expertises = new ArrayList<>();
        EmployeeRequestDTO employee = new EmployeeRequestDTO();
        employee.setName(NEW_EMPLOYEE_NAME);
        expertises.add(EXPERTISE_ID);
        //act
        ResultActions result = mockMvc.perform(patch("/employee/" + EXISTING_EMPLOYEE_ID).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .writeValueAsString(employee)));
        //assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(employee.getName())) //assert the change happened
                .andExpect(jsonPath("$.dateOfBirth").value(DATE_OF_BIRTH.toString()))
                .andExpect(jsonPath("$.gender").value(Gender.MALE.toString()))
                .andExpect(jsonPath("$.graduationDate").value(GRADUATION_DATE.toString()))
                .andExpect(jsonPath("$.salary").value(SALARY))
                .andExpect(jsonPath("$.departmentId").value(DEPARTMENT_ID))
                .andExpect(jsonPath("$.managerId").value(MANAGER_ID2.get()))
                .andExpect(jsonPath("$.teamId").value(TEAM_ID))
                .andExpect(jsonPath("$.expertisesIds").value(expertises));
    }

    @Test
    public void modifyEmployee_RemoveExpertises_ExpectOK() throws Exception {
        prepareDB("/datasets/ModifyEmployeeDataset.xml");
        //arrange
        objectMapper = new ObjectMapper();
        List<Integer> expertises = new ArrayList<>();
        EmployeeRequestDTO employee = new EmployeeRequestDTO();
        employee.setName(NEW_EMPLOYEE_NAME);
        employee.setExpertise(expertises);
        //act
        ResultActions result = mockMvc.perform(patch("/employee/" + EXISTING_EMPLOYEE_ID).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(employee)));
        //assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(employee.getName())) //assert the change happened
                .andExpect(jsonPath("$.dateOfBirth").value(DATE_OF_BIRTH.toString()))
                .andExpect(jsonPath("$.gender").value(Gender.MALE.toString()))
                .andExpect(jsonPath("$.graduationDate").value(GRADUATION_DATE.toString()))
                .andExpect(jsonPath("$.salary").value(SALARY))
                .andExpect(jsonPath("$.departmentId").value(DEPARTMENT_ID))
                .andExpect(jsonPath("$.managerId").value(MANAGER_ID2.get()))
                .andExpect(jsonPath("$.teamId").value(TEAM_ID))
                .andExpect(jsonPath("$.expertisesIds").value(expertises));
    }

    @Test
    public void deleteEmployee() throws Exception {
        prepareDB("/datasets/DeleteEmployee.xml");
        //act
        ResultActions result = mockMvc.perform(delete("/employee/" + EXISTING_EMPLOYEE_ID));

        result.andExpect(status().isNoContent());

    }
}