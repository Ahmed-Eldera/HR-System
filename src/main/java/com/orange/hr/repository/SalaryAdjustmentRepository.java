package com.orange.hr.repository;

import com.orange.hr.entity.SalaryAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryAdjustmentRepository extends JpaRepository<SalaryAdjustment, Integer> {
}
