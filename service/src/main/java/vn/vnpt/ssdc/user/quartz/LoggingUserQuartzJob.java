package vn.vnpt.ssdc.user.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import vn.vnpt.ssdc.user.services.LoggingUserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by THANHLX on 6/29/2017.
 */
public class LoggingUserQuartzJob implements Job {
    @Autowired
    LoggingUserService loggingUserService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(jobExecutionContext.getPreviousFireTime() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(jobExecutionContext.getPreviousFireTime());
            cal.add(Calendar.SECOND, -10);
            Date fromDate = cal.getTime();
            String fromDateTime = df.format(fromDate);

            cal.setTime(jobExecutionContext.getFireTime());
            cal.add(Calendar.SECOND, -10);
            Date endDate = cal.getTime();
            String endDateTime = df.format(endDate);

            loggingUserService.getUpdateMysql(fromDateTime, endDateTime);
        }
    }
}
