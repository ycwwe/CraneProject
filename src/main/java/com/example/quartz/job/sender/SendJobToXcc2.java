package com.example.quartz.job.sender;


import com.example.communication.uselesss.ComLink2;
import com.example.communication.mina.Message;
import com.example.optimizer.OrderOptimizer;
import com.example.quartz.job.filter.InfoMaintenanceClass;
import com.example.quartz.job.filter.JobCraneMethodEnum;
import com.example.quartz.job.filter.XCCClassEnum;
import com.example.quartz.job.filter.XCCInstanceEnum;
import com.example.util.ExcelHandler;
import com.example.util.JobInfo;
import com.example.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.Objects.requireNonNull;


public class SendJobToXcc2 {
    private ComLink2 comLink2;
    private Map<String, String> addressAndIdMap = InfoMaintenanceClass.addressAndIdMap;/*记录xcc的ip：port（value） 和 ID(key)*/
    private Map<String, Integer> addressAndSuffixMap = InfoMaintenanceClass.addressAndSuffixMap;/*记录注册到IOC容器中的comlink对象的IP地址+端口号(key) 和 注册到容器的comlink的后缀（value）*/
    private static final long MAXINTERVAL = 60000;/*超市未收到接受确认的最大时间间隔*/
    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private static Logger loggError = LoggerFactory.getLogger("demo_error");
    private static ConcurrentHashMap<String, Long> sendTimeAndJobIdMap = new ConcurrentHashMap<String, Long>();/*记录已发送给Xcc的jobID以及发送的时间所对应的毫秒数*/
    private static ConcurrentHashMap<String, Integer> seqAndjobIdMap = new ConcurrentHashMap<String, Integer>();/*记录发送给XCC的指令的序号对应得jobid,jobid为key*/
    private static Calendar calendar = Calendar.getInstance();/*获取同一个日历对象*/
    private static int sequenceNum = 0;/*发送给XCC的指令的序号*/
    private MessageForm messageForm;
    private static ConcurrentLinkedQueue<MessageForm> sendMessageRecordQueue = new ConcurrentLinkedQueue<MessageForm>();/*记录发送给XCC的作业*/
    private boolean isSuccess = false;
    private static ConcurrentLinkedQueue<MessageForm> waitToSendJobQueue = new ConcurrentLinkedQueue<MessageForm>();/*用于接收等待发送的作业*/
    public static void deleteSendMessageRecordQueue(String jobId) {
        for (MessageForm messageForm : sendMessageRecordQueue) {
            Map param = messageForm.getParameter();
            if (param.get("jobId").equals(jobId)) {
                sendMessageRecordQueue.size();
                if (sendMessageRecordQueue.remove(messageForm)) {
                    sendMessageRecordQueue.size();
                    loggerInfo.info("已经成功删除记录XJD发送给XCC作业的队列中，任务已被XCC执行完毕的作业信息");
                }
            }
        }

    }

    /*根据jobId 删除sendJobIdQueue的值*/
    public static void deleteSendTimeAndJobIdMap(String jobId) {
        sendTimeAndJobIdMap.remove(jobId);
    }

    /*根据jobId 删除seqAndjobIdMap的值*/
    public static Integer deleteSeqAndjobIdMap(String jobId) {
        return seqAndjobIdMap.remove(jobId);
    }

    /*
     * 传入参数paramters:
     *                  List<Map>
     * 生成要发送给XCC的list
     * */
    private List<MessageForm> returnDischargeMessageList(List<Map> parameters) {
        try {
            List<MessageForm> messageFormList = new ArrayList();
            int instanceNum = XCCInstanceEnum.JOB_CRANE.getClassId();
            int classNum = XCCClassEnum.JOB_CRANE.getClassId();
            int methodNum = JobCraneMethodEnum.PICKUPCHASSIS.getMethodId();
            for (Map paramap : parameters) {
                messageForm = new MessageForm();
                messageForm.setInstanceNum(instanceNum);
                messageForm.setClassNum(classNum);
                messageForm.setMethodNum(methodNum);
                messageForm.setReplyFlag(1);
                messageForm.setSender("XJD");
                messageForm.setSequenceNum(sequenceNum);
                messageForm.setParameter(paramap);
                String armgId = paramap.get("armgId").toString();
                String jobId = paramap.get("jobId").toString();
                messageForm.setReciver(armgId);
                seqAndjobIdMap.put(jobId, sequenceNum);
                messageFormList.add(messageForm);
                sequenceNum += 2;
            }
            loggerInfo.info("要发送给XCC的卸船作业指令队列" + messageFormList.toString());
            return messageFormList;
        } catch (Exception e) {
            loggError.error("产生discharge作业错误");
            e.printStackTrace();
        }
        return null;
    }

    private List<MessageForm> returnLoadMessageList(List<Map> parameters) {
        try {
            List<MessageForm> messageFormList = new ArrayList();
            int instanceNum = XCCInstanceEnum.JOB_CRANE.getClassId();
            int classNum = XCCClassEnum.JOB_CRANE.getClassId();
            int methodNum = JobCraneMethodEnum.SETDOWNCHASSIS.getMethodId();
            for (Map paramap : parameters) {
                messageForm = new MessageForm();
                messageForm.setInstanceNum(instanceNum);
                messageForm.setClassNum(classNum);
                messageForm.setMethodNum(methodNum);
                messageForm.setReplyFlag(1);
                messageForm.setSender("XJD");
                messageForm.setSequenceNum(sequenceNum);
                messageForm.setParameter(paramap);
                String armgId = paramap.get("armgId").toString();
                String jobId = paramap.get("jobId").toString();
                messageForm.setReciver(armgId);
                seqAndjobIdMap.put(jobId, sequenceNum);
                messageFormList.add(messageForm);
                sequenceNum += 2;
            }
            loggerInfo.info("要发送给XCC的装船作业指令队列" + messageFormList.toString());
            return messageFormList;
        } catch (Exception e) {
            loggError.error("产生load作业错误");
            e.printStackTrace();
        }
        return null;
    }

    private List<MessageForm> returnShuffleMessageList(List<Map> parameters) {
        try {
            List<MessageForm> messageFormList = new ArrayList();
            int instanceNum = XCCInstanceEnum.JOB_CRANE.getClassId();
            int classNum = XCCClassEnum.JOB_CRANE.getClassId();
            int methodNum = JobCraneMethodEnum.BLOCKSHUFFLE.getMethodId();
            for (Map paramap : parameters) {
                messageForm = new MessageForm();
                messageForm.setInstanceNum(instanceNum);
                messageForm.setClassNum(classNum);
                messageForm.setMethodNum(methodNum);
                messageForm.setReplyFlag(1);
                messageForm.setSender("XJD");
                messageForm.setSequenceNum(sequenceNum);
                messageForm.setParameter(paramap);
                String armgId = paramap.get("armgId").toString();
                String jobId = paramap.get("jobId").toString();
                messageForm.setReciver(armgId);
                seqAndjobIdMap.put(jobId, sequenceNum);
                messageFormList.add(messageForm);
                sequenceNum += 2;
            }
            loggerInfo.info("要发送给XCC的箱区换箱作业指令队列" + messageFormList.toString());
            return messageFormList;
        } catch (Exception e) {
            loggError.error("产生shuffle作业错误");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将ArrayList转化为二进制数组
     *
     * @param messageForm MessageForm>对象
     * @return 二进制数组
     */
    private byte[] getJobInfoBytesFromList(MessageForm messageForm) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    arrayOutputStream);
            objectOutputStream.writeObject(messageForm);
            objectOutputStream.flush();
            byte[] data = arrayOutputStream.toByteArray();
            int i = data.length;
            objectOutputStream.close();
            arrayOutputStream.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /*检测超时没回复的指令*/
    public void calculateMillisInterval() {
        /*  new Thread(()->{*/
        Calendar calendar = Calendar.getInstance();
        loggerInfo.info("正在判断是否存在:已被XJD发送的作业，该作业超时未被XCC接收，需要重新发给XCC");
        calendar.setTime(new Date());
        long millis = calendar.getTimeInMillis();
        for (ConcurrentHashMap.Entry<String, Long> entry : sendTimeAndJobIdMap.entrySet()) {
            System.out.println("记录的Map，和计算的值" + entry.getKey() + "\\" + String.valueOf(millis - entry.getValue()) + "\\" + sendTimeAndJobIdMap.toString());
            if (millis - entry.getValue() > MAXINTERVAL) {
                String jobid = entry.getKey();
                loggerInfo.info("jobId为：" + jobid + "的作业超时未被XCC接收，需要重新发给XCC");
                int seq = seqAndjobIdMap.get(jobid);
                if (sendMessageRecordQueue.size() != 0) {/*判断是否为空，不为空则执行下一步*/
                    for (MessageForm messageForm : sendMessageRecordQueue) {
                        if (messageForm.getSequenceNum() == seq) {/*分割线*/
                            String xccId = messageForm.getReciver();
                            if (addressAndIdMap.containsKey(xccId)) {
                                String address = addressAndIdMap.get(xccId);
                                int suffix = addressAndSuffixMap.get(address);
                                ComLink2 comLink = (ComLink2) SpringUtil.getBean("comLink" + String.valueOf(suffix));/*获取xccId对应的并注册到IOC容器中的comLink对象*/
                                loggerInfo.info("此次调用的comlink实力的ip:" + comLink.getClientAddress() + comLink.getClientPort());
                                loggerInfo.info("调用的comlink:" + comLink.toString());
                                if (comLink != null) {
                                    if (comLink.send(new Message(messageForm.toString()))) {
                                        this.isSuccess = true;
                                        String jobId = messageForm.getParameter().get("jobId").toString();/*将jobid取出存入sendJobIDQueue*/
                                        calendar.setTime(new Date());
                                        sendTimeAndJobIdMap.computeIfPresent(jobId, (k, v) -> v = calendar.getTimeInMillis());
                                        loggerInfo.info("接收到指令的XccID:" + messageForm.getReciver());
                                    } else {
                                        loggerInfo.info("发送失败:xccid:" + messageForm.getReciver() + "即将重新发送作业指令：" + messageForm.toString());
                                        this.isSuccess = false;
                                        while (!isSuccess) {
                                            loggerInfo.info("正在重新发送作业指令：" + messageForm.toString());
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            if (comLink.send(new Message(messageForm.toString()))) {
                                                this.isSuccess = true;
                                                loggerInfo.info("重新发送成功，该条作业指令为：" + messageForm.toString());
                                                String jobId = messageForm.getParameter().get("jobId").toString();/*将jobid取出存入sendJobIDQueue*/
                                                calendar.setTime(new Date());
                                                sendTimeAndJobIdMap.computeIfPresent(jobId, (k, v) -> v = calendar.getTimeInMillis());
                                                loggerInfo.info("接收到指令的XccID：" + messageForm.getReciver());
                                            } else {
                                                loggerInfo.info("重新发送失败，该条作业指令为：" + messageForm.toString());
                                                this.isSuccess = false;
                                            }
                                        }
                                    }
                                } else {
                                    loggError.error("XCC未连接,id:" + xccId);
                                }

                            } else {
                                loggerInfo.info("该XccID未注册：" + xccId);
                            }

                        }
                    }
                } else {
                    loggerInfo.info("sendMessageRecordQueue暂时为空");
                }

            }
        }
        loggerInfo.info("暂时不存在超时未被XCC接收的作业");

        /* }).start();*/
    }


/*
    通过调用这个方法发送卸船作业给XCC
    @param  List<Map>
    */

    /* xccListener.run();*/

    public void sendPickUpChassisJob2(List<Map> parameters) throws InterruptedException {
        waitToSendJobQueue.addAll(returnDischargeMessageList(parameters));
        for (MessageForm messageForm : waitToSendJobQueue) {
            Thread.sleep(200);
            String xccId = messageForm.getReciver();
            if (addressAndIdMap.containsKey(xccId)) {
                String address = addressAndIdMap.get(xccId);
                int suffix = addressAndSuffixMap.get(address);
                ComLink2 comLink = (ComLink2) SpringUtil.getBean("comLink" + String.valueOf(suffix));/*获取xccId对应的并注册到IOC容器中的comLink对象*/
                loggerInfo.info("此次调用的comlink实例的ip:" + comLink.getClientAddress() + ":" + comLink.getClientPort());
                loggerInfo.info("调用的comlink:" + comLink.toString());
                if (comLink != null) {
                    if (comLink.send(new Message(messageForm.toString()))) {
                        this.isSuccess = true;
                        waitToSendJobQueue.remove(messageForm);
                        sendMessageRecordQueue.add(messageForm);
                        String jobId = messageForm.getParameter().get("jobId").toString();/*将jobid取出存入sendJobIDQueue*/
                        calendar.setTime(new Date());
                        sendTimeAndJobIdMap.put(jobId, calendar.getTimeInMillis());
                        loggerInfo.info("接收到指令的XccID:" + messageForm.getReciver());
                    } else {
                        loggerInfo.info("发送失败:xccid:" + messageForm.getReciver() + "即将重新发送作业指令：" + messageForm.toString());
                        this.isSuccess = false;
                        while (!isSuccess) {
                            loggerInfo.info("正在重新发送作业指令：" + messageForm.toString());
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (comLink.send(new Message(messageForm.toString()))) {
                                this.isSuccess = true;
                                waitToSendJobQueue.remove(messageForm);
                                loggerInfo.info("重新发送成功，该条作业指令为：" + messageForm.toString());
                                sendMessageRecordQueue.add(messageForm);
                                String jobId = messageForm.getParameter().get("jobId").toString();/*将jobid取出存入sendJobIDQueue*/
                                calendar.setTime(new Date());
                                sendTimeAndJobIdMap.put(jobId, calendar.getTimeInMillis());
                                loggerInfo.info("接收到指令的XccID：" + messageForm.getReciver());
                            } else {
                                loggerInfo.info("重新发送失败，该条作业指令为：" + messageForm.toString());
                                this.isSuccess = false;
                            }
                        }
                    }
                } else {
                    loggError.error("XCC未连接,id:" + xccId);
                }

            } else {
                loggerInfo.info("该XccID未注册：" + xccId);
            }

        }
        /* xccListener.run();*/

    }

    private void addAllJob(OrderOptimizer optimizer, List<Map> dischargeParameters, List<Map> loadParameters, List<Map> shuffleParameters) {
        try {
            if (dischargeParameters.size() != 0) {
                waitToSendJobQueue.addAll(requireNonNull(returnDischargeMessageList(dischargeParameters)));
            }
            if (loadParameters.size() != 0) {
                    waitToSendJobQueue.addAll(requireNonNull(returnLoadMessageList(loadParameters)));
            }
            if (shuffleParameters.size() != 0) {
                waitToSendJobQueue.addAll(requireNonNull(returnShuffleMessageList(shuffleParameters)));
            }
            loggerInfo.info("正在对作业列表进行优化.......");
            optimizer.returnOptimizedOrder(waitToSendJobQueue);
        } catch (NullPointerException e) {
            loggError.error("传入的列表不能为空：{}", e);
        }

    }


    public void sendAllJob(OrderOptimizer optimizer, List<Map> dischargeParameters, List<Map> loadParameters, List<Map> shuffleParameters) throws InterruptedException {
        this.addAllJob(optimizer, dischargeParameters, loadParameters, shuffleParameters);
        for (MessageForm messageForm : waitToSendJobQueue) {
            Thread.sleep(200);
            String xccId = messageForm.getReciver();
            if (addressAndIdMap.containsKey(xccId)) {
                String address = addressAndIdMap.get(xccId);
                int suffix = addressAndSuffixMap.get(address);
                ComLink2 comLink = (ComLink2) SpringUtil.getBean("comLink" + String.valueOf(suffix));/*获取xccId对应的并注册到IOC容器中的comLink对象*/
                loggerInfo.info("此次调用的comlink实例的ip:" + comLink.getClientAddress() + comLink.getClientPort());
                loggerInfo.info("调用的comlink:" + comLink.toString());
                if (comLink != null) {
                    if (comLink.send(new Message(messageForm.toString()))) {
                        this.isSuccess = true;
                        waitToSendJobQueue.remove(messageForm);
                        sendMessageRecordQueue.add(messageForm);
                        String jobId = messageForm.getParameter().get("jobId").toString();/*将jobid取出存入sendJobIDQueue*/
                        calendar.setTime(new Date());
                        sendTimeAndJobIdMap.put(jobId, calendar.getTimeInMillis());
                        try {
                            ExcelHandler.getExcelHandlerInstance().addJobInfo(new JobInfo(xccId,jobId,new Date()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        loggerInfo.info("接收到指令的XccID:" + messageForm.getReciver());
                    } else {
                        loggerInfo.info("发送失败:xccid:" + messageForm.getReciver() + "即将重新发送作业指令：" + messageForm.toString());
                        this.isSuccess = false;
                        while (!isSuccess) {
                            loggerInfo.info("正在重新发送作业指令：" + messageForm.toString());
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (comLink.send(new Message(messageForm.toString()))) {
                                this.isSuccess = true;
                                waitToSendJobQueue.remove(messageForm);
                                loggerInfo.info("重新发送成功，该条作业指令为：" + messageForm.toString());
                                sendMessageRecordQueue.add(messageForm);
                                String jobId = messageForm.getParameter().get("jobId").toString();/*将jobid取出存入sendJobIDQueue*/
                                calendar.setTime(new Date());
                                sendTimeAndJobIdMap.put(jobId, calendar.getTimeInMillis());
                                try {
                                    ExcelHandler.getExcelHandlerInstance().addJobInfo(new JobInfo(xccId,jobId,new Date()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                loggerInfo.info("接收到指令的XccID：" + messageForm.getReciver());
                            } else {
                                loggerInfo.info("重新发送失败，该条作业指令为：" + messageForm.toString());
                                this.isSuccess = false;
                            }
                        }
                    }
                } else {
                    loggError.error("XCC未连接,id:" + xccId);
                }

            } else {
                loggerInfo.warn("该XccID未注册：" + xccId);
            }

        }
        /* xccListener.run();*/

    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        System.out.println(calendar.getTimeInMillis());

       /* Map map = new HashMap();
        map.put("sender", "XJD");
        map.put("reciever", "XCC");
        map.put("row", "120");
        MessageForm msgf = new MessageForm(2,3,1,map);

        ComLink serverLink = new ComLink("127.0.0.1", 8086);


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        while (true) {
            serverLink.send(message);
            MessageForm msgreci = serverLink.getMessage();
            System.out.println("client main "+msgreci.getClassNum());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
*/
    }
}

