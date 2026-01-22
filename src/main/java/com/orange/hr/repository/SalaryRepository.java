package com.orange.hr.repository;

import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Salary;
import com.orange.hr.exceptions.NoSuchEmployeeException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Integer> {

    Optional<Salary> findFirstByEmployeeOrderByCreatedAtDesc(Employee employee);

    default Salary findCurrentSalaryByEmployee(Employee employee) {
        return findFirstByEmployeeOrderByCreatedAtDesc(employee).orElseThrow(() -> new NoSuchEmployeeException(HttpStatus.NOT_FOUND, "There's no Salary for that employee."));
    }
}