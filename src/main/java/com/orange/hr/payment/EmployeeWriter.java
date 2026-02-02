package com.orange.hr.payment;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class EmployeeWriter implements ItemWriter<String> {
    @Override
    public void write(Chunk<? extends String> items) throws Exception {
        items.forEach(System.out::println); // Print items to console
    }
}
