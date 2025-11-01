package com.orange.hr.unit;

import com.orange.hr.entity.Department;
import com.orange.hr.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.flyway.enabled=true")
public class DepartmentUnitTest {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void CreateDepartment_WithName_ShouldReturnSavedEntity() {
        String deptName = "Department 1";
        Department dept = new Department(null, deptName);
        assertNull(dept.getDepartment_Id());
        dept = departmentRepository.save(dept);
        assertNotEquals(dept.getDepartment_Id(), null);
        assertEquals(dept.getName(), deptName);
    }

    @Test
    public void CreateDepartment_WithExistingName_shouldThrowException() {
        String deptName = "Department 1";
        Department dept1 = new Department(null, deptName);
        departmentRepository.save(dept1);
        Department dept2 = new Department(null, deptName);
        assertThrows(Exception.class, () -> departmentRepository.save(dept2));


    }
}
