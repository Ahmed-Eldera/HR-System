package com.orange.hr.controller;


import com.orange.hr.payment.PayrollScheduler;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final PayrollScheduler payrollScheduler;

    @PostMapping
    public ResponseEntity<String> generatePayments() {
        payrollScheduler.generatePayroll();
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }
}
