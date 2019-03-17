package com.example.business;

import com.example.quartz.job.filter.InfoMaintenanceClass;
import com.example.quartz.job.sender.MessageForm;

import com.example.quartz.job.sender.SendJobToXcc2;
import com.example.quartz.job.sender.SendJobToXccByMina;
import com.example.util.SpringUtil;
import org.apache.logging.log4j.LogManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/*面向消息*/
/*@Component("jobTLC")*/
public class JobTLC extends BaseClass {
    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private static Logger loggError = LoggerFactory.getLogger("demo_error");
    private JobTLCCallBack jobTLCCallBack = null;

    /* @Autowired
     private JobTLCCallBackImpl jobTLCImpl;*/
    public JobTLC() {
        this.setJobTLCCallBack();
    }

    /*接口回调,从注册的bean中获取对象赋值，注册该类JobTLC时需要懒加载，以打破bean循环调用*/
    private void setJobTLCCallBack() {
        this.jobTLCCallBack = SpringUtil.getBean(JobTLCCallBackImpl.class);
    }

    ;

    /*过滤信息*/
    @Override
    public void messageReceived(MessageForm msg) {
        if (msg.getClassNum() == 2) {
            super.messageReceived(msg);
        } else if (msg.getClassNum() == 101 || msg.getClassNum() == 201) {
            switch (msg.getMethodNum()) {
                case 1:
                    this.jobAcceptedReport(jobTLCCallBack, msg);
                    super.ack(msg);
                    break;
                case 2:
                    this.jobStartedReport(jobTLCCallBack, msg);
                    super.ack(msg);
                    break;
                case 3:
                    if (msg.getClassNum() == 101) {
                        this.jobDoneReport(jobTLCCallBack, msg);
                    } else {
                        this.jobDoneReport2(jobTLCCallBack, msg);
                    }
                    super.ack(msg);
                    break;
                case 4:
                    this.waitingReport(jobTLCCallBack, msg);
                    super.ack(msg);
                    break;
                case 5:
                    this.transferPosReport(jobTLCCallBack, msg);
                    super.ack(msg);
                    break;
                case 6:
                    this.blockShuffleReport(jobTLCCallBack, msg);
                    super.ack(msg);
                    break;
                case 7:
                    this.parsePickUpChassisReport(jobTLCCallBack, msg);
                    super.ack(msg);
                    break;
                case 8:
                    this.setDownChassisReport(jobTLCCallBack, msg);
                    super.ack(msg);
                    break;
                case 9:
                    this.moveReport(jobTLCCallBack, msg);
                    break;
                case 10:
                    this.parkReport(jobTLCCallBack, msg);
                    break;
                case 11:
                    this.calibrateSensorsReport(jobTLCCallBack, msg);
                    break;
                case 12:
                    this.chassisShuffleReport(jobTLCCallBack, msg);
                    break;
                case 13:
                    this.pickUpTransferAreaReport(jobTLCCallBack, msg);
                    break;
                case 14:
                    this.setDownTransferAreaReport(jobTLCCallBack, msg);
                    break;
                case 15:
                    this.pickUpChassis2Report(jobTLCCallBack, msg);
                    break;
                case 16:
                    this.setDownChassis2Report(jobTLCCallBack, msg);
                    break;
                default:
                    loggError.error("XCC传入的methodId的错误，无法识别!jobId:" + msg.getMethodNum());
                    break;
            }
        } else {
            loggError.error("XCC传入的classId的错误，无法识别!jobId:");
        }
    }

    private void waitingReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
        if (jobTLCCallBack != null) {
            Map<String, String> map = msg.getParameter();
            jobTLCCallBack.parseWaitingReport(Long.valueOf(map.get("jobId")), Integer.valueOf(map.get("waitType")), Integer.valueOf(map.get("latchedWeight")));
        } else {
            loggError.error("回调接口没有赋值实现类！");
        }
    }

    private void transferPosReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
    }

    private void blockShuffleReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
    }

    private void parsePickUpChassisReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
    }

    private void setDownChassisReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
    }

    private void moveReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
    }

    private void parkReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
    }

    private void calibrateSensorsReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
    }

    private void chassisShuffleReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
    }

    private void pickUpTransferAreaReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
    }

    private void setDownTransferAreaReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
    }

    private void pickUpChassis2Report(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
    }

    private void setDownChassis2Report(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
    }

    private void jobDoneReport2(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
    }

    private void jobDoneReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
        if (jobTLCCallBack != null) {
            Map<String, String> map = msg.getParameter();
            String jobId = map.get("jobId");
            int regarding = Integer.valueOf(map.get("regarding"));
            if (regarding == 1) {
                /*SendJobToXCC.deleteSendMessageRecordQueue(jobId);*/
                SendJobToXccByMina.deleteSendMessageRecordQueue(jobId);
                jobTLCCallBack.parseJobDoneReport(Long.valueOf(jobId), regarding, Integer.valueOf(map.get("action")), Integer.valueOf(map.get("rejectCode")));
            } else {
                jobTLCCallBack.parseJobDoneReport(Long.valueOf(jobId), regarding, Integer.valueOf(map.get("action")), Integer.valueOf(map.get("rejectCode")));
            }
        } else {
            System.out.println("回调接口没有赋值实现类！");
        }
    }

    private void jobStartedReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
        if (jobTLCCallBack != null) {
            Map<String, String> map = msg.getParameter();
            jobTLCCallBack.parseJobStartedReport(Long.valueOf(map.get("jobId")), Integer.valueOf(map.get("predictedTime")), Integer.valueOf(map.get("partOfJob")));
        } else {
            System.out.println("回调接口没有赋值实现类！");
        }
    }

    private void jobAcceptedReport(JobTLCCallBack jobTLCCallBack, MessageForm msg) {
        if (jobTLCCallBack != null) {
            Map<String, String> map = msg.getParameter();
            String jobId = map.get("jobId");
            /* int seq= SendJobToXCC.deleteSeqAndjobIdMap(jobId);*//*删除SeqAndjobIdMap中的记录*/
            /* SendJobToXCC.deleteSendTimeAndJobIdMap(jobId);*//*删除SendTimeAndJobIdMap中的记录*/
            try{
                System.out.println(Thread.currentThread().getName()+":"+"jobid"+jobId);
                int seq = SendJobToXccByMina.deleteSeqAndjobIdMap(jobId);/*删除SeqAndjobIdMap中的记录*/
                SendJobToXccByMina.deleteSendTimeAndJobIdMap(jobId);/*删除SendTimeAndJobIdMap中的记录*/
                loggerInfo.info("XCC已经接收到XJD发送的作业指令，其消息序号为：" + seq + " jobId为：" + jobId);
                jobTLCCallBack.parseJobAcceptedReport(Long.valueOf(map.get("jobId")));
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            System.out.println("回调接口没有赋值实现类！");
        }
    }

}
