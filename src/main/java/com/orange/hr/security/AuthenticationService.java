package com.orange.hr.security;


import com.orange.hr.dto.LoginRequestDTO;
import com.orange.hr.entity.Employee;
import com.orange.hr.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthenticationService {
    private final EmployeeRepository employeeRepository;

    private final AuthenticationManager authenticationManager;

    public Employee authenticate(LoginRequestDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return employeeRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}