package com.example.quartz.job;


import com.example.quartz.job.listener.XCCListener;
import com.example.quartz.job.poller.PollingDB;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Slf4j
@DisallowConcurrentExecution
public class JobTest1 implements Job {
    /*@Autowired
    private PollingDB pollingDB;
*/
    public void run() {

    }

    @Autowired
    private XCCListener xccListener;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}