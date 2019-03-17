package com.example.quartz.config;

import com.example.business.BlockMapTLC;
import com.example.business.JobTLC;
import com.example.optimizer.OrderOptimizerImpl;
import com.example.quartz.job.filter.JobKindFilter;
import com.example.quartz.job.filter.XJDMessageFilter;
import com.example.quartz.job.listener.XCCListener3;
import com.example.quartz.job.poller.PollingDB;
import com.example.quartz.job.sender.SendJobToXcc2;
import com.example.quartz.job.sender.SendJobToXccByMina;
import com.example.quartz.jobDispatcher.QuartzManage;
import com.example.quartz.startupRunner.ExcelConsumer;
import org.quartz.Scheduler;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author by xup .
 * @Descriptions
 * @Datetime in 2018/1/30 17:07.
 */
@Configuration
public class QuartzConfiguration {
    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private static Logger loggError = LoggerFactory.getLogger("demo_error");
    private static final int PORT = 8086;
    private static final String ADDRESS = "192.168.0.101";

    @Autowired
    private DataSource dataSource;

    //解决Job中注入Spring Bean为null的问题
    @Component("QuartzJobFactory")
    private class QuartzJobFactory extends AdaptableJobFactory {
        @Autowired
        private AutowireCapableBeanFactory capableBeanFactory;

        @Override
        protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
            //调用父类的方法
            Object jobInstance = super.createJobInstance(bundle);
            //进行注入,这属于Spring的技术,不清楚的可以查看Spring的API.
            capableBeanFactory.autowireBean(jobInstance);
            /* System.out.print("@@@@@@已返回jobInstance\n");*/
            return jobInstance;
        }
    }

    //注入scheduler到spring，在下面quartzManege会用到
    @Bean(name = "scheduler")
    public Scheduler scheduler(QuartzJobFactory qjf) throws Exception {
        loggerInfo.info("系统初始化中........");
        /*
        loggError.info("我是测试demoerror的，哈哈哈");
        loggError.error("我ye是测试demoerror的，哈哈哈");*/
        //获取配置属性
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setQuartzProperties(propertiesFactoryBean.getObject());
        /*dataSource.getConnection("system","123456");*/

        factoryBean.setDataSource(dataSource);
        factoryBean.setJobFactory(qjf);
        factoryBean.afterPropertiesSet();
        Scheduler scheduler = factoryBean.getScheduler();
        scheduler.startDelayed(2);/*延迟2秒启动*/
        /*scheduler.start();*/

        return scheduler;
    }

    @Bean(name = "pollingDB")
    public PollingDB pollingDB() {
        return new PollingDB();
    }

    /* @Bean(name="comLink")
     public ComLink comLink(){
         loggerInfo.info("已连接XCC_ONE");
         return new ComLink(XCCIPAndIDEnum.XCC_ONE.getIp(),8086);
     }
     @Bean(name="comLinkTwo")
     public ComLink comLinkTwo(){
         loggerInfo.info("已连接XCC_Two");
         return new ComLink(XCCIPAndIDEnum.XCC_TOW.getIp(),PORT);
     }*/
    @Bean("jobKindFilter")
    public JobKindFilter jobKinsdFilter() {
        return new JobKindFilter();
    }

    /*@Bean("sendJobToXCC")
    public SendJobToXCC sendJobToXCC(){
        return new SendJobToXCC();
    }*/
  /*  @Bean("sendJobToXcc2")
    public SendJobToXcc2 sendJobToXcc2() {
        return new SendJobToXcc2();
    }*/
    @Bean("sendJobToXccByMina")
    public SendJobToXccByMina sendJobToXccByMina() {
        return new SendJobToXccByMina();
    }
    @Bean(value = "quartzManage"/*,destroyMethod = "shutDown"*/)
    public QuartzManage quartzManage() {
        return new QuartzManage();
    }

    @Bean("xjdMessageFilter")
    public XJDMessageFilter xjdMessageFilter() {
        return new XJDMessageFilter();
    }

    /*@Bean(value = "xccListener"*//*,initMethod = "init"*//*)
    public XCCListener xccListener(){
        return new XCCListener();
    }*/
   /* @Bean(value = "xccListener2")
    public XccListener2 xccListener2(){
        return new XccListener2();
    }*/
    @Bean(value = "xccListener3")
    public XCCListener3 xccListener3() {
        return XCCListener3.getXCCListener3Instance();
    }

    /*懒加载*/
    @Bean("jobTLC")
    @Lazy
    public JobTLC jobTLC() {
        return new JobTLC();
    }

    /*懒加载*/
    @Bean("blockMapTLC")
    @Lazy
    public BlockMapTLC blockMapTLC() {
        return new BlockMapTLC();
    }

    @Bean("orderOptimizerImpl")
    public OrderOptimizerImpl orderOptimizerImpl() {
        return new OrderOptimizerImpl();
    }

    /* @Bean("xjdDaoService")
     public XjdDaoServiceImpl xjdDaoService(){
         return new XjdDaoServiceImpl();
     }*/
    @Bean("listenerTaskExecutor")
    public Executor asyncExecuter() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(3);
        //配置最大线程数
        executor.setMaxPoolSize(6);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-XCClistener-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
  /*  @Bean("senderTaskExecutor")
    public Executor asyncSenderExecuter(){
        ThreadPoolTaskExecutor executor =new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(2);
        //配置最大线程数
        executor.setMaxPoolSize(6);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-XJDsender-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return  executor;
    }*/
  @Bean("excelConsumer")
  @Lazy
public ExcelConsumer excelConsumer(){
    return  new ExcelConsumer();
}
}

