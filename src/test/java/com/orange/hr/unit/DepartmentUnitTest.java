package com.orange.hr.unit;

import com.orange.hr.entity.Department;
import com.orange.hr.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
public class DepartmentUnitTest {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Test
    public void CreateDepartment_WithName_ShouldReturnSavedEntity(){
        Department dept = new Department(null,"department 1");
        assertNull(dept.getDepartment_Id());
        dept = departmentRepository.save(dept);
        assertNotEquals(dept.getDepartment_Id(),null);
        assertEquals(dept.getName(),"department 1");
    }
}
