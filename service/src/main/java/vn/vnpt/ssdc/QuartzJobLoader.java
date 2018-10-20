package vn.vnpt.ssdc;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import vn.vnpt.ssdc.user.quartz.LoggingUserQuartzJob;

import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 * Created by THANHLX on 6/29/2017.
 */
@Component
public class QuartzJobLoader implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(QuartzJobLoader.class);

    @Autowired
    private Scheduler scheduler;

    public void run(ApplicationArguments args) {
        initLoggingUserQuartz();
    }

    public void initLoggingUserQuartz() {
        try {
            if(scheduler == null){
                logger.info("QuartzJobLoader no config....");
                return;
            }
            JobKey jobKey = new JobKey("Logging User Job");
            if (scheduler.getJobDetail(jobKey) != null) {
                logger.info("Exist logging user quartz job");
            } else {
                Date startDate = new Date();
                JobDetail job = JobBuilder.newJob(LoggingUserQuartzJob.class).withIdentity("Logging User Job").build();
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity("Logging User Trigger")
                        .startAt(startDate)
                        .withSchedule(simpleSchedule().withIntervalInSeconds(10).repeatForever())
                        .build();
                scheduler.scheduleJob(job, trigger);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
