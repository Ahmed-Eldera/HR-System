package com.orange.hr.payment;

import com.orange.hr.entity.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class PayrollBatchProcessor {
    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("hello", jobRepository)
                .start(step1)
                .build();
    }


    @Bean
    public Step step1(ItemReader<Employee> reader,
                      ItemProcessor<Employee, String> processor,
                      ItemWriter<String> writer,
                      JobRepository jobRepository,
                      PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("gogo", jobRepository)
                .<Employee, String>chunk(10, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
