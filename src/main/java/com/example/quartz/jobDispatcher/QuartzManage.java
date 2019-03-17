package com.example.quartz.jobDispatcher;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @Author by yc
 * @Descriptions
 * @Datetime in 2018/1/30 17:20.
 */

public class QuartzManage {

    @Autowired
    @Qualifier("scheduler")
    private Scheduler scheduler;

    public void addJob(QuartzJob job) throws SchedulerException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //通过类名获取实体类，即要执行的定时任务的类
        Class<?> clazz = Class.forName(job.getBeanName());
        Job jobEntity = (Job) clazz.newInstance();
        //通过实体类和任务名创建 JobDetail
        JobDetail jobDetail = newJob(jobEntity.getClass())
                .withIdentity(job.getJobName()).build();
        //通过触发器名和cron 表达式创建 Trigger
        Trigger cronTrigger = newTrigger()
                .withIdentity(job.getTriggerName())
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression()))
                .build();
        //执行定时任务
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }
        /*SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        Scheduler sched = schedFact.getScheduler();

        sched.start();
        sched.scheduleJob(jobDetail,cronTrigger);*/
        /*if(scheduler.checkExists(jobDetail.getKey())){
            scheduler.deleteJob(jobDetail.getKey());
        }*/

    /**
     * 更新job cron表达式
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void updateJobCron(QuartzJob quartzJob) throws SchedulerException {

        TriggerKey triggerKey = TriggerKey.triggerKey(quartzJob.getJobName());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression());
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 删除一个job
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void deleteJob(QuartzJob quartzJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(quartzJob.getJobName());

        scheduler.deleteJob(jobKey);
    }

    /**
     * 恢复一个job
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void resumeJob(QuartzJob quartzJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(quartzJob.getJobName());
        scheduler.resumeJob(jobKey);

    }

    /**
     * 立即执行job
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void runAJobNow(QuartzJob quartzJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(quartzJob.getJobName());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 暂停一个job
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void pauseJob(QuartzJob quartzJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(quartzJob.getJobName());
        scheduler.pauseJob(jobKey);
    }

    /*判断数据库中是否已存在某个job，存在返回值为true*/
    public boolean checkJobIfExists(QuartzJob quartzJob) throws SchedulerException {
        boolean bool = false;
        JobKey jobKey = JobKey.jobKey(quartzJob.getJobName());
        while (scheduler.checkExists(jobKey)) {
            for (Trigger trigger : scheduler.getTriggersOfJob(jobKey)){
                if (trigger.toString().equals(quartzJob.getTriggerName())) {
                    bool = true;
                    break;
                }
            }
            if (bool) {
                System.out.println("DB中已存在(jobkey,trigger):(" + jobKey + "," + ")\n" + "将重新运行DB已有的任务\n");
            }
        }
        return bool;
        /*JobKey jobKey = jobDetail.getKey();
       if( scheduler.checkExists(jobKey)){
           for(Trigger trigger :scheduler.getTriggersOfJob(jobKey)){
               scheduler.resumeJob(jobKey);
           };
       }*/

    }

    /*启用所有数据库中存在的Job*/
    public void startAllJobInStore() throws SchedulerException {
        try {
            for (String jobGroupName : scheduler.getJobGroupNames()) {
                JobKey jobKey = new JobKey(jobGroupName);
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                for (Trigger trigger : scheduler.getTriggersOfJob(jobKey)) {
                    scheduler.scheduleJob(jobDetail, trigger);
                }

            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        ;
    }

    public void shutDown() {
        try {
            System.out.println("shutDown....执行完毕");
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}


