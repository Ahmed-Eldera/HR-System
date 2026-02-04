package com.orange.hr.security;


import com.orange.hr.dto.LoginRequestDTO;
import com.orange.hr.entity.Employee;
import com.orange.hr.repository.EmployeeRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final EmployeeRepository employeeRepository;


    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            EmployeeRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.employeeRepository = userRepository;
    }


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