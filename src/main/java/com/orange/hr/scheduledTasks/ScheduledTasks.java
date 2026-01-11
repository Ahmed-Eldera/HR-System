package com.orange.hr.scheduledTasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //    @Scheduled(cron = "0 0 0 25 * * ")
//    @Scheduled(cron = @monthly)
    public void reportCurrentTime() {
        log.info("hr now {}", LocalDateTime.now());
    }
}
