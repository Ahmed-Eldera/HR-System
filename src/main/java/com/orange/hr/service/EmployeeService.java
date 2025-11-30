package com.orange.hr.service;

import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.dto.SalaryDTO;

import java.util.List;

public interface EmployeeService {
    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO employee);

    public EmployeeResponseDTO modifyEmployee(Integer id, EmployeeRequestDTO employee);

    public void deleteEmployeeAndReassignSubordinates(Integer id);

    public EmployeeResponseDTO getEmployee(Integer id);

    public SalaryDTO getSalary(Integer id);

    public List<EmployeeResponseDTO> getSubordinates(Integer id);
}