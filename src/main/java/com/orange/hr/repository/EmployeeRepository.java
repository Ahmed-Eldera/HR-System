package com.orange.hr.repository;

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

    List<Employee> findByManager(Employee manager);
}
