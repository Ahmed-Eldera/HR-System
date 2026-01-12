package com.orange.hr.integration;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.orange.hr.dto.BonusRequestDTO;
import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.dto.LeaveRequestDTO;
import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Expertise;
import com.orange.hr.entity.Leave;
import com.orange.hr.entity.SalaryAdjustment;
import com.orange.hr.enums.Gender;
import com.orange.hr.mapper.EmployeeMapper;
import com.orange.hr.repository.EmployeeRepository;
import com.orange.hr.repository.LeaveRepository;
import com.orange.hr.repository.SalaryAdjustmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerIntegrationTest extends AbstractTest {
    private static final int NON_EXISTENT_EMPLOYEE_ID = 999;
    private static final int EXISTING_EMPLOYEE_ID = 1;
    private static final int EXISTING_EMPLOYEE_ID2 = 2;
    private static final int LEAF_EMPLOYEE_ID3 = 3;
    private static final int SUPER_MANAGER_ID2 = 2;
    private static final int NEW_EMPLOYEE_ID = 2;
    private static final String EXISTING_EMPLOYEE_NAME = "Ahmed";
    private static final String NEW_EMPLOYEE_NAME = "Ahmed Eldera";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(2003, 2, 18);
    private static final LocalDate NEW_DATE_OF_BIRTH = LocalDate.of(2004, 2, 18);
    private static final LocalDate FUTURE_DATE_OF_BIRTH = LocalDate.of(2999, 2, 18);
    private static final LocalDate GRADUATION_DATE = LocalDate.of(2026, 2, 18);
    private static final LocalDate NEW_GRADUATION_DATE = LocalDate.of(2029, 2, 18);
    private static final float SALARY = 2000F;
    private static final float INVALID_SALARY = 100F;
    private static final float NEW_SALARY = 550F;
    private static final int DEPARTMENT_ID = 1;
    private static final int DEPARTMENT_ID2 = 2;
    private static final int NON_EXISTENT_DEPARTMENT_ID = 9876;
    private static final int TEAM_ID = 1;
    private static final int TEAM_ID2 = 2;
    private static final int NON_EXISTENT_TEAM_ID = 9876;
    private static final Integer MANAGER_ID = 1;
    private static final Integer MANAGER_ID2 = 2;
    private static final Integer NON_EXISTENT_MANAGER_ID = 99;
    private static final int EXPERTISE_ID = 1;
    private static final int EXPERTISE_ID2 = 1;
    private static final int NON_EXISTENT_EXPERTISE_ID = 123;
    private static final int INSURANCE = 500;
    private static final float TAX = 0.15f;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private LeaveRepository leaveRepository;
    @Autowired
    private SalaryAdjustmentRepository salaryAdjustmentRepository;

    @Test
    public void addEmpolyeeSuccessfully_WithFullData_ExpectCreated() throws Exception {
        prepareDB("/datasets/EmployeeController/AddEmployeeDataset.xml");
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
                Optional.of(MANAGER_ID),
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
                .andExpect(jsonPath("$.managerId").value(employee.getManagerId().get()))
                .andExpect(jsonPath("$.teamId").value(employee.getTeamId()))
                .andExpect(jsonPath("$.expertisesIds").value(employee.getExpertise()));
    }

    @Test
    public void addEmpolyeeSuccessfully_WithNoManager_ExpectCreated() throws Exception {
        prepareDB("/datasets/EmployeeController/AddEmployeeDataset.xml");
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
        ResultActions result = mockMvc
                .perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
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
    public void addEmpolyee_WithMissingData_ExpectBadRequest() throws Exception {
        prepareDB("/datasets/EmployeeController/AddEmployeeDataset.xml");
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
    public void addEmpolyee_WithDepartmentNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/EmployeeController/AddEmployeeDataset.xml");
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
                Optional.of(MANAGER_ID),
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
    public void addEmpolyee_WithTeamNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/EmployeeController/AddEmployeeDataset.xml");
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
                Optional.of(MANAGER_ID),
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
    public void addEmpolyee_WithManagerNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/EmployeeController/AddEmployeeDataset.xml");
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
                Optional.of(NON_EXISTENT_MANAGER_ID),
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
    public void addEmpolyee_WithBirthDateNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/EmployeeController/AddEmployeeDataset.xml");
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
                Optional.of(MANAGER_ID),
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
    public void addEmpolyee_WithExpertiseNotValid_ExpectNotFound() throws Exception {
        prepareDB("/datasets/EmployeeController/AddEmployeeDataset.xml");
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
                Optional.of(MANAGER_ID),
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
        prepareDB("/datasets/EmployeeController/ModifyEmployeeDataset.xml");
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
                Optional.of(MANAGER_ID2),
                TEAM_ID2,
                expertises
        );
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //act
        ResultActions result = mockMvc.perform(patch("/employee/" + EXISTING_EMPLOYEE_ID).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        //assert
        result.andExpect(status().isOk())
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
        prepareDB("/datasets/EmployeeController/ModifyEmployeeDataset.xml");
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
        prepareDB("/datasets/EmployeeController/ModifyEmployeeDataset.xml");
        //arrange
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
                .andExpect(jsonPath("$.managerId").value(MANAGER_ID2))
                .andExpect(jsonPath("$.teamId").value(TEAM_ID))
                .andExpect(jsonPath("$.expertisesIds").value(expertises));
    }

    @Test
    public void modifyEmployee_RemoveExpertises_ExpectOK() throws Exception {
        prepareDB("/datasets/EmployeeController/ModifyEmployeeDataset.xml");
        //arrange
        List<Integer> expertises = new ArrayList<>();
        EmployeeRequestDTO employee = new EmployeeRequestDTO();
        employee.setName(NEW_EMPLOYEE_NAME);
        employee.setExpertise(expertises);
        //act
        ResultActions result = mockMvc.perform(patch("/employee/" + EXISTING_EMPLOYEE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .writeValueAsString(employee)));
        //arrange
        List<Expertise> expertisesAfter = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get().getExpertises();
        //assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(employee.getName())) //assert the change happened
                .andExpect(jsonPath("$.dateOfBirth").value(DATE_OF_BIRTH.toString()))
                .andExpect(jsonPath("$.gender").value(Gender.MALE.toString()))
                .andExpect(jsonPath("$.graduationDate").value(GRADUATION_DATE.toString()))
                .andExpect(jsonPath("$.salary").value(SALARY))
                .andExpect(jsonPath("$.departmentId").value(DEPARTMENT_ID))
                .andExpect(jsonPath("$.managerId").value(MANAGER_ID2))
                .andExpect(jsonPath("$.teamId").value(TEAM_ID))
                .andExpect(jsonPath("$.expertisesIds").value(expertises));
        assertTrue(expertisesAfter.isEmpty());
    }

    @Test
    public void modifyEmployee_InValidSalary_ExpectBadRequest() throws Exception {
        prepareDB("/datasets/EmployeeController/ModifyEmployeeDataset.xml");
        //arrange
        List<Integer> expertises = new ArrayList<>();
        EmployeeRequestDTO employee = new EmployeeRequestDTO();
        employee.setSalary(INVALID_SALARY);
        employee.setExpertise(expertises);
        //act
        ResultActions result = mockMvc.perform(patch("/employee/" + EXISTING_EMPLOYEE_ID).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .writeValueAsString(employee)));
        //assert
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("must be greater than or equal to 500")); //assert the change happened

    }

    @Test
    public void deleteEmployee_WithManager_ShouldReturnNoContent() throws Exception {
        prepareDB("/datasets/EmployeeController/DeleteEmployee.xml");
        //act
        ResultActions result = mockMvc.perform(delete("/employee/" + EXISTING_EMPLOYEE_ID));

        result.andExpect(status().isNoContent());

    }

    @Test
    public void deleteEmployee_WithNoManager_ShouldReturnConflict() throws Exception {
        prepareDB("/datasets/EmployeeController/DeleteEmployee.xml");
        //arrange
        Employee entityBefore = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
        String beforeVal = objectMapper.writeValueAsString(employeeMapper.toDTO(entityBefore));
        //act
        ResultActions result = mockMvc.perform(delete("/employee/" + EXISTING_EMPLOYEE_ID2));
        //arrange
        Employee entityAfter = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
        String afterVal = objectMapper.writeValueAsString(employeeMapper.toDTO(entityAfter));
        //assert
        result.andExpect(status().isConflict());
        assertEquals(beforeVal, afterVal);
    }

    @Test
    public void deleteEmployee_WithNonExistentEmployee_ShouldReturnNotFound() throws Exception {
        prepareDB("/datasets/EmployeeController/DeleteEmployee.xml");
        //act
        ResultActions result = mockMvc.perform(delete("/employee/" + NON_EXISTENT_EMPLOYEE_ID));

        result.andExpect(status().isNotFound());

    }

    @Test
    public void deleteEmployee_WithAManagerThatHasEmployees_ShouldMoveEmployeesToHisManager() throws Exception {
        prepareDB("/datasets/EmployeeController/DeleteEmployee.xml");
        //arrange
        List<Employee> subordinatesBeforeReassign = employeeRepository.findById(EXISTING_EMPLOYEE_ID)
                .get()
                .getSubordinates();
        List<Integer> subordinatesIds = new ArrayList<>();
        for (Employee emp : subordinatesBeforeReassign) {
            subordinatesIds.add(emp.getEmployeeID());
        }
        //act
        ResultActions result = mockMvc.perform(delete("/employee/" + EXISTING_EMPLOYEE_ID));
        result.andExpect(status().isNoContent());
        //assert
        List<Employee> subordinatesAfterReassign = employeeRepository.findAllById(subordinatesIds);
        for (Employee emp : subordinatesAfterReassign) {
            assertEquals(SUPER_MANAGER_ID2, emp.getManager().getEmployeeID());
        }
        assertFalse(employeeRepository.findById(EXISTING_EMPLOYEE_ID).isPresent());
    }

    @Test
    public void getEmployee_WithValidEmployee_ShouldReturnOK() throws Exception {
        prepareDB("/datasets/EmployeeController/GetEmployee.xml");
        //act
        ResultActions result = mockMvc.perform(get("/employee/" + EXISTING_EMPLOYEE_ID));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeID").value(EXISTING_EMPLOYEE_ID))
                .andExpect(jsonPath("$.name").value(EXISTING_EMPLOYEE_NAME))
                .andExpect(jsonPath("$.dateOfBirth").value(DATE_OF_BIRTH.toString()))
                .andExpect(jsonPath("$.gender").value(Gender.MALE.toString()))
                .andExpect(jsonPath("$.graduationDate").value(GRADUATION_DATE.toString()))
                .andExpect(jsonPath("$.salary").value(SALARY))
                .andExpect(jsonPath("$.departmentId").value(DEPARTMENT_ID))
                .andExpect(jsonPath("$.managerId").isEmpty())
                .andExpect(jsonPath("$.teamId").value(TEAM_ID))
                .andExpect(jsonPath("$.expertisesIds").isEmpty());

    }

    @Test
    public void getEmployee_WithInValidEmployee_ShouldReturnNotFound() throws Exception {
        prepareDB("/datasets/EmployeeController/GetEmployee.xml");
        //act
        ResultActions result = mockMvc.perform(get("/employee/" + NON_EXISTENT_EMPLOYEE_ID));

        result.andExpect(status().isNotFound());

    }

    @Test
    public void getSalary_WithValidEmployee_ShouldReturnOK() throws Exception {
        prepareDB("/datasets/EmployeeController/GetSalary.xml");
        //prepare
        Float netSalary = SALARY - INSURANCE - SALARY * TAX; //net = gross - fixed 500 and - 15% tax
        //act
        ResultActions result = mockMvc.perform(get("/employee/" + EXISTING_EMPLOYEE_ID + "/salary"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.gross").value(SALARY))
                .andExpect(jsonPath("$.net").value(netSalary));
    }

    @Test
    public void getSalary_WithInValidEmployee_ShouldReturnNotFound() throws Exception {
        prepareDB("/datasets/EmployeeController/GetSalary.xml");
        //act
        ResultActions result = mockMvc.perform(get("/employee/" + NON_EXISTENT_EMPLOYEE_ID + "/salary"));
        //assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("Can't find the selected employee"));
    }


    @Test
    public void getSubordinates_WithValidEmployee_ShouldReturnOk() throws Exception {
        prepareDB("/datasets/EmployeeController/GetSubordinates.xml");
        //                     |-> leaf1
        // root -> directChild |
        //                     |-> leaf2

        final Integer ROOT_ID = 1;
        final Integer DIRECT_CHILD_ID = 2;
        final Integer LEAF_ID = 3;
        final Integer LEAF2_ID = 4;

        //arrange
        Employee directChild = employeeRepository.findById(DIRECT_CHILD_ID).get();
        Employee leaf1 = employeeRepository.findById(LEAF_ID).get();
        Employee leaf2 = employeeRepository.findById(LEAF2_ID).get();
        List<EmployeeResponseDTO> expectedSubs = new ArrayList<>();
        expectedSubs.add(employeeMapper.toDTO(directChild));
        expectedSubs.add(employeeMapper.toDTO(leaf1));
        expectedSubs.add(employeeMapper.toDTO(leaf2));
        String expectedOutput = objectMapper.writeValueAsString(expectedSubs);


        //act
        ResultActions result = mockMvc.perform(get("/employee/" + ROOT_ID + "/subordinates"));

        String actualOutput = result.andReturn().getResponse().getContentAsString();
        //assert
        result.andExpect(status().isOk());
        JSONAssert.assertEquals(expectedOutput, actualOutput, JSONCompareMode.NON_EXTENSIBLE);

    }

    @Test
    public void getSubordinates_WithInValidEmployee_ShouldReturnNotFound() throws Exception {
        prepareDB("/datasets/EmployeeController/GetSubordinates.xml");
        //arrange

        //act
        ResultActions result = mockMvc.perform(get("/employee/" + NON_EXISTENT_EMPLOYEE_ID + "/subordinates"));

        //assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("Can't find such employee."));

    }

    @Test
    public void getDirectEmployees_GivenValidManager_ShouldReturnOk() throws Exception {
        prepareDB("/datasets/EmployeeController/GetSubordinates.xml");
        Integer sub1 = 3;
        Integer sub2 = 4;
        //arrange
        List<Employee> employees = employeeRepository.findAllById(List.of(sub1, sub2));
        List<EmployeeResponseDTO> dtos = employees.stream().map(employeeMapper::toDTO).toList();

        String expectedResponse = objectMapper.writeValueAsString(dtos);

        //act
        ResultActions result = mockMvc.perform(get("/employee?managerId=" + MANAGER_ID2));
        String actualResponse = result.andReturn().getResponse().getContentAsString();
        //assert
        result.andExpect(status().isOk());
        JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    public void getDirectEmployees_GivenInValidManager_ShouldReturnNotFound() throws Exception {
        prepareDB("/datasets/EmployeeController/GetSubordinates.xml");

        //act
        ResultActions result = mockMvc.perform(get("/employee?managerId=" + NON_EXISTENT_MANAGER_ID));
        String actualResponse = result.andReturn().getResponse().getContentAsString();
        //assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("Can't find such Manager."));
    }

    @Test
    public void addLeave_GivenValidDate_ShouldReturnCreated() throws Exception {
        prepareDB("/datasets/EmployeeController/AddLeave.xml");
        //mocking
        LocalDate fixedNow = LocalDate.of(2000, 1, 1);
        try (MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(fixedNow);
            //arrange
            LocalDate leaveDate = LocalDate.of(2000, 01, 02);//same year as the system
            LeaveRequestDTO leave = new LeaveRequestDTO(leaveDate);
            //act
            ResultActions result = mockMvc.perform(post("/employee/" + EXISTING_EMPLOYEE_ID + "/leave")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                            .writeValueAsString(leave)));
            //assert
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.employeeId").value(EXISTING_EMPLOYEE_ID))
                    .andExpect(jsonPath("$.date").value(leave.getDate().toString()))
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.createdAt").isNotEmpty());
            List<Leave> totalLeaves = leaveRepository.findAll();
            Integer numberOfInsertedLeaves = 1;
            assertEquals(numberOfInsertedLeaves, totalLeaves.size());
            Leave expectedLeave = totalLeaves.getFirst();//1st entry because db is empty (check dataset)
            assertEquals(expectedLeave.getEmployee().getEmployeeID(), EXISTING_EMPLOYEE_ID);
            assertEquals(expectedLeave.getDate(), leave.getDate());
        }
    }

    @Test
    public void addLeave_GivenInValidDate_ShouldReturnBadRequest() throws Exception {
        prepareDB("/datasets/EmployeeController/AddLeave.xml");
        //mocking
        LocalDate fixedNow = LocalDate.of(2000, 1, 1);
        try (MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(fixedNow);
            //arrange
            LocalDate leaveDate = LocalDate.of(2022, 01, 02);//future year from the system (shouldn't be accepted)
            LeaveRequestDTO leave = new LeaveRequestDTO(leaveDate);
            //act
            ResultActions result = mockMvc.perform(post("/employee/" + EXISTING_EMPLOYEE_ID + "/leave")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                            .writeValueAsString(leave)));
            //assert
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.msg").value("You can only record leaves in the current year."));
        }
    }

    @Test
    public void addLeave_GivenInValidEmployee_ShouldReturnNotFound() throws Exception {
        prepareDB("/datasets/EmployeeController/AddLeave.xml");
        //mocking
        LocalDate fixedNow = LocalDate.of(2000, 1, 1);
        try (MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(fixedNow);
            //arrange
            LocalDate leaveDate = LocalDate.of(2000, 01, 02);
            LeaveRequestDTO leave = new LeaveRequestDTO(leaveDate);
            //act
            ResultActions result = mockMvc.perform(post("/employee/" + NON_EXISTENT_EMPLOYEE_ID + "/leave")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                            .writeValueAsString(leave)));
            //assert
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.msg").value("Can't find selected employee."));
        }
    }


    @Test
    public void addBonus_GivenValidAmount_ShouldReturnCreated() throws Exception {
        prepareDB("/datasets/EmployeeController/AddBonus.xml");

        //arrange
        final Double BONUS_AMOUNT = 500d;
        BonusRequestDTO bonus = new BonusRequestDTO(BONUS_AMOUNT);
        //act
        ResultActions result = mockMvc.perform(post("/employee/" + EXISTING_EMPLOYEE_ID + "/bonus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .writeValueAsString(bonus)));
        //assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.employeeId").value(EXISTING_EMPLOYEE_ID))
                .andExpect(jsonPath("$.amount").value(BONUS_AMOUNT))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());

        List<SalaryAdjustment> totalSalaryAdjustment = salaryAdjustmentRepository.findAll();
        Integer TOTAL_INSERTED_BONUSES_COUNT = 1;
        assertEquals(TOTAL_INSERTED_BONUSES_COUNT, totalSalaryAdjustment.size());

        SalaryAdjustment savedBonus = totalSalaryAdjustment.getFirst();
        assertEquals(BONUS_AMOUNT, savedBonus.getAmount());
        assertEquals(EXISTING_EMPLOYEE_ID, savedBonus.getEmployee().getEmployeeID());
        assertNotNull(savedBonus.getAdjustmentId());
        assertNotNull(savedBonus.getCreatedAt());
    }

    @Test
    public void addBonus_GivenNonExistingEmployee_ShouldReturnNotFound() throws Exception {
        prepareDB("/datasets/EmployeeController/AddBonus.xml");

        //arrange
        final Double BONUS_AMOUNT = 500d;
        BonusRequestDTO bonus = new BonusRequestDTO(BONUS_AMOUNT);
        //act
        ResultActions result = mockMvc.perform(post("/employee/" + NON_EXISTENT_EMPLOYEE_ID + "/bonus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .writeValueAsString(bonus)));
        //assert
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("No Such Employee."));
    }

    @Test
    public void addBonus_GivenNegativeAmount_ShouldReturnBadRequest() throws Exception {
        prepareDB("/datasets/EmployeeController/AddBonus.xml");

        //arrange
        final Double BONUS_AMOUNT = -500d;
        BonusRequestDTO bonus = new BonusRequestDTO(BONUS_AMOUNT);
        //act
        ResultActions result = mockMvc.perform(post("/employee/" + EXISTING_EMPLOYEE_ID + "/bonus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .writeValueAsString(bonus)));
        //assert
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("amount can't be negative."));
    }
}

