package com.orange.hr.service;

import com.orange.hr.dto.*;
import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Payment;
import com.orange.hr.entity.SalaryAdjustment;

import java.util.List;

public interface EmployeeService {
    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO employee);

    public EmployeeResponseDTO modifyEmployee(Integer id, EmployeeRequestDTO employee);

    public void deleteEmployeeAndReassignSubordinates(Integer id);

    public EmployeeResponseDTO getEmployee(Integer id);

    public SalaryDTO getSalary(Integer id);

    public List<EmployeeResponseDTO> getSubordinates(Integer id);

    public List<EmployeeResponseDTO> getDirectSubordinates(Integer managerId);

    public LeaveResponseDTO addLeave(Integer employeeId, LeaveRequestDTO requestDTO);

    public BonusResponseDTO addBonus(Integer employeeId, BonusRequestDTO requestDTO);

    public SalaryDTO addRaise(Integer employeeId, RaiseRequestDTO raisePercentage);

    Payment calculatePayment(Employee employee);

    List<SalaryAdjustment> calculateDeductions(Employee employee);
}