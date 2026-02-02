package com.orange.hr.payment;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import org.slf4j.Logger;

@Component
public class payrollScheduler {

    private static final Logger log = LoggerFactory.getLogger(payrollScheduler.class);

    @Scheduled(cron = "${payroll.cron}")
    public void reportCurrentTime() {
        log.info("The time is now " + LocalTime.now());
    }
}