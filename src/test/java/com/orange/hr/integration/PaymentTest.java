package com.orange.hr.integration;

import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Payment;
import com.orange.hr.entity.Salary;
import com.orange.hr.payment.PayrollScheduler;
import com.orange.hr.repository.EmployeeRepository;
import com.orange.hr.repository.PaymentRepository;
import com.orange.hr.repository.SalaryAdjustmentRepository;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.orange.hr.entity.Employee.byLatestSalaryComparator;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    SalaryAdjustmentRepository salaryAdjustmentRepository;
    @Autowired
    JobRepository jobRepository;
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

    @BeforeEach
    void clearBatchTables() throws SQLException {
        String[] tables = {
                "BATCH_STEP_EXECUTION_CONTEXT",
                "BATCH_STEP_EXECUTION",
                "BATCH_JOB_EXECUTION_CONTEXT",
                "BATCH_JOB_EXECUTION_PARAMS",
                "BATCH_JOB_EXECUTION",
                "BATCH_JOB_INSTANCE"
        };
        dbUnitConnection.getConnection().createStatement().execute("SET REFERENTIAL_INTEGRITY FALSE");
        for (String table : tables) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
        }
        dbUnitConnection.getConnection().createStatement().execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    public void pay_givenYoeLessThan10AllowedLeavesWithNoBonus_NoDeduction() throws Exception {
        prepareDB("/datasets/payment/payment-YOELessThan10.xml");
        try (MockedStatic<LocalDateTime> dateTime = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
             MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(FIXED_NOW.toLocalDate());
            dateTime.when(LocalDateTime::now).thenReturn(FIXED_NOW);
            payrollScheduler.generatePayroll();
            Employee employee = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
            Salary salary = employee.getSalaries().stream().max(byLatestSalaryComparator()).get();
            List<Payment> payments = salary.getPayments();
            double expectedPayment = salary.getGross() - salary.getGross() * TAX
                    - INSURANCE;
            assertThat(salaryAdjustmentRepository.findByEmployee(employee).isEmpty());
            assertEquals(1, payments.size());
            assertEquals(expectedPayment, payments.getFirst().getAmount());
        }
    }

    @Test
    public void pay_givenYoeLessThan10With1ExceededLeavesWithNoBonus_ExpectDeduction() throws Exception {
        prepareDB("/datasets/payment/payment-YOELessThan10.xml");
        prepareDB("/datasets/payment/extraLeaves.xml", DatabaseOperation.INSERT);
        try (MockedStatic<LocalDateTime> dateTime = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
             MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(FIXED_NOW.toLocalDate());
            dateTime.when(LocalDateTime::now).thenReturn(FIXED_NOW);
            payrollScheduler.generatePayroll();
            Employee employee = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
            Salary salary = employee.getSalaries().stream().max(byLatestSalaryComparator()).get();
            List<Payment> payments = salary.getPayments();
            double expectedPayment = salary.getGross() - salary.getGross() * TAX
                    - INSURANCE - DEDUCTION_AMOUNT;
            assertThat(!salaryAdjustmentRepository.findByEmployee(employee).isEmpty());
            assertEquals(1, payments.size());
            assertEquals(expectedPayment, payments.getFirst().getAmount());
        }
    }

    @Test
    public void pay_givenYoeLessThan10With1ExceededLeavesWithBonus_ExpectDeduction() throws Exception {
        prepareDB("/datasets/payment/payment-YOELessThan10.xml");
        prepareDB("/datasets/payment/extraLeaves.xml", DatabaseOperation.INSERT);
        prepareDB("/datasets/payment/extraBonus.xml", DatabaseOperation.INSERT);
        try (MockedStatic<LocalDateTime> dateTime = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
             MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(FIXED_NOW.toLocalDate());
            dateTime.when(LocalDateTime::now).thenReturn(FIXED_NOW);
            payrollScheduler.generatePayroll();
            Employee employee = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
            Salary salary = employee.getSalaries().stream().max(byLatestSalaryComparator()).get();
            List<Payment> payments = salary.getPayments();

            double expectedPayment = salary.getGross() - salary.getGross() * TAX
                    - INSURANCE - DEDUCTION_AMOUNT + BONUS_AMOUNT;
            assertThat(!salaryAdjustmentRepository.findByEmployee(employee).isEmpty());
            assertEquals(1, payments.size());
            assertEquals(expectedPayment, payments.getFirst().getAmount());
        }
    }

    @Test
    public void pay_givenYoeMoreThan10AllowedLeavesWithNoBonus_NoDeduction() throws Exception {
        prepareDB("/datasets/payment/payment-YOEMoreThan10.xml");
        try (MockedStatic<LocalDateTime> dateTime = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
             MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(FIXED_NOW.toLocalDate());
            dateTime.when(LocalDateTime::now).thenReturn(FIXED_NOW);
            payrollScheduler.generatePayroll();
            Employee employee = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
            Salary salary = employee.getSalaries().stream().max(byLatestSalaryComparator()).get();
            List<Payment> payments = salary.getPayments();
            double expectedPayment = salary.getGross() - salary.getGross() * TAX
                    - INSURANCE;
            assertThat(salaryAdjustmentRepository.findByEmployee(employee).isEmpty());
            assertEquals(1, payments.size());
            assertEquals(expectedPayment, payments.getFirst().getAmount());
        }
    }

    @Test
    public void pay_givenYoeMoreThan10With1ExceededLeavesWithNoBonus_ExpectDeduction() throws Exception {
        prepareDB("/datasets/payment/payment-YOEMoreThan10.xml");
        prepareDB("/datasets/payment/extraLeaves.xml", DatabaseOperation.INSERT);
        try (MockedStatic<LocalDateTime> dateTime = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
             MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(FIXED_NOW.toLocalDate());
            dateTime.when(LocalDateTime::now).thenReturn(FIXED_NOW);
            payrollScheduler.generatePayroll();
            Employee employee = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
            Salary salary = employee.getSalaries().stream().max(byLatestSalaryComparator()).get();
            List<Payment> payments = salary.getPayments();
            double expectedPayment = salary.getGross() - salary.getGross() * TAX
                    - INSURANCE - DEDUCTION_AMOUNT;
            assertThat(!salaryAdjustmentRepository.findByEmployee(employee).isEmpty());
            assertEquals(1, payments.size());
            assertEquals(expectedPayment, payments.getFirst().getAmount());
        }
    }

    @Test
    public void pay_givenYoeMoreThan10With1ExceededLeavesWithBonus_ExpectDeduction() throws Exception {
        prepareDB("/datasets/payment/payment-YOEMoreThan10.xml");
        prepareDB("/datasets/payment/extraLeaves.xml", DatabaseOperation.INSERT);
        prepareDB("/datasets/payment/extraBonus.xml", DatabaseOperation.INSERT);
        try (MockedStatic<LocalDateTime> dateTime = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS);
             MockedStatic<LocalDate> date = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            date.when(LocalDate::now).thenReturn(FIXED_NOW.toLocalDate());
            dateTime.when(LocalDateTime::now).thenReturn(FIXED_NOW);
            payrollScheduler.generatePayroll();
            Employee employee = employeeRepository.findById(EXISTING_EMPLOYEE_ID).get();
            Salary salary = employee.getCurrentSalary();
            List<Payment> payments = salary.getPayments();
            double expectedPayment = salary.getGross() - salary.getGross() * TAX
                    - INSURANCE - DEDUCTION_AMOUNT + BONUS_AMOUNT;
            assertThat(!salaryAdjustmentRepository.findByEmployee(employee).isEmpty());
            assertEquals(1, payments.size());
            assertEquals(expectedPayment, payments.getFirst().getAmount());
        }
    }
}