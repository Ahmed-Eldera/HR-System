package com.orange.hr.integration;

import com.orange.hr.payment.PayrollScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentTest extends AbstractTest {
    @Autowired
    PayrollScheduler payrollScheduler;

    @Test
    public void pay_givenYoeLessThan10AvailableLeavesWithBonus() {

    }

    @Test
    public void pay_givenYoeLessThan10AvailableLeavesWithNoBonus() {

    }

    @Test
    public void pay_givenYoeLessThan10DeductionLeavesWithBonus() {

    }

    @Test
    public void pay_givenYoeLessThan10DeductionLeavesWithNoBonus() {

    }

    @Test
    public void pay_givenYoeMoreThan10AvailableLeavesWithBonus() {

    }

    @Test
    public void pay_givenYoeMoreThan10AvailableLeavesWithNoBonus() {

    }

    @Test
    public void pay_givenYoeMoreThan10DeductionLeavesWithBonus() {

    }

    @Test
    public void pay_givenYoeMoreThan10DeductionLeavesWithNoBonus() {

    }


}
