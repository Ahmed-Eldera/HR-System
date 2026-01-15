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


    @Query(value = """
            WITH RECURSIVE employee_hierarchy (
                employee_id,
                name,
                date_of_birth,
                gender,
                graduation_date,
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
                    e.department_id,
                    e.team_id,
                    e.manager_id
                FROM employees e
                INNER JOIN employee_hierarchy eh
                    ON e.manager_id = eh.employee_id
            ),

            latest_salary (
                employee_id,
                gross_salary
            ) AS (
                SELECT
                    s.employee_id,
                    s.gross_salary
                FROM salaries s
                INNER JOIN (
                    SELECT employee_id, MAX(created_at) AS max_created_at
                    FROM salaries
                    GROUP BY employee_id
                ) latest
                    ON s.employee_id = latest.employee_id
                   AND s.created_at = latest.max_created_at
            )

            SELECT
                eh.employee_id,
                eh.name,
                eh.date_of_birth,
                eh.gender,
                eh.graduation_date,
                ls.gross_salary AS salary,
                eh.department_id,
                eh.team_id,
                eh.manager_id,
                ex.expertise_id,
                ex.name AS expertise_name
            FROM employee_hierarchy eh
            LEFT JOIN latest_salary ls
                ON eh.employee_id = ls.employee_id
            LEFT JOIN employees_expertise ee
                ON eh.employee_id = ee.employee_id
            LEFT JOIN expertises ex
                ON ee.expertise_id = ex.expertise_id
            WHERE eh.employee_id != :managerId
            ORDER BY eh.employee_id
            """,
            nativeQuery = true)
    List<EmployeeHierarchyProjection> findSubordinatesRec(
            @Param("managerId") Integer managerId
    );

    List<Employee> findByManager(Employee manager);

}
