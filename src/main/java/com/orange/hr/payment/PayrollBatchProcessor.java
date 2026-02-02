package com.orange.hr.payment;

import javax.sql.DataSource;

import com.orange.hr.entity.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class PayrollBatchProcessor {
    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("hello",jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    public CommandLineRunner run(JobLauncher jobLauncher, Job job) {
        return args -> {
            jobLauncher.run(
                    job,
                    new JobParametersBuilder()
                            .addLong("time", System.currentTimeMillis())
                            .toJobParameters()
            );
        };
    }

    @Bean
    public Step step1(ItemReader<String> reader,
                     ItemProcessor<String, String> processor,
                     ItemWriter<String> writer,
                     JobRepository jobRepository,
                      PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("gogo", jobRepository)
                .<String, String>chunk(3, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    @Bean
    public ItemReader<String> reader() {
        return new EmployeeReader();
    }


    @Bean
    public ItemProcessor<String, String> processor() {
        return new EmployeeProcessor();
    }


    @Bean
    public ItemWriter<String> writer() {
        return new EmployeeWriter();
    }

}