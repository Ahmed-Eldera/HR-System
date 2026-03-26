package com.orange.hr.payment;

import com.orange.hr.entity.Employee;
import com.orange.hr.entity.SalaryAdjustment;
import com.orange.hr.service.EmployeeService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Employee2DeductionProcessor implements ItemProcessor<Employee, List<SalaryAdjustment>> {
    @Autowired
    EmployeeService employeeService;

    @Override
    public List<SalaryAdjustment> process(Employee employee) {
        return employeeService.calculateDeductions(employee);
    }

}