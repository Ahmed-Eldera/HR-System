package com.orange.hr.payment;

import com.orange.hr.entity.Employee;
import com.orange.hr.entity.Payment;
import com.orange.hr.entity.SalaryAdjustment;
import com.orange.hr.repository.EmployeeRepository;
import com.orange.hr.repository.PaymentRepository;
import com.orange.hr.repository.SalaryAdjustmentRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class PayrollConfig {
    private static final int CHUNK_SIZE = 100;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    SalaryAdjustmentRepository salaryAdjustmentRepository;

    @Bean
    public RepositoryItemReader<Employee> createReader() {
        return new RepositoryItemReaderBuilder<Employee>()
                .name("EmployeeItemReader")
                .repository(employeeRepository)
                .methodName("findAll")
                .pageSize(CHUNK_SIZE)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public RepositoryItemWriter<Payment> createPaymentWriter() {
        return new RepositoryItemWriterBuilder<Payment>()
                .repository(paymentRepository)
                .build();
    }

    @Bean
    public ItemWriter<List<SalaryAdjustment>> createDeductionWriter() {
        return items -> {
            for (List<SalaryAdjustment> adjustmentList : items) {
                salaryAdjustmentRepository.saveAll(adjustmentList);
            }
        };
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step generatePayments, Step applyDeductions) {
        return new JobBuilder("payroll", jobRepository)
                .start(applyDeductions)
                .next(generatePayments)
                .build();
    }

    @Bean
    public Step generatePayments(RepositoryItemReader<Employee> reader,
                                 ItemProcessor<Employee, Payment> processor,
                                 RepositoryItemWriter<Payment> writer,
                                 JobRepository jobRepository,
                                 PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("generatePaymentsStep", jobRepository)
                .<Employee, Payment>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step applyDeductions(RepositoryItemReader<Employee> reader,
                                ItemProcessor<Employee, List<SalaryAdjustment>> processor,
                                ItemWriter<List<SalaryAdjustment>> writer,
                                JobRepository jobRepository,
                                PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("deductionsStep", jobRepository)
                .<Employee, List<SalaryAdjustment>>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}