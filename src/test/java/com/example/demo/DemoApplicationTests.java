package com.example.demo;


import com.example.communication.ComLink;
import com.example.quartz.job.sender.SendJobToXCC;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


/*@SpringBootTest
@RunWith(SpringRunner.class)*/
public  class  DemoApplicationTests {
/*
    @Before
    public void setUp() throws Exception {
        // 初始化测试用例类中由Mockito的注解标注的所有模拟对象
        MockitoAnnotations.initMocks(this);
    }
    @Mock
    private ComLink comLinkMock;
    @InjectMocks
    private SendJobToXCC sendJobToXCC;
    @Test
    public void test() throws InterruptedException {
        when(comLinkMock.send(any())).thenReturn(true);
        Map map =new HashMap(){{
            put("t","t");
        }
        };
        List<Map> parameters=new ArrayList<Map>(){{
            add(map);
        }};
        sendJobToXCC.sendPickUpChassisJob(parameters);
    }*/


}
// 其他测试类继承MockitoBasedTest


