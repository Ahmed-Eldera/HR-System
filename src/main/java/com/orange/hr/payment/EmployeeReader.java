package com.orange.hr.payment;

import com.orange.hr.entity.Employee;
import com.orange.hr.repository.EmployeeRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeReader implements ItemReader<Employee> {
    private final int CHUNK_SIZE = 10;
    @Autowired
    EmployeeRepository employeeRepository;
    private int index = 0;
    private int employeeIdLowerBound = 0;
    private List<Employee> employeeList = null;

    @Override
    public Employee read() {
        if (employeeList == null) {
            employeeList = employeeRepository.findTop10ByEmployeeIDGreaterThanOrderByEmployeeIDAsc(employeeIdLowerBound);
            employeeList.forEach(e -> System.out.println(e.getName()));
        }
        if (index < employeeIdLowerBound + CHUNK_SIZE || index <= employeeList.size()) {
            return employeeList.get(index++);
        } else {
            employeeIdLowerBound += CHUNK_SIZE;
            employeeList = employeeRepository.findTop10ByEmployeeIDGreaterThanOrderByEmployeeIDAsc(employeeIdLowerBound);
            return read();
        }
    }
}