package com.orange.hr.unit;


import com.orange.hr.dto.DepartmentDTO;
import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.dto.TeamDTO;
import com.orange.hr.entity.Employee;
import com.orange.hr.enums.Gender;
import com.orange.hr.mapper.EmployeeMapper;
import com.orange.hr.repository.EmployeeRepository;
import com.orange.hr.service.Impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class EmployeeServiceUnitTest {
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    EmployeeMapper employeeMapper;
    @InjectMocks
    EmployeeServiceImpl employeeService;

    @Test
    public void addEmployee_givenValidData_shouldReturnSavedEmployee() {

        //Arrange
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO(1, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000, 1, 1, 1, null);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(1, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000, new DepartmentDTO(1, "dept 1"), 1, new TeamDTO(1, "team 1"), null);
        Employee emp = new Employee();
        emp.setEmployeeID(1);
        emp.setName("ahmed Eldera");
        when(employeeMapper.toEntity(employeeRequestDTO)).thenReturn(emp);
        when(employeeRepository.save(emp)).thenReturn(emp);
        when(employeeMapper.toDTO(emp).thenReturn(employeeResponseDTO));
        Employee result = employeeService.addEmployee(employeeRequestDTO);
        assertEquals(result.getEmployeeID(),employeeResponseDTO.getEmployeeID());
        assertEquals(result.getName(),employeeResponseDTO.getName());
    }
}
