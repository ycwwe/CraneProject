package com.example.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONPathException;
import com.example.business.blockmap.StackDataType;
import com.example.quartz.job.sender.MessageForm;
import com.example.util.SpringUtil;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


public class BlockMapTLC extends BaseClass{
    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private  static Logger loggError = LoggerFactory.getLogger("demo_error");

    private BloackMapTLCCallBack bloackMapTLCCallBack=null;
    public BlockMapTLC(){
        this.setBloackMapTLCCallBack();
    }
    private void setBloackMapTLCCallBack() {
        this.bloackMapTLCCallBack = SpringUtil.getBean(BlockMapTLCCallBackImpl.class);
    }
    public void messageReceived(MessageForm msg) {
            if (msg.getClassNum() == 105)
            {
                switch (msg.getMethodNum())
                {
                    case 1:
                        getStack(bloackMapTLCCallBack,msg);
                        break;
                    case 2:
                        writeStack(bloackMapTLCCallBack,msg);
                        break;
                    case 3:
                        stackDataReport(bloackMapTLCCallBack,msg);
                        break;
                    case 4:
                        blockMapStatusReport(bloackMapTLCCallBack,msg);
                        break;
                    case 5:
                        stackUtilizationReport(bloackMapTLCCallBack,msg);
                        break;
                    default:
                        loggError.error("XCC传入的methodId的错误，无法识别!jobId:"+msg.getMethodNum());
                        break;

                }
            }else if(msg.getClassNum()==2){
                super.messageReceived(msg);
            }else {
                loggError.error("XCC传入的classId的错误，无法识别!jobId:");
            }
    }



    private void writeStack(BloackMapTLCCallBack bloackMapTLCCallBack,MessageForm msg){
        Map<String,String> map=msg.getParameter();
        /*利用fastjson将String 转换为stakDataType类型的对象*/
        try{
            StackDataType stackData = JSON.parseObject(map.get("stackData"),StackDataType.class);
            bloackMapTLCCallBack.parseWriteStack(map.get("block"),Integer.valueOf(map.get("blockMapRow")),Integer.valueOf(map.get("blockMapBay")),stackData,Integer.valueOf(map.get("force")));
        }
        catch(JSONException js){
            loggError.error("类BlockMapTLC的方法writeStack（）中JSON.parseObject(map.get(\"stackData\"),StackDataType.class)出现错误！！");
            js.printStackTrace();

        }
    }
    private void stackUtilizationReport(BloackMapTLCCallBack bloackMapTLCCallBack, MessageForm msg) {
    }

    private void blockMapStatusReport(BloackMapTLCCallBack bloackMapTLCCallBack, MessageForm msg) {
    }

    private void getStack(BloackMapTLCCallBack bloackMapTLCCallBack, MessageForm msg) {
    }

    private void stackDataReport(BloackMapTLCCallBack bloackMapTLCCallBack, MessageForm msg) {
    }
}
