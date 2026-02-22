package com.orange.hr.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PayrollJobStartupRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PayrollJobStartupRunner.class);

    private final JobLauncher jobLauncher;
    private final Job job;

    public PayrollJobStartupRunner(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Running Payroll Job on application startup...");

        try {
            jobLauncher.run(
                    job,
                    new JobParametersBuilder()
                            .addLong("time", System.currentTimeMillis()) // important for uniqueness
                            .toJobParameters()
            );
        } catch (JobExecutionAlreadyRunningException |
                 JobRestartException |
                 JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {

            log.error("Failed to run payroll job on startup", e);
        }
    }
}
