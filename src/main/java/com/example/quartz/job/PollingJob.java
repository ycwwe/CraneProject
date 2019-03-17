package com.example.quartz.job;

import com.example.quartz.job.poller.PollingDB;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/*@Slf4j*/
@DisallowConcurrentExecution
public class PollingJob implements Job {
    @Autowired
    private PollingDB pollingDB;

    private void run() throws InterruptedException {
        pollingDB.startPollingDB();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}