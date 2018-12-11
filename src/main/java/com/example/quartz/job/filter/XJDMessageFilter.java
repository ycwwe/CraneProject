package com.example.quartz.job.filter;

import com.example.business.BlockMapTLC;
import com.example.business.JobTLC;

import com.example.quartz.job.sender.MessageForm;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class XJDMessageFilter {
    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private  static Logger loggError = LoggerFactory.getLogger("demo_error");
    private static Queue<Integer> recSeqQueue=new ConcurrentLinkedDeque<>();
    private String Message;
   /* private static final int[] INSTANCEID={1,2,3,4,5};
    private static final int[] CLASSID={1,2,3,4,5,6,100,101,102,103,104,105,110,111,201};
    private static final int[] JOB_TLCMETOHDID={1,2,3,4,5,6,7,8,9,10,11,12,13,14};*/
   @Autowired
    private JobTLC jobTLC;
   @Autowired
   private BlockMapTLC blockMapTLC;
    private static final  List INSTANCEIDLIST=InfoMaintenanceClass.getINSTANCEIDLIST();
    /*private static final  List CLASSIDLIST=InfoMaintenanceClass.getCLASSIDLIST();
    private static final  List JOB_TLCMETOHDIDLIST=InfoMaintenanceClass.getJobTlcmetohdidlist();
    private static final  Map<Integer,String> INSTANCEIDMAP=InfoMaintenanceClass.getINSTANCEIDMAP();
    private static final  Map<Integer,String> CLASSIDMAP=InfoMaintenanceClass.getCLASSIDMAP();
*/
    /*public static void main(String[] args){
        XJDMessageFilter xjdfilter= new XJDMessageFilter();
    }*/
    /*对传过来的ID
    判断*/
    /*private void messageFilter(List<MessageForm> messageFormList){
        for(MessageForm messageForm:messageFormList){
            int instanceId=messageForm.getInstanceNum();
            int classId=messageForm.getClassNum();
            int methodId=messageForm.getMethodNum();
            Map paramMap=messageForm.getParameter();
            if(INSTANCEIDLIST.contains(instanceId)&&CLASSIDLIST.contains(classId)*//*&&JOB_TLCMETOHDIDLIST.contains(methodId)*//*){
                if(classId!=2){*//*调用的类不是baseclass时*//*
                    if(INSTANCEIDMAP.get(instanceId).equals(CLASSIDMAP.get(classId))){*//*要调用的类名=实例名，则调用该类的方法*//*
                        switch (classId){
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 101:
                                if(JOB_TLCMETOHDIDLIST.contains(methodId)){
                                    *//*switch (methodId){
                                        case 1:
                                            int jobId1=(int)paramMap.get("jobId");
                                            jobTLC.jobAcceptedReport(jobId1);
                                            break;
                                        case 2:
                                            int jobId2=(int)paramMap.get("jobId");
                                            int predictedTime =(int)paramMap.get("predictedTime");
                                            int partOfJob=(int)paramMap.get("partOfJob");
                                            jobTLC.jobStartedReport(jobId2,predictedTime,partOfJob);
                                            break;
                                        case 3:
                                            int jobId3=(int)paramMap.get("jobId");
                                            int regarding=(int)paramMap.get("regarding");
                                            int action=(int)paramMap.get("action");
                                            int rejectCode=(int)paramMap.get("rejectCode");
                                            jobTLC.jobDoneReport(jobId3,regarding,action,rejectCode);
                                            break;
                                        case 4:
                                            int jobId4=(int)paramMap.get("jobId");
                                            int waitType=(int)paramMap.get("waitType");
                                            int latchedWeight=(int)paramMap.get("latchedWeight");
                                            jobTLC.waitingReport(jobId4,waitType,latchedWeight);
                                            break;
                                        case 5:
                                            break;}*//*
                                    jobTLC.messageReceived(messageForm);
                                }else{
                                    System.out.println("传入的methodId不存在，无法识别!jobId:"+paramMap.get("jobId"));
                                }
                                break;
                            case 103:
                                break;
                            case 105:
                                if(JOB_TLCMETOHDIDLIST.contains(methodId)){
                                    blockMapTLC.messageReceived(messageForm);
                                }else{
                                    System.out.println("传入的methodId不存在，无法识别!jobId:"+paramMap.get("jobId"));
                                }
                                break;

                        }
                    }else{
                        System.out.println("传入的instanceId和classId的组合不合法，无法识别!jobId:"+paramMap.get("jobId"));
                    }

                }else if(instanceId!=1&&classId==2){*//*调用父类BaseClass的方法*//*
                    switch (instanceId){
                case 2:
                    break;
                case 3:
                 *//*   switch (methodId){
                        case 1:
                            jobTLC.ack();
                             break;
                        case 2:
                            jobTLC.nak();
                             break;
                        case 3:
                             jobTLC.dataPartNak();
                             break;
                        case 4:
                            jobTLC.bufferNak();
                             break;
                        case 6:
                            jobTLC.logicalNak();
                             break;
                        case 5:
                            jobTLC.methodBusyNak();
                            break;

                }*//*
                    break;
                case 4:
                    break;
                case 5:
                    break;

                }
                }else {
                    System.out.println("传入的instanceId和classId的组合不合法，无法识别!jobId:"+paramMap.get("jobId"));
                }
                *//*switch (instanceId){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;

                }*//*
            }else{
                System.out.println("传入的instanceId或classId错误，无法识别!jobId:"+paramMap.get("jobId"));
            }
        }
    }*/
    /**
     * 从二进制数组转换Arrayist对象
     * @param bytes 二进制数组
     * @return  List<MessageForm>返回对象
     */
    private MessageForm getJobInfoListFromBytes(byte[] bytes) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(
                    arrayInputStream);
            MessageForm list = (MessageForm) inputStream
                    .readObject();
            inputStream.close();
            arrayInputStream.close();
            return list;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public  void messageFilter(MessageForm messageForm){
            int instanceId=messageForm.getInstanceNum();
           /* int classId=messageForm.getClassNum();
            int methodId=messageForm.getMethodNum();*/
           recSeqQueue.add(messageForm.getSequenceNum());
            Map paramMap=messageForm.getParameter();
          /*  if(INSTANCEIDLIST.contains(instanceId)){*/
               switch (instanceId){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    jobTLC.messageReceived(messageForm);
                    break;
                case 4:
                    break;
                case 5:
                    blockMapTLC.messageReceived(messageForm);
                    break;
                    default:
                       loggError.error("XCC传入的instanceId错误，无法识别!jobId:"+paramMap.get("jobId"));
                       break;
                }
          /* }else{
                loggError.error("XCC传入的instanceId错误，无法识别!jobId:"+paramMap.get("jobId"));
            }*/
        }
        public boolean delteRecSeqQueue(Integer recSeq){
        recSeqQueue.remove(recSeq);
        return  true;
        }
}
