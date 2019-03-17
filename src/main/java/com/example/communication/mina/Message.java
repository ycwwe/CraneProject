package com.example.communication.mina;

import com.example.quartz.job.sender.MessageForm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {
    private String string;
    public Message(String string) {
        this.string = string;
    }
    public void setString(String string) {
        this.string = string;
    }
    public String getString() {
        return this.string;
    }

    public  static MessageForm stringToMessage(String in_str) {
        int equalIndex ;
        int commaIndex=0;
        MessageForm out_msgform = new MessageForm();
        Map mapstr = new HashMap();
        final int COUNT = 7;    //头数据总数
        // 解析字段
        for (int index = 1; index <= COUNT; index++) {
            equalIndex = in_str.indexOf('=', commaIndex );
            commaIndex = in_str.indexOf(',', equalIndex );
            String num = in_str.substring(equalIndex + 1, commaIndex);
            switch (index) {
                case 1:
                    out_msgform.setInstanceNum(Integer.parseInt(num));
                    break;
                case 2:
                    out_msgform.setClassNum(Integer.parseInt(num));
                    break;
                case 3:
                    out_msgform.setMethodNum(Integer.parseInt(num));
                    break;
                case 4:
                    out_msgform.setReplyFlag(Integer.parseInt(num));
                    break;
                case 5:
                    out_msgform.setSequenceNum(Integer.parseInt(num));
                    break;
                case 6:
                    out_msgform.setSender(num.substring(1,num.length()-1));
                    break;
                case 7:
                    out_msgform.setReciver(num.substring(1,num.length()-1));
                    break;
                default:
                    break;
            }
        }
        // 解析map
        int braceLeftIndex = in_str.indexOf('{', commaIndex );
        int braceRightIndex = in_str.indexOf('}', braceLeftIndex);
        while (braceLeftIndex < braceRightIndex) {
            equalIndex = in_str.indexOf('=', braceLeftIndex);
            commaIndex = in_str.indexOf(',', braceLeftIndex);
            if (-1 == equalIndex){
                break;
            }
            if (-1 == commaIndex){
                commaIndex = in_str.indexOf('}', braceLeftIndex);
            }
            String key = in_str.substring(braceLeftIndex + 1, equalIndex);
            String value = in_str.substring(equalIndex + 1, commaIndex);
            //cout << "key = " << key << ", value = " << value << "  "<< ++countt << endl;
            mapstr.put(key, value);

            braceLeftIndex = commaIndex + 1;
            //cout << "braceLeftIndex: " << braceLeftIndex << endl;
        }
        out_msgform.setParameter(mapstr);

        //mapstrs::iterator it;
        //int c = 0;
        //for (it = mapstr.begin(); it != mapstr.end(); it++)
        //{
        //	cout << it->first << "=" << it->second
        //		<< "  " << ++c << endl;
        //}
        String className = new Throwable().getStackTrace()[1].getMethodName();
        /*	System.out.println("String2MessageForm: "+className+"//"+out_msgform.getInstanceNum()+" "+out_msgform.getClassNum()+" "+out_msgform.getMethodNum()+" "+out_msgform.getParameter());*/


        //cout << "out_msgform.toString(): " << out_msgform.toString().c_str() << endl;

        return out_msgform;
    }


}
