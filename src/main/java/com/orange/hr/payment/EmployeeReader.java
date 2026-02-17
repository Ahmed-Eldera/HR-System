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
    private int currentBatchNo = 0;
    private int employeeIdLowerBound = 0;
    private List<Employee> employeeList = null;
    private long empCount;
    private int totalBatchesNo;

    @Override
    public Employee read() {
        if (employeeList == null) {
            empCount = employeeRepository.count();
            employeeList = employeeRepository.findTop10ByEmployeeIDGreaterThanOrderByEmployeeIDAsc(employeeIdLowerBound);
            totalBatchesNo = (int) Math.ceil((double) empCount / CHUNK_SIZE);
        }
        if (index < employeeList.size()) {
            System.out.println("EMPLOYEEEE PROCESSSSIINNNGGGGGGGGGG" + employeeList.get(index).getName());
            Employee employee = employeeList.get(index++);
            return employee;
        } else if (currentBatchNo < totalBatchesNo) {
            index = 0;
            currentBatchNo++;
            employeeIdLowerBound += CHUNK_SIZE;
            employeeList = employeeRepository.findTop10ByEmployeeIDGreaterThanOrderByEmployeeIDAsc(employeeIdLowerBound);
            return read();
        } else {
            return null;
        }
    }
}