package com.orange.hr.payment;

import com.orange.hr.entity.Payment;
import com.orange.hr.repository.PaymentRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeWriter implements ItemWriter<Payment> {
    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public void write(Chunk<? extends Payment> items) throws Exception {
        paymentRepository.saveAll(items);
    }
}
