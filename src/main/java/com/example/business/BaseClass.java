package com.example.business;

import com.example.communication.ComLink;
import com.example.communication.ComLink2;
import com.example.communication.Message;
import com.example.quartz.job.filter.InfoMaintenanceClass;
import com.example.quartz.job.sender.MessageForm;
import com.example.util.SpringUtil;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*父类方法，且是抽象类不可被实例化*/
public abstract class BaseClass {
    /*@Autowired
    @Qualifier("comLink")
    private ComLink comLink;
    @Autowired
    @Qualifier("comLinkTwo")
    private ComLink comLinkTwo;*/


    private static org.slf4j.Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private  static Logger loggError = LoggerFactory.getLogger("demo_error");
    private static final String JOBTLC="com.example.business.JobTLC";
    private static final String BLOCKMAPTLC="com.example.business.BlockMapTLC";
    private  Map<String,String> addressAndIdMap= InfoMaintenanceClass.addressAndIdMap;/*记录xcc的ip：port（value） 和 ID(key)*/
    private  Map<String,Integer> addressAndSuffixMap=InfoMaintenanceClass.addressAndSuffixMap;/*记录注册到IOC容器中的comlink对象的IP地址+端口号(key) 和 注册到容器的comlink的后缀（value）*/
    protected void ack(MessageForm messageForm) {
      /*  获取调用者的类名*/
        String className= new Throwable().getStackTrace()[1].getClassName();
        Map<String,String> param =new HashMap<String,String>(){{
        }};
        String reciver = messageForm.getSender();
        MessageForm reply=new MessageForm(messageForm.getInstanceNum(),2,2,2,messageForm.getSequenceNum(),"XJD",reciver,param);
        loggerInfo.info("XJD已经接收到XCC回复到的消息，即将发送确认消息给:"+reciver);
        if(addressAndIdMap.containsKey(reciver)){
            String address=addressAndIdMap.get(reciver);
            int suffix=addressAndSuffixMap.get(address);
            ComLink2 comLink= (ComLink2) SpringUtil.getBean("comLink"+String.valueOf(suffix));/*获取xccId对应的并注册到IOC容器中的comLink对象*/
            boolean isSuccess=false;
            if(comLink!=null){
                if( comLink.send(new Message(messageForm.toString()))){
                    isSuccess=true;
                    loggerInfo.info("成功发送确认消息给XccID:"+messageForm.getReciver());
                }else{
                    loggerInfo.info("发送确认失败:xccid:"+messageForm.getReciver()+"即将重新发送确认消息："+messageForm.toString());
                    isSuccess=false;
                    while(!isSuccess){
                        loggerInfo.info("正在重新发送确认消息："+messageForm.toString());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(comLink.send(new Message(messageForm.toString()))){
                          isSuccess=true;
                            loggerInfo.info("重新发送发送确认消息成功，该条作业指令为："+messageForm.toString());
                            String jobId= messageForm.getParameter().get("jobId").toString();/*将jobid取出存入sendJobIDQueue*/
                        }else{
                            loggerInfo.info("重新发送确认消息失败，该条作业指令为："+messageForm.toString());
                           isSuccess=false;
                        }
                    }
                }
            }else{
                loggError.error("XCC未连接,id:"+reciver);
            }

        }else{
            loggerInfo.info("该XccID未注册："+reciver);
        }
       /* switch(reciver){
            case"XCC_ONE":
                comLink.send(new Message(reply.toString()));
                loggerInfo.info("已经向XCC_ONE返回确认消息，消息序号"+messageForm.getSequenceNum());
                break;
            case"XCC_TWO":
                comLinkTwo.send(new Message(reply.toString()));
                loggerInfo.info("已经向XCC_TWO返回确认消息，消息序号"+messageForm.getSequenceNum());
                break;
        }*/


    }
    protected void ack() {
        /*获取调用者的类名*/
        String className= new Throwable().getStackTrace()[1].getClassName();
        System.out.println("ack()被"+className+"调用");
    }
   /*  protected void ack(MessageForm messageForm) {
    *//* 获取调用者的类名*//*
        String className= new Throwable().getStackTrace()[1].getClassName();
        Map<String,String> param =new HashMap<String,String>(){{
        }};
        MessageForm reply=new MessageForm(messageForm.getInstanceNum(),2,2,param);
       *//* comLink.send(new Message(reply.toString()));
        switch (className){
            case JOBTLC:
                MessageForm reply=new MessageForm(messageForm.getInstanceNum(),2,1,param);
                break;
            case  BLOCKMAPTLC:
                MessageForm reply=new MessageForm(messageForm.getInstanceNum(),2,1,param);
                break; }*//*

    }*/
    protected  int ack(int i,String str) {
        /*System.out.println("ack()被调用");*/
        return i;
    }
    public void messageReceived(MessageForm msg) {

            switch(msg.getMethodNum()) {
                case 1:
                    this.parseNak(msg);
                    break;
                case 2:
                    this.parseAck(msg);
                    break;
                case 3:
                    this.parseDataPartNak(msg);
                    break;
                case 4:
                    this.parseBufferNak(msg);
                    break;
                case 5:
                    this.parseMethodBusyNak(msg);
                    break;
                case 6:
                    this.parseLogicalNak(msg);
                    break;
                default:
                    loggError.error("XCC传入的methodId错误，无法识别!jobId:"+msg.getMethodNum());
                    break;
             }
    }

    private void parseLogicalNak(MessageForm msg) {
    }

    private void parseBufferNak(MessageForm msg) {
    }

    private void parseDataPartNak(MessageForm msg) {
    }

    private void parseAck(MessageForm msg) {
        /*获取调用者的类名*/
        String className= new Throwable().getStackTrace()[1].getClassName();
        switch (className){
            case JOBTLC:
                loggerInfo.info("XJD的方法"+className+":已经接收到ACK()"+msg);
                break;
            case  BLOCKMAPTLC:
                loggerInfo.info("XJD的方法"+className+":已经接收到ACK()"+msg);
                break;
        }
    }


    private void parseMethodBusyNak(MessageForm msg) {
    }

    private void parseNak(MessageForm msg) {
    }

    protected  void nak() {

    }


    protected  void dataPartNak() {

    }


    protected  void bufferNak() {

    }


    protected  void methodBusyNak() {

    }

    protected  void logicalNak() {

    }
}
