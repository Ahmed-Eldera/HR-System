package com.orange.hr.repository;

import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee,Integer> {
}
