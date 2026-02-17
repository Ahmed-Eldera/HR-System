package com.orange.hr.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PayrollScheduler {
    private static final Logger log = LoggerFactory.getLogger(PayrollScheduler.class);
//    @Autowired
//    JobLauncher jobLauncher;
//    @Autowired
//    Job job;

    @Scheduled(cron = "${payroll.cron}")
    public void reportCurrentTime() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//        log.info("The time is now " + LocalTime.now());
//        jobLauncher.run(
//                job,
//                new JobParametersBuilder()
//                        .addLong("time", System.currentTimeMillis())
//                        .toJobParameters()
//        );
    }
}