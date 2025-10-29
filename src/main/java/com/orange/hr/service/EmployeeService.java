package com.orange.hr.service;

import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.dto.EmployeeResponseDTO;

public interface EmployeeService {
    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO employee);

}
