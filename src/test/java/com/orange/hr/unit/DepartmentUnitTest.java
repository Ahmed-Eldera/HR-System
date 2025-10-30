package com.orange.hr.unit;

import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.*;

public class DepartmentUnitTest {

    @Test
    public void CreateDepartment_WithName_ShouldReturnSavedEntity(){
        Department dept = new Department("department 1");
        assertEquals(dept.getId(),null);
        dept = departmentRepository.save(dept);
        assertNotEquals(dept.getId(),null);
        assertEquals(dept.getName(),"department 1");
    }
}
