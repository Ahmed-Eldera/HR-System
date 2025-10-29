//package com.orange.hr.integration;
//
//
//import com.orange.hr.dto.DepartmentDTO;
//import com.orange.hr.dto.EmployeeRequestDTO;
//import com.orange.hr.dto.TeamDTO;
//import com.orange.hr.entity.Team;
//import com.orange.hr.enums.Gender;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//import java.time.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//public class EmployeeControllerIntegrationTest {
//    @Autowired
//    MockMvc mockMvc;
//    @Autowired
//    DepartmentRepository departmentRepository;
//    @Autowired
//    TeamRepository teamRepository;
//
//    @Test
//    public void AddEmpolyeeSuccessfully() throws Exception {
//
//        //Arrange
//        Department department = new Department(1,"Developers");
//        DepartmentDTO departmentDTO = new DepartmentDTO(1,"Developers");
//        departmentRepository.save(department);
//        Team team = new Team(1,"Project-X");
//        TeamDTO teamDTO = new TeamDTO(1,"Project-X");
//        teamRepository.save(team);
//        EmployeeRequestDTO employee = new EmployeeRequestDTO(1, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000, department.getDepartment_Id(), null, team.getTeam_id());
//
//        //act
//                ResultActions result = mockMvc.perform(post("/employees/{id}", employee));
//        //assert
//        result.andExpect(status().isCreated())
//              .andExpect(jsonPath("$.id").value(employee.getEmployee_id()))
//              .andExpect(jsonPath("$.name").value(employee.getEmployee_name()))
//              .andExpect(jsonPath("$.dateOfBirth").value(employee.getDateOfBirth()))
//              .andExpect(jsonPath("$.gender").value(employee.getGender()))
//              .andExpect(jsonPath("$.graduationDate").value(employee.getGraduationDate()))
//              .andExpect(jsonPath("$.salary").value(employee.getSalary()))
//              .andExpect(jsonPath("$.department").value(employee.getDepartment()))
////              .andExpect(jsonPath("$.manager").value(employee.getManager()))
//              .andExpect(jsonPath("$.team").value(employee.getTeam()));
//    }
//}
