package com.orange.hr.unit;


import com.orange.hr.dto.DepartmentDTO;
import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.dto.TeamDTO;
import com.orange.hr.entity.Department;
import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Team;
import com.orange.hr.enums.Gender;
import com.orange.hr.mapper.EmployeeMapper;
import com.orange.hr.repository.DepartmentRepository;
import com.orange.hr.repository.EmployeeRepository;
import com.orange.hr.repository.TeamRepository;
import com.orange.hr.service.Impl.EmployeeServiceImpl;
import net.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceUnitTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private TeamRepository teamRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    public void addEmployee_givenFullValidData_shouldReturnSavedEmployee() {

        //Arrange
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO(1, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000, 1, 1, 1, null);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(1, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000, new DepartmentDTO(1, "dept 1"), 1, new TeamDTO(1, "team 1"), null);
        Employee emp = new Employee();
        Optional<Department> department = Optional.of(new Department(1, "abc"));
        Optional<Team> team = Optional.of(new Team(1, "abc"));
        Optional<Employee> manager = Optional.of(new Employee());
        manager.get().setEmployeeID(1);
        emp.setName("ahmed Eldera");
        when(employeeMapper.toEntity(employeeRequestDTO)).thenReturn(emp);
        when(employeeRepository.save(emp)).thenReturn(emp);
        when(employeeMapper.toDTO(emp)).thenReturn(employeeResponseDTO);
        when(departmentRepository.findById(any())).thenReturn(department);
        when(teamRepository.findById(any())).thenReturn(team);
        when(employeeRepository.findById(employeeRequestDTO.getManagerId())).thenReturn(manager);

        //act
        EmployeeResponseDTO result = employeeService.addEmployee(employeeRequestDTO);

        //assert
        assertEquals(result.getEmployeeID(), employeeResponseDTO.getEmployeeID());
        assertEquals(result.getName(), employeeResponseDTO.getName());
    }
     @Test
    public void addEmployee_givenValidDataWithNoManager_shouldReturnSavedEmployee() {

        //Arrange
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO(1, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000, 1, null, 1, null);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(1, "ahmed ELdera", LocalDate.of(2003, 2, 18), Gender.MALE, LocalDate.of(2026, 4, 12), 1000, new DepartmentDTO(1, "dept 1"), null , new TeamDTO(1, "team 1"), null);
        Employee emp = new Employee();
        Optional<Department> department = Optional.of(new Department(1, "abc"));
        Optional<Team> team = Optional.of(new Team(1, "abc"));
        Optional<Employee> manager = Optional.empty();
        emp.setName("ahmed Eldera");
        when(employeeMapper.toEntity(employeeRequestDTO)).thenReturn(emp);
        when(employeeRepository.save(emp)).thenReturn(emp);
        when(employeeMapper.toDTO(emp)).thenReturn(employeeResponseDTO);
        when(departmentRepository.findById(any())).thenReturn(department);
        when(teamRepository.findById(any())).thenReturn(team);
        when(employeeRepository.findById(employeeRequestDTO.getManagerId())).thenReturn(manager);

        //act
        EmployeeResponseDTO result = employeeService.addEmployee(employeeRequestDTO);

        //assert
        assertEquals(result.getEmployeeID(), employeeResponseDTO.getEmployeeID());
        assertEquals(result.getName(), employeeResponseDTO.getName());
    }
}
