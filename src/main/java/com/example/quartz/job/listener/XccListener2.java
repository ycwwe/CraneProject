package com.example.quartz.job.listener;

import com.example.communication.uselesss.ComlinkInterface;
import com.example.quartz.job.filter.InfoMaintenanceClass;
import com.example.quartz.job.filter.XJDMessageFilter;
import com.example.quartz.job.sender.MessageForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/*XCC监听程序*/
public class XccListener2 {
    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private static Logger loggError = LoggerFactory.getLogger("demo_error");
    private Map<String, Integer> addressAndSuffixMap = InfoMaintenanceClass.addressAndSuffixMap;/*记录注册到IOC容器中的comlink对象的IP地址+端口号(key) 和 注册到容器的comlink的后缀（value）*/
    @Autowired
    private XJDMessageFilter xjdMessageFilter;
    /*线程安全的容器ConcurrentLinkedDeque */
    private static List<MessageForm> msgflist = new ArrayList<>();

    public static void setMsgflist(MessageForm messageForm) {
        XccListener2.msgflist.add(messageForm);
    }

    private static List<MessageForm> msgfToRemove = new ArrayList<>();

    public void startListener(ComlinkInterface comlinkInterface) {
        while (comlinkInterface.getMessage() != null) {
           /* for(HashMap.Entry<String, Integer> entry:addressAndSuffixMap.entrySet()){
                int suffix=entry.getValue();
                ComLink comLink = (ComLink) SpringUtil.getBean("comLink"+String.valueOf(suffix));*//*获取xccId对应的并注册到IOC容器中的comLink对象*//*
                if(comLink!=null){
                    msgflist.add(new MessageForm()*//*comLink.getMessage()*//*);
                }*/
            XccListener2.setMsgflist(comlinkInterface.getMessage());
        }

    }

    public void read() {
        while (true) {
            if (!msgflist.isEmpty()) {
                System.out.println("XCClistener2开始读取");
                MessageForm messageForm = msgflist.get(0);
                loggerInfo.info("********************************************************");
                loggerInfo.info("-------------接收到的XCC_ONE的返回信息：" + messageForm + "---------------");
                loggerInfo.info("********************************************************");
                xjdMessageFilter.messageFilter(messageForm);
                msgflist.remove(0);
           /* for (MessageForm messageForm : msgflist) {
                xjdMessageFilter.messageFilter(messageForm);
                msgflist.remove(messageForm);
            }*/
            }
        }

    }

    public XccListener2() {
        System.out.println("XCClistener2被注册，正在执行");
        new Thread(() -> {
            read();
        }).start();

    }
}
