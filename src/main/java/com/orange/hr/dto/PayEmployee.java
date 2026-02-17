package com.orange.hr.dto;

import com.orange.hr.entity.Leave;
import com.orange.hr.entity.Salary;
import com.orange.hr.entity.SalaryAdjustment;

import java.util.List;

public interface PayEmployee {
    Integer getEmployeeId();

    List<SalaryAdjustment> getAdjustments();

    List<Leave> getLeaves();

    Salary getSalary();
}
