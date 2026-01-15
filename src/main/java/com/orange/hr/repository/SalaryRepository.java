package com.orange.hr.repository;

import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Integer> {

    Salary findFirstByEmployeeOrderByCreatedAtDesc(Employee employee);

    default Salary findByEmployee(Employee employee) {
        return findFirstByEmployeeOrderByCreatedAtDesc(employee);
    }
}