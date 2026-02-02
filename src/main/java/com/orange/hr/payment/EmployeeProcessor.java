package com.orange.hr.payment;
import org.springframework.batch.item.ItemProcessor;

public class EmployeeProcessor implements ItemProcessor<String,String> {

        @Override
        public String process(String item) {
            return item.toUpperCase(); // Convert item to uppercase
        }

}
