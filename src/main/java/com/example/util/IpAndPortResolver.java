package com.example.util;

import com.example.communication.ComLink;
import com.example.communication.ComLink2;
import com.example.communication.Message;
import com.example.quartz.job.filter.InfoMaintenanceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "xcc")
public class IpAndPortResolver {
    private List<String> address;

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }
    private static int suffix=1;
    public static int getSuffix() {
        return suffix;
    }

    public IpAndPortResolver() {
    }
/*注册配置文件的xcc.adderss，与XCC进行socket连接*/
    public  void registerAddress() {
        ApplicationContext applicationContext=  SpringUtil.getApplicationContext();
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        for (String address : this.getAddress()) {
            String[] array = address .split(":");
            String ip = array[0];
            int port = Integer.valueOf(array[1]);
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ComLink2.class);
            beanDefinitionBuilder.setScope("singleton");
            beanDefinitionBuilder.setLazyInit(true);
            beanDefinitionBuilder.addPropertyValue("clientAddress", ip);
            beanDefinitionBuilder.addPropertyValue("clientPort", port);
            defaultListableBeanFactory.registerBeanDefinition("comLink" + String.valueOf(suffix), beanDefinitionBuilder.getBeanDefinition());
            InfoMaintenanceClass.addressAndSuffixMap.put(address ,suffix);
            if(suffix<10){
                String id="A0"+String.valueOf(suffix);
                InfoMaintenanceClass.addressAndIdMap.put(id,address);
            }else{
                String id="A"+String.valueOf(suffix);
                InfoMaintenanceClass.addressAndIdMap.put(id,address);
            }
            suffix += 1;
        }
    }
            /*ComLink comLinkTest=new ComLink(ip,port);*/
          /*  System.out.println(comLinkTest.send(new Message("@@")));*/
         /*   System.out.println(defaultListableBeanFactory.getBeanDefinition("comLink" + String.valueOf(suffix)));
            ComLink2 comLink=(ComLink2) SpringUtil.getBean("comLink" + String.valueOf(suffix));*/
            /*System.out.println("!!!!!!!!!!!!!!!!!!!!!");
          *//*  *//*
            System.out.println("+++++++++++++++++++++++++");
            System.out.println(comLink.send(new Message("@@")));*/
            public  void registerAddressAndId() {
                ApplicationContext applicationContext=  SpringUtil.getApplicationContext();
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
                for (String address : this.getAddress()) {
                    String[] array = address .split(":");
                    String ip = array[0];
                    int port = Integer.valueOf(array[1]);
                    String id=array[2];
                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ComLink2.class);
                    beanDefinitionBuilder.setScope("singleton");
                    beanDefinitionBuilder.setLazyInit(true);
                    beanDefinitionBuilder.addPropertyValue("clientAddress", ip);
                    beanDefinitionBuilder.addPropertyValue("clientPort", port);
                    defaultListableBeanFactory.registerBeanDefinition("comLink" + String.valueOf(suffix), beanDefinitionBuilder.getBeanDefinition());
                    InfoMaintenanceClass.addressAndSuffixMap.put(address ,suffix);
                    InfoMaintenanceClass.addressAndIdMap.put(id,address);
                    suffix += 1;
                }
            }


}
