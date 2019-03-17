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
    private static Logger loggError = LoggerFactory.getLogger("demo_error");
    private static Queue<Integer> recSeqQueue = new ConcurrentLinkedDeque<>();
    @Autowired
    private JobTLC jobTLC;
    @Autowired
    private BlockMapTLC blockMapTLC;
    private static final List INSTANCEIDLIST = InfoMaintenanceClass.getINSTANCEIDLIST();

    /**
     * 从二进制数组转换Arrayist对象
     *
     * @param bytes 二进制数组
     * @return List<MessageForm>返回对象
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

    public void messageFilter(MessageForm messageForm) {
        int instanceId = messageForm.getInstanceNum();
           /* int classId=messageForm.getClassNum();
            int methodId=messageForm.getMethodNum();*/
        recSeqQueue.add(messageForm.getSequenceNum());
        Map paramMap = messageForm.getParameter();
        /*  if(INSTANCEIDLIST.contains(instanceId)){*/
        switch (instanceId) {
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
                loggError.error("XCC传入的instanceId错误，无法识别!jobId:" + paramMap.get("jobId"));
                break;
        }
          /* }else{
                loggError.error("XCC传入的instanceId错误，无法识别!jobId:"+paramMap.get("jobId"));
            }*/
    }

    public boolean delteRecSeqQueue(Integer recSeq) {
        recSeqQueue.remove(recSeq);
        return true;
    }
}
