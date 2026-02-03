package com.orange.hr.payment;

import com.orange.hr.entity.Employee;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProcessor implements ItemProcessor<Employee, String> {

    @Override
    public String process(Employee item) {
        return item.getName().toUpperCase(); // Convert item to uppercase
    }

}
