package com.example.business;

import com.example.dao.service.XjdDaoServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("jobTLCImpl")
/*面向数据库*/
public class JobTLCCallBackImpl implements JobTLCCallBack {
    @Autowired
    private XjdDaoServiceImpl xjdDaoService;
    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private  static Logger loggError = LoggerFactory.getLogger("demo_error");
    @Override
    public void parseJobAcceptedReport(long jobId) {

     loggerInfo.info("XCC已经接收卸船作业，作业序号:"+jobId+"---------数据库正在更新作业状态.....");
     xjdDaoService.upadteJobAccepted((long)jobId,"A",new Date());
     loggerInfo.info("XCC已经接收卸船作业，作业序号:"+jobId+"---------数据库更新已完成!");
    }

    @Override
    public void parseJobStartedReport(long jobId, int predictedTime, int partOfJob) {
        String armgId=xjdDaoService.findArmgIdByJobOrderSeqMs(jobId);
        if(partOfJob==1) {
            loggerInfo.info("XCC正在执行卸船作业，作业序号:"+jobId+"--------第一部分（抓取）作业开始，数据库正在更新作业状态....");
            xjdDaoService.upadteJobStartedFirst(jobId,"s",(short)1,(short)0,armgId,(short)1,(short)3);
            loggerInfo.info("XCC正在执行卸船作业，作业序号:"+jobId+"--------第一部分（抓取）作业开始,数据库更新已完成");
        }else if(partOfJob==2){
            loggerInfo.info("XCC正在执行卸船作业，作业序号:"+jobId+"--------第二部分（放下）作业开始，数据库正在更新作业状态....");
            xjdDaoService.upadteJobStartedSecond(armgId, (short) 9);
            loggerInfo.info("XCC正在执行卸船作业，作业序号:"+jobId+"--------第二部分（放下）作业开始,数据库更新已完成");
        }else{
            loggError.error("jobid:"+jobId+"作业开始中回复partofjob不存在，应为2或1");
        }

    }

    @Override
    public void parseJobDoneReport( long jobId, int regarding, int action, int rejectCode) {
        if(regarding==2){
            if(action==1){
                loggerInfo.info("XCC正在执行卸船作业，作业序号:"+jobId+"--------第一部分（抓取）作业已经结束，数据库正在更新作业状态....");
                xjdDaoService.upadteJobDoneFirst(jobId,"Y",new Date());
                loggerInfo.info("XCC正在执行卸船作业，作业序号:"+jobId+"--------第一部分（抓取）作业已经结束，数据库更新已完成");
            }else{
                switch (action){
                    case 2:
                        loggerInfo.info(xjdDaoService.findArmgIdByJobOrderSeqMs(jobId)+":执行命令（"+jobId+"）出现错误，错误情况代码为"+action+",内容为：Manually in a faulty position 3 = Aborted\n" +
                              "4 = Rejected");
                    break;
                    case 3:
                        loggerInfo.info(xjdDaoService.findArmgIdByJobOrderSeqMs(jobId)+":执行命令（"+jobId+"）被取消，取消代码为"+action+",内容为：Aborted");
                        break;
                    case 4:
                        loggerInfo.info(xjdDaoService.findArmgIdByJobOrderSeqMs(jobId)+":执行命令（"+jobId+"）被拒绝，拒绝代码为"+action+",内容为： Rejected/"+"拒绝的原因是："+rejectCode);
                        break;
                }
            }
        }else if(regarding==1){
            loggerInfo.info("XCC执行完毕卸船作业，作业序号:"+jobId+"--------该卸船指令已经执行完毕，数据库正在更新作业状态....");
            xjdDaoService.upadteJobDoneSecond(jobId,"F","Y",new Date(),200);
            loggerInfo.info("XCC执行完毕卸船作业，作业序号:"+jobId+"--------该卸船指令已经执行完毕,数据库更新已完成");
        }else{
            loggError.error("jobid:"+jobId+"作业结束中regarding填充错误！");
        }
    }

    @Override
    public void parseWaitingReport(long jobId, int waitType, int latchedWeight) {
        String armgId=xjdDaoService.findArmgIdByJobOrderSeqMs(jobId);
        loggerInfo.info("ARMG"+armgId+"正在等待卸船作业:#"+jobId+"#被执行--------数据库正在更新作业状态......");
        xjdDaoService.updateWaitingStatus(jobId,(short)0,(short)1,armgId,(short)7,(short)2);
        loggerInfo.info("ARMG"+armgId+"正在等待卸船作业:#"+jobId+"#被执行--------数据库完成更新");
    }
}
