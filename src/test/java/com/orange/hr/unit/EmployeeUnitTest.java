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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        employeeRepository.save(emp);
        assertNotNull(emp.getEmployeeID());
    }

    @Test
    public void CreateEmployee_givenMissingData_shouldThrowException() {
        Employee emp = new Employee();
        emp.setEmployeeID(1);
        emp.setName("Ahmed");
        assertThrows(Exception.class,()->employeeRepository.save(emp));
    }
}
