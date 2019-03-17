package com.example.quartz.job.poller;

import com.example.dao.entity.Armgjobq;
import com.example.dao.service.XjdDaoServiceImpl;
import com.example.optimizer.OrderOptimizerImpl;
import com.example.quartz.job.filter.JobKindFilter;
import com.example.quartz.job.sender.SendJobToXcc2;
import com.example.quartz.job.sender.SendJobToXccByMina;
import com.example.util.Zookeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class PollingDB {
    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private static Logger loggError = LoggerFactory.getLogger("demo_error");
    private static Date jobCreateTime;
    private RuningInfoRecord runingInfoRecord=RuningInfoRecord.getRuningInfoRecordInstance();
    private Zookeeper zookeeper= Zookeeper.getZookeeperInstance();
    /*
            ifFirstPoll判断是否是第一次访问数据库的指示信号
            */
    private static boolean ifFirstPoll = true;
    @Autowired
    private XjdDaoServiceImpl xjdDaoService;
    @Autowired
    private SendJobToXccByMina sendJobToXccByMina;
    @Autowired
    private JobKindFilter jobKindFilter;
    @Autowired
    private OrderOptimizerImpl orderOptimizerImpl;

    public PollingDB() {
        if (isReboot()) {
            loggerInfo.info("堆场ECS系统正在重新启动......");
            ifFirstPoll = false;
        }
        zookeeper.doWithLock();
    }

    /*判断是否重新启动，不是返回false,是返回true*/
    private boolean isReboot(){
        if(null==zookeeper.checkIfFollower()){
            return false;
        }
        return true;
    }
    /*
     * 对表JOBQ进行查询，如果是第一次查询则调用fiandall,不是则调用findAllByJobCreateTimeAfter
     * */
    private List<Armgjobq> findAllAtOnce() {
        List<Armgjobq> list = null;
        if (ifFirstPoll) {
            loggerInfo.info("XJD第一次运行，正在从数据库查询JobQ所有等待执行记录......");
            list = xjdDaoService.findAllJob();
            if (list.isEmpty()) {
                /* throw new NullPointerException("数据库表ARMGJOBQ在初始化时未写入数据");*/
                loggerInfo.info("JOBQ表中没有等待执行的数据，返回的list为空！,等待数据库写入记录.......");
            } else {
                loggerInfo.info("查询到的数据库记录的数量：" + list.size());
                List<Date> listOfDate = new ArrayList<>();
                for (Armgjobq armgjobqEntity : list) {
                    listOfDate.add(armgjobqEntity.getJobCreateTime());
                }
                /*排序*/
                Collections.sort(listOfDate);
                /*  jobCreateTime=new SimpleDateFormat("yyyy-MM-dd hh24:mi:ss").format(listOfDate.get(0));*/
                jobCreateTime = listOfDate.get(listOfDate.size() - 1);/*获得最后job创建的时间*/
                ifFirstPoll = false;
                runingInfoRecord.setIfFirstPoll(ifFirstPoll).setJobCreateTime(jobCreateTime).updateRecordToDoc();
                zookeeper.writeToZK(jobCreateTime);
                               /* for(int i=0;i<listOfDate.size();i++){
                                    Date date = listOfDate.get(i);

                                }*/
            }
        } else {
            loggerInfo.info("对数据库进行轮询中，正在查询数据库表JOBQ中由TOS新添加的记录......");
            /* list= armgJobQServiceImpl.findAllByJobCreateTimeAfter(jobCreateTime);*/

            if (jobCreateTime == null)
            {/*重启后首次运行,双重检查*/
                try {
                    if(null!=zookeeper.readFromZK()){
                        jobCreateTime=zookeeper.readFromZK();
                    }
                    if(jobCreateTime==null){
                        jobCreateTime=runingInfoRecord.readTimeRecord();
                    }
                    if(jobCreateTime==null){
                        System.out.println("请按照如下形式 （yyyy-MM-dd空格HH:mm:ss），输入任务的开始时间：");
                        Scanner sc=new Scanner(System.in);
                        String str= sc.nextLine();
                        System.out.println(str);
                        jobCreateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
                    }
                    /*jobCreateTime= runingInfoRecord.readTimeRecord*/
                    loggerInfo.info("上次查询数据库表中最新插入的记录的记录创建时间：" + jobCreateTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
                list = xjdDaoService.findAllByJobCreateTimeAfter(jobCreateTime);
                if (list.isEmpty()) {
                    loggerInfo.info("JOBQ表中没有Tos新插入的数据，等待插入数据........");
                } else {
                    List<Date> listOfDate = new ArrayList<>();
                    for (Armgjobq armgjobqEntity : list) {
                        listOfDate.add(armgjobqEntity.getJobCreateTime());
                    }
                    Collections.sort(listOfDate);
                    /*jobCreateTime=new SimpleDateFormat("yyyy-MM-dd hh24:mm:ss").format(listOfDate.get(0))*/
                    jobCreateTime = listOfDate.get(0);/*获得最后job创建的时间*/
                    runingInfoRecord.setJobCreateTime(jobCreateTime).updateRecordToDoc();
                    zookeeper.writeToZK(jobCreateTime);
                }
            }
        return list;
    }
    /*将 sendJobToXCC和jobKindFilter进行组装*/
    private void recognizeAndCreateDischargeJobQueue(List<Armgjobq> listofjob) throws InterruptedException {
        /*  sendJobToXCC.sendPickUpChassisJob(jobKindFilter.getDISCHARGR_LIST(listofjob));*/
        jobKindFilter.start(listofjob);
        sendJobToXccByMina.sendAllJob(orderOptimizerImpl, jobKindFilter.getDISCHARGR_LIST(), jobKindFilter.getLOAD_LIST(), jobKindFilter.getSHUFFLE_LIST());
        /* sendJobToXcc2.sendPickUpChassisJob2(jobKindFilter.getDISCHARGR_LIST());*/
        /*jobKindFilter.setISSEND(true);*/
        /*   jobKindFilter.removeALLDISCHARGR_LIST();*/

    }

    /*
    调用此函数进行轮询数据库,与判断，发送分离开，可以实现解耦，又便于后期加入任务调度算法优化
    */
    public void startPollingDB() throws InterruptedException {
        List<Armgjobq> listofjob = findAllAtOnce();
        /*此处可加入任务调度算法进行优化*/
        if (!listofjob.isEmpty()) {
            loggerInfo.info("XJD正在生成作业.......");
            recognizeAndCreateDischargeJobQueue(listofjob);
        }
        /* xccListener.run();*/
    }

    public static Date getJobCreateTime() {
        return jobCreateTime;
    }

    public static void setJobCreateTime(Date jobCreateTime) {
        PollingDB.jobCreateTime = jobCreateTime;
    }

    public static boolean isIfFirstPoll() {
        return ifFirstPoll;
    }

    public static void setIfFirstPoll(boolean ifFirstPoll) {
        PollingDB.ifFirstPoll = ifFirstPoll;
    }

}
