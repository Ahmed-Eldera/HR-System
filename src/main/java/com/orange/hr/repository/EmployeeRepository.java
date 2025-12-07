package com.orange.hr.repository;

import com.orange.hr.dto.EmployeeHierarchyProjection;
import com.orange.hr.entity.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
                UPDATE Employee e
                SET e.manager.id = :newManagerId
                WHERE e.manager.id = :oldManagerId
            """)
    void reassignSubordinates(@Param("oldManagerId") Integer oldManagerId,
                              @Param("newManagerId") Integer newManagerId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = """
            WITH RECURSIVE employee_hierarchy(
                                                 employee_id,
                                                 name,
                                                 date_of_birth,
                                                 gender,
                                                 graduation_date,
                                                 salary,
                                                 department_id,
                                                 team_id,
                                                 manager_id
                                             ) AS (
                SELECT
                    employee_id,
                    name,
                    date_of_birth,
                    gender,
                    graduation_date,
                    salary,
                    department_id,
                    team_id,
                    manager_id
                FROM employees
                WHERE employee_id = :managerId
                       
                UNION ALL
                       
                SELECT
                    e.employee_id,
                    e.name,
                    e.date_of_birth,
                    e.gender,
                    e.graduation_date,
                    e.salary,
                    e.department_id,
                    e.team_id,
                    e.manager_id
                FROM employees e
                INNER JOIN employee_hierarchy eh
                    ON e.manager_id = eh.employee_id
            )
            SELECT
                eh.*,
                ex.expertise_id,
                ex.name as expertise_name
            FROM employee_hierarchy eh
            LEFT JOIN employees_expertise ee
                ON eh.employee_id = ee.employee_id
            LEFT JOIN expertises ex
                ON ee.expertise_id = ex.expertise_id
            ORDER BY eh.employee_id
            offset 1;
                        """,
            nativeQuery = true)
    List<EmployeeHierarchyProjection> findSubordinatesRec(@Param("managerId") Integer managerId);
}
