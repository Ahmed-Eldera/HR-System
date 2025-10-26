package com.orange.hr.unit;

import com.orange.hr.entity.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerUnitTest {
    @Mock
    private EmployeeService employeeService;
    @InjectMocks
    private EmployeeController employeeController;
    @Autowired
    MockMvc mockMvc;

    @Test
    public void AddEmpolyeeSuccessfully() throws Exception {

        //Arrange

        EmployeeDTO employee = new EmployeeDTO(1, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000, department.getDepartment_Id(), null, team.getTeam_id());
        when(employeeService.addEmployee(employee)).thenReturn(employee);

        //act
        ResultActions result = mockMvc.perform(post("/employees/{id}", employee));
        //assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(employee.getEmployee_id()))
                .andExpect(jsonPath("$.name").value(employee.getEmployee_name()))
                .andExpect(jsonPath("$.dateOfBirth").value(employee.getDateOfBirth()))
                .andExpect(jsonPath("$.gender").value(employee.getGender()))
                .andExpect(jsonPath("$.graduationDate").value(employee.getGraduationDate()))
                .andExpect(jsonPath("$.salary").value(employee.getSalary()))
                .andExpect(jsonPath("$.department").value(employee.getDepartment()))
//              .andExpect(jsonPath("$.manager").value(employee.getManager()))
                .andExpect(jsonPath("$.team").value(employee.getTeam()));
    }
}
