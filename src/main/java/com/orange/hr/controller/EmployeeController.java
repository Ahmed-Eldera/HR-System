package com.orange.hr.controller;

import com.orange.hr.dto.EmployeeNodeDTO;
import com.orange.hr.dto.EmployeeRequestDTO;
import com.orange.hr.dto.EmployeeResponseDTO;
import com.orange.hr.dto.SalaryDTO;
import com.orange.hr.service.EmployeeService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PatchExchange;

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
    public ResponseEntity<EmployeeResponseDTO> modifyEmployee(@PathVariable("id") Integer id, @RequestBody EmployeeRequestDTO requestDTO) {
        EmployeeResponseDTO responseDTO = employeeService.modifyEmployee(id, requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Integer id){
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
    public ResponseEntity<EmployeeNodeDTO> getSubordinates(@PathVariable("id") Integer id){
        EmployeeNodeDTO employeeNodeDTO = employeeService.getSubordinates(id);
        return new ResponseEntity<>(employeeNodeDTO,HttpStatus.OK);
    }
}
