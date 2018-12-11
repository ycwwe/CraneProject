package com.example.quartz.job;


import com.example.quartz.job.sender.SendJobToXCC;
import com.example.quartz.job.sender.SendJobToXcc2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@DisallowConcurrentExecution
public class JudgeJob implements Job {
    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
  /* @Autowired
   private SendJobToXCC sendJobToXCC;*/
    @Autowired
    private SendJobToXcc2 sendJobToXcc2;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        loggerInfo.info("正在执行judger");
      /*if(sendJobToXCC!=null){
            sendJobToXCC.calculateMillisInterval();
        }*/
       /* System.out.println("??????????judgejob");*/
       if(sendJobToXcc2!=null){
            sendJobToXcc2.calculateMillisInterval();
        }else{
            loggerInfo.info("sendjobtoxcc2未注册");
        }

    }
}
