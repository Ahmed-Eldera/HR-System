package com.orange.hr.payment;

import org.springframework.batch.item.ItemReader;


public class EmployeeReader implements ItemReader<String> {
    private String[] data = {"Alice", "Bob", "Charlie", "Diana"};
    private int index = 0;

    @Override
    public String read() {
        if (index < data.length) {
            return data[index++];
        }
        return null; // End of data
    }
}