package com.orange.hr.unit;

import com.orange.hr.entity.Department;
import com.orange.hr.entity.Team;
import com.orange.hr.entity.Employee;
import com.orange.hr.enums.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeUnitTest {

    @Autowired
    private EmployeeRepository employeeRepository;


    @Test
    public void CreateEmployee_givenValidDataWithNoExpertiseNoManager_shouldReturnSavedEmployee() {
        //no employee can exist without a department and a team

        //Arrange
        Employee emp = new Employee();
        emp.setName("Ahmed");
        emp.setDateOfBirth(LocalDate.of(1999, 5, 12));
        emp.setGender(Gender.MALE);
        emp.setGraduationDate(LocalDate.of(2021, 7, 1));
        emp.setSalary(500F);
        Department department = new Department(1,"dept 1");
        emp.setDepartment(department);
        Team team = new Team(1,"team 1");
        emp.setTeam(team);
        employeeRepository.save(emp);
        assertNotNull(emp.getEmployeeID());

    }
}
