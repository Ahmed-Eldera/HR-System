package com.orange.hr.unit;

import com.orange.hr.entity.Department;
import com.orange.hr.entity.Team;
import com.orange.hr.entity.Employee;
import com.orange.hr.enums.Gender;
import com.orange.hr.repository.DepartmentRepository;
import com.orange.hr.repository.EmployeeRepository;
import com.orange.hr.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

//@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeUnitTest {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    TeamRepository teamRepository;


    @Test
    public void CreateEmployee_givenValidDataWithNoExpertiseNoManager_shouldReturnSavedEmployee() {
        //no employee can exist without a department and a team

        //Arrange
        Employee emp = new Employee();
        emp.setEmployeeID(1);
        emp.setName("Ahmed");
        emp.setDateOfBirth(LocalDate.of(1999, 5, 12));
        emp.setGender(Gender.MALE);
        emp.setGraduationDate(LocalDate.of(2021, 7, 1));
        emp.setSalary(500F);
        Department department = new Department(null, "dept 1");
        departmentRepository.save(department);
        emp.setDepartment(department);
        Team team = new Team(null, "team 1");
        teamRepository.save(team);
        emp.setTeam(team);
        //act
        employeeRepository.save(emp);
        //assert
        assertNotNull(emp.getEmployeeID());
    }

    @Test
    public void CreateEmployee_givenMissingData_shouldThrowException() {
        //arrange
        Employee emp = new Employee();
        emp.setEmployeeID(1);
        emp.setName("Ahmed");
        //act&assert
        assertThrows(Exception.class, () -> employeeRepository.save(emp));
    }

    @Test
    public void CreateEmployee_givenValidDataWithManger_ShouldReturnSavedEmployee() {
        //Arrange
        Employee manager = new Employee();
        manager.setEmployeeID(1);
        manager.setName("Ahmed");
        manager.setDateOfBirth(LocalDate.of(1999, 5, 12));
        manager.setGender(Gender.MALE);
        manager.setGraduationDate(LocalDate.of(2021, 7, 1));
        manager.setSalary(500F);
        Department department = new Department(null, "dept 1");
        departmentRepository.save(department);
        manager.setDepartment(department);
        Team team = new Team(null, "team 1");
        teamRepository.save(team);
        manager.setTeam(team);
        employeeRepository.save(manager);
        Employee employee = new Employee();
        employee.setEmployeeID(2);
        employee.setName("Ahmed");
        employee.setDateOfBirth(LocalDate.of(1999, 5, 12));
        employee.setGender(Gender.MALE);
        employee.setGraduationDate(LocalDate.of(2021, 7, 1));
        employee.setSalary(500F);
        manager.setDepartment(department);
        manager.setTeam(team);
        employee.setManager(manager);
        employeeRepository.save(employee);
        assertEquals(employee.getManager().getEmployeeID(), manager.getEmployeeID());
    }
}
