package com.orange.hr.repository;

import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Integer> {
    int countByEmployeeAndLeaveDateLessThanAndCreatedAtGreaterThanEqual(
            Employee employee,
            LocalDate currentDate,
            LocalDateTime thisYear);
}
