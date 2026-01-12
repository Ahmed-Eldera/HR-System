package com.orange.hr.repository;

import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Raise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RaiseRepository extends JpaRepository<Raise, Integer> {

    Optional<Raise> findFirstByEmployeeOrderByCreatedAtDesc(Employee employee);
}