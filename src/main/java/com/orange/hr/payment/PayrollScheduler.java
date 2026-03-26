package com.orange.hr.payment;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class PayrollScheduler {
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    Job job;

    @Scheduled(cron = "${payroll.cron}")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Async
    public void generatePayroll() throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(
                job,
                new JobParametersBuilder()
                        .addLocalDate("Date : ", LocalDate.now().withDayOfMonth(25))
                        .toJobParameters()
        );
    }
}