package com.orange.hr.payment;

import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Payment;
import com.orange.hr.service.EmployeeService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProcessor implements ItemProcessor<Employee, Payment> {
    @Autowired
    EmployeeService employeeService;

    @Override
    public Payment process(Employee employee) {
        return employeeService.pay(employee);

    }

}
