package com.orange.hr.controller;

import com.orange.hr.dto.*;
import com.orange.hr.service.EmployeeService;
import com.orange.hr.validation.Always;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@RequestBody @Valid EmployeeRequestDTO requestDTO) {
        EmployeeResponseDTO responseDTO = employeeService.addEmployee(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> modifyEmployee(@PathVariable("id") Integer id, @RequestBody @Validated(Always.class) EmployeeRequestDTO requestDTO) {
        EmployeeResponseDTO responseDTO = employeeService.modifyEmployee(id, requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Integer id) {
        employeeService.deleteEmployeeAndReassignSubordinates(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable("id") Integer id) {
        EmployeeResponseDTO responseDTO = employeeService.getEmployee(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }

    @GetMapping("/{id}/salary")
    public ResponseEntity<SalaryDTO> getSalary(@PathVariable("id") Integer id) {
        SalaryDTO responseDTO = employeeService.getSalary(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }

    @GetMapping("/{id}/subordinates")
    public ResponseEntity<List<EmployeeResponseDTO>> getSubordinates(@PathVariable("id") Integer id) {
        List<EmployeeResponseDTO> response = employeeService.getSubordinates(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getDirectSubordinates(@RequestParam("managerId") Integer managerId) {
        List<EmployeeResponseDTO> response = employeeService.getDirectSubordinates(managerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{Id}/leave")
    public ResponseEntity<LeaveResponseDTO> addLeave(@PathVariable("Id") Integer employeeId, @RequestBody @Valid LeaveRequestDTO request) {
        LeaveResponseDTO response = employeeService.addLeave(employeeId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
