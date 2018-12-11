package com.example.quartz.startupRunner;

import com.example.communication.ComLink;
import com.example.communication.ServerManager;
import com.example.quartz.jobDispatcher.QuartzJob;
import com.example.quartz.jobDispatcher.QuartzManage;
import com.example.util.IpAndPortResolver;
import com.example.util.SpringUtil;

import org.apache.logging.log4j.LogManager;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/*初始化配置类
* 可以配置监听程序等*/
@Component
public class ConfigurationJobStartupRunner implements CommandLineRunner {


    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private  static Logger loggError = LoggerFactory.getLogger("demo_error");
    @Autowired
    private  IpAndPortResolver ipAndPortResolver;
    @Autowired
    @Qualifier("quartzManage")
    private QuartzManage quartzManage;
    public void exeJudge(){
        /*DBListener dbListener=new DBListener();
        dbListener.init();*/
        loggerInfo.info("正在注册JudgeJob.....");
        QuartzJob qj = new QuartzJob();
        qj.setJobName("judgeJob");
        qj.setTriggerName("judgeTrigger");
        qj.setCronExpression("0/10 * * * * ?");
        qj.setBeanName("com.example.quartz.job.JudgeJob");
        try{
            loggerInfo.info("正在执行\n");
             if ( quartzManage.checkJobIfExists(qj)){
                  quartzManage.resumeJob(qj);
                 loggerInfo.info("已唤醒judgejob.\n");
             }else{
                 loggerInfo.info("不存在judgetrigger，正在添加......\n");
                quartzManage.addJob(qj);}
        } catch (SchedulerException se){
            se.printStackTrace();
        }catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }catch(InstantiationException ie){
            ie.printStackTrace();
        }catch(IllegalAccessException iae){
            iae.printStackTrace();
        }
    }
    public void exePoll(){
        /*DBListener dbListener=new DBListener();
        dbListener.init();*/
        loggerInfo.info("正在注册PollJob.....");
        QuartzJob qj = new QuartzJob();
        qj.setJobName("pollJob");
        qj.setTriggerName("pollTrigger");
        qj.setCronExpression("0/10 * * * * ?");
        qj.setBeanName("com.example.quartz.job.PollingJob");
        try{
            loggerInfo.info("正在执行\n");
            if ( quartzManage.checkJobIfExists(qj)){
                quartzManage.resumeJob(qj);
            }else{
                loggerInfo.info("不存在polltrigger，正在添加......\n");
                quartzManage.addJob(qj);}
        } catch (SchedulerException se){
            se.printStackTrace();
        }catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }catch(InstantiationException ie){
            ie.printStackTrace();
        }catch(IllegalAccessException iae){
            iae.printStackTrace();
        }
    }
   /* * 测试*/
    /*@Autowired
    private ArmgJobQServiceImpl armgJobQServiceImpl;
    public  void testfindall(){
      *//*  List<ArmgjobqEntity> list = armgJobQServiceImpl.findAllByJobCreateTimeAfter(new SimpleDateFormat("yyyy-MM-dd hh24:mm:ss").format(new Date()));*//*
        List<ArmgjobqEntity> list1 = armgJobQServiceImpl.findAllByJobCreateTimeIsAfter(new Date());
        if( list1.size()==0)
        { System.out.println("数据库kong#####");
        }else{
            System.out.println("数据库bukong#####"+list1.size()+list1.isEmpty());
            System.out.println(new Date());
            for(ArmgjobqEntity armgjobqEntity :list1){
            System.out.println(armgjobqEntity.getJobCreateTime());}
        }

    }*/

      /*  armgJobQServiceImpl.upadteJobAccepted(1,"F",new Date());
        System.out.println("更新完成");*/

    @Override
    public void run(String... args) throws Exception{
        ipAndPortResolver.registerAddressAndId();
        exeJudge();
        exePoll();
        /* SpringUtil.t();*//*输出注册的bean,用于测试*/
    }
}
