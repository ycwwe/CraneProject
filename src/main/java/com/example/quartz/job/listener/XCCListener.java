package com.example.quartz.job.listener;

import com.alibaba.fastjson.JSON;
import com.example.communication.ComLink;
import com.example.communication.ComlinkInterface;
import com.example.communication.Message;
import com.example.quartz.job.filter.XJDMessageFilter;
import com.example.quartz.job.sender.MessageForm;
import com.example.util.SpringUtil;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/*XCC监听程序*/
public class XCCListener  {
    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private  static Logger loggError = LoggerFactory.getLogger("demo_error");
    @Autowired
    private XJDMessageFilter xjdMessageFilter;
    @Autowired
    @Qualifier("comLink")
    private ComLink comLink;
    @Autowired
    @Qualifier("comLinkTwo")
    private ComLink comLinkTwo;
    /*线程安全的容器ConcurrentLinkedDeque */
    private static ConcurrentLinkedDeque<MessageForm> msgflist= new ConcurrentLinkedDeque<>();
    private static ConcurrentLinkedDeque<MessageForm> msgflistOne= new ConcurrentLinkedDeque<>();
    private static ConcurrentLinkedDeque<MessageForm> msgflistTwo= new ConcurrentLinkedDeque<>();
    private static ConcurrentLinkedDeque<MessageForm> msgfToRemove= new ConcurrentLinkedDeque<>();

    public  XCCListener(){
       /* this.init();*/
       loggerInfo.info("正在初始化XCC_ONE的监听器");
        new Thread(()->{
            try {
                Thread.sleep(30);
                loggerInfo.info("XCCListener已开启新线程监听XCC_ONE");
                this.runListener_ONE();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
               }).start();
        loggerInfo.info("正在初始化XCC_TWO的监听器");
        new Thread(()->{try {
            Thread.sleep(30);
            loggerInfo.info("XCCListener已开启新线程监听XCC_TWO");
            this.runListener_TWO();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }).start();
    }
    private void run(ComlinkInterface comlinkInterface){
        while (true) {
            if(comlinkInterface!=null){
                msgflist.add(comlinkInterface.getMessage());
                if (msgflist!= null) {
                    for(MessageForm messageForm:msgflist){
                        loggerInfo.info("********************************************************");
                        loggerInfo.info("-------------接收到的"+messageForm.getSender()+"的返回信息：" + messageForm + "---------------");
                        loggerInfo.info("********************************************************");
                        xjdMessageFilter.messageFilter(messageForm);
                        msgflist.remove(messageForm);
                    }
                }
            }else{

                loggerInfo.info("-------------XCC_Two未连接 ,无法监听---------------");


            }

        }
    }

    private void runListener_ONE() {
        run(comLink);
    }
    private void runListener_TWO(){
        run(comLinkTwo);
    }
}
