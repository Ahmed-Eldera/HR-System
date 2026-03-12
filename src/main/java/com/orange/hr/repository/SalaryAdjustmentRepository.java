package com.orange.hr.repository;

import com.orange.hr.entity.Employee;
import com.orange.hr.entity.SalaryAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalaryAdjustmentRepository extends JpaRepository<SalaryAdjustment, Integer> {
    List<SalaryAdjustment> findByEmployee(Employee employee);

    int countByEmployeeAndAmountLessThanAndCreatedAtGreaterThanEqual(Employee employee, Double amount, LocalDateTime startOfYear);

    List<SalaryAdjustment> findByEmployeeAndCreatedAtGreaterThanAndCreatedAtLessThanEqual(Employee employee, LocalDateTime startOfMonth, LocalDateTime endOfMonth);
}
