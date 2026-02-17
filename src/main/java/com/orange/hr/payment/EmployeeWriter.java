package com.orange.hr.payment;

import com.orange.hr.entity.Payment;
import com.orange.hr.repository.PaymentRepository;
import jakarta.persistence.EntityManager;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Component
public class EmployeeWriter implements ItemWriter<Payment> {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    DataSource dataSource;

    @Override
    public void write(Chunk<? extends Payment> items) throws Exception {
        String insertEmployeeSQL = "INSERT INTO payments(amount, salary_id) VALUES (?,?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement employeeStmt = connection.prepareStatement(insertEmployeeSQL)) {

            for (Payment p : items.getItems()) {
                employeeStmt.setString(1, p.getAmount().toString());
                employeeStmt.setString(2, p.getSalary().getSalaryId().toString());
                employeeStmt.addBatch();
            }

            employeeStmt.executeBatch();
        }
    }
}
