package com.orange.hr.repository;

import com.orange.hr.entity.Adjustment;
import com.orange.hr.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AdjustmentRepository extends JpaRepository<Adjustment, Integer> {
    long countByEmployeeAndDateGreaterThanEqualAndDateLessThan(Employee employee, LocalDate startDate, LocalDate endDate);

}
