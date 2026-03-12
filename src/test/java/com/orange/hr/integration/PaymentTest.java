package com.orange.hr.integration;

import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Payment;
import com.orange.hr.entity.Salary;
import com.orange.hr.payment.PayrollScheduler;
import com.orange.hr.repository.EmployeeRepository;
import com.orange.hr.repository.PaymentRepository;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.orange.hr.entity.Employee.byLatestSalaryComparator;

@SpringBatchTest
public class PaymentTest extends AbstractTest {
    private static final int EXISTING_EMPLOYEE_ID = 1;
    private static final int INSURANCE = 500;
    private static final Double TAX = 0.15d;
    private static final Double DEDUCTION_AMOUNT = 500d;
    private static final Double BONUS_AMOUNT = 125d;
    private static final LocalDateTime FIXED_NOW = LocalDateTime.of(2025, 2, 25, 1, 1);

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    PayrollScheduler payrollScheduler;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setupDatabaseTime() {
        jdbcTemplate.execute(
                "CREATE ALIAS LOCALTIMESTAMP AS "
                        + "'java.time.LocalDateTime m() { "
                        + "return java.time.LocalDateTime.of(2025, 2, 1, 0, 0, 0); }'"
        );
    }

    @Test
    public void pay_givenYoeLessThan10AllowedLeavesWithNoBonus_NoDeduction() throws Exception {
        prepareDB("/datasets/payment/payment-YOELessThan10.xml");
        try (MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(FIXED_NOW.toLocalDate());
            payrollScheduler.generatePayroll();
            Employee employee = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
            Salary salary = employee.getSalaries().stream().max(byLatestSalaryComparator()).get();
            List<Payment> payments = salary.getPayments();
            assert payments.size() == 1;
            assert payments.getFirst().getAmount() == salary.getGross() - salary.getGross() * TAX
                    - INSURANCE;
        }
    }

    @Test
    public void pay_givenYoeLessThan10With1ExceededLeavesWithNoBonus_ExpectDeduction() throws Exception {
        prepareDB("/datasets/payment/payment-YOELessThan10.xml");
        prepareDB("/datasets/payment/extraLeaves.xml", DatabaseOperation.INSERT);
        try (MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(FIXED_NOW.toLocalDate());
            payrollScheduler.generatePayroll();
            Employee employee = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
            Salary salary = employee.getSalaries().stream().max(byLatestSalaryComparator()).get();
            List<Payment> payments = salary.getPayments();
            assert payments.size() == 1;
            assert payments.getFirst().getAmount() == salary.getGross() - salary.getGross() * TAX
                    - INSURANCE - DEDUCTION_AMOUNT;
        }
    }

    @Test
    public void pay_givenYoeLessThan10With1ExceededLeavesWithBonus_ExpectDeduction() throws Exception {
        prepareDB("/datasets/payment/payment-YOELessThan10.xml");
        prepareDB("/datasets/payment/extraLeaves.xml", DatabaseOperation.INSERT);
        prepareDB("/datasets/payment/extraBonus.xml", DatabaseOperation.INSERT);
        try (MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(FIXED_NOW.toLocalDate());
            payrollScheduler.generatePayroll();
            Employee employee = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
            Salary salary = employee.getSalaries().stream().max(byLatestSalaryComparator()).get();
            List<Payment> payments = salary.getPayments();

            assert payments.size() == 1;
            assert payments.getFirst().getAmount() == salary.getGross() - salary.getGross() * TAX
                    - INSURANCE - DEDUCTION_AMOUNT + BONUS_AMOUNT;
        }
    }

    @Test
    public void pay_givenYoeMoreThan10AllowedLeavesWithNoBonus_NoDeduction() throws Exception {
        prepareDB("/datasets/payment/payment-YOEMoreThan10.xml");
        try (MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(FIXED_NOW.toLocalDate());
            payrollScheduler.generatePayroll();
            Employee employee = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
            Salary salary = employee.getSalaries().stream().max(byLatestSalaryComparator()).get();
            List<Payment> payments = salary.getPayments();
            assert payments.size() == 1;
            assert payments.getFirst().getAmount() == salary.getGross() - salary.getGross() * TAX
                    - INSURANCE;
        }
    }

    @Test
    public void pay_givenYoeMoreThan10With1ExceededLeavesWithNoBonus_ExpectDeduction() throws Exception {
        prepareDB("/datasets/payment/payment-YOEMoreThan10.xml");
        prepareDB("/datasets/payment/extraLeaves.xml", DatabaseOperation.INSERT);
        try (MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(FIXED_NOW.toLocalDate());
            payrollScheduler.generatePayroll();
            Employee employee = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
            Salary salary = employee.getSalaries().stream().max(byLatestSalaryComparator()).get();
            List<Payment> payments = salary.getPayments();
            assert payments.size() == 1;
            assert payments.getFirst().getAmount() == salary.getGross() - salary.getGross() * TAX
                    - INSURANCE - DEDUCTION_AMOUNT;
        }
    }

    @Test
    public void pay_givenYoeMoreThan10With1ExceededLeavesWithBonus_ExpectDeduction() throws Exception {
        prepareDB("/datasets/payment/payment-YOEMoreThan10.xml");
        prepareDB("/datasets/payment/extraLeaves.xml", DatabaseOperation.INSERT);
        prepareDB("/datasets/payment/extraBonus.xml", DatabaseOperation.INSERT);
        try (MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(FIXED_NOW.toLocalDate());
            payrollScheduler.generatePayroll();
            Employee employee = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
            Salary salary = employee.getCurrentSalary();
            List<Payment> payments = salary.getPayments();
            assert payments.size() == 1;
            assert payments.getFirst().getAmount() == salary.getGross() - salary.getGross() * TAX
                    - INSURANCE - DEDUCTION_AMOUNT + BONUS_AMOUNT;
        }
    }
}