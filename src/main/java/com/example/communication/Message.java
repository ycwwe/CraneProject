package com.example.communication;

import com.example.quartz.job.sender.MessageForm;

import java.util.HashMap;
import java.util.Map;

public class Message {
	private byte[] bytes;
	private String string;
	
	
	
	public Message(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public Message(String string) {
		this.string = string;
	}
	
	public Message(Message messageToCopy) {
		this.bytes = ((byte[])messageToCopy.bytes.clone());
	}
	
	public void setString(String string) {
		this.string = string;
	}
	
	public void setByte(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getString() {
		return this.string;
	}
	
	public byte[] getBytes() {
		return this.bytes;
	}
	
	public String byteToString(byte[] bytes) {
		this.bytes = bytes;
		return this.bytes.toString();
	}
	
	public String byteToString() {
		return this.bytes.toString();
	}
	
	public byte[] stringToByte(String string) {
		this.string = string;
		return this.string.getBytes();
	}
	
	public byte[] stringToByte() {
		return this.string.getBytes();
	}

	public String getClassNo() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMethodNo() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSender(String sender) {
		// TODO Auto-generated method stub
		
	}

	public void setReceiver(String receiver) {
		// TODO Auto-generated method stub
		
	}

	public static MessageForm stringToMessage(String in_str){
		MessageForm out_msgform = new MessageForm();
		Map mapstr = new HashMap();
		final int COUNT = 5;	//数据总数
		int equalIndex = 0;
		int commaIndex = 0;
		// 解析字段
		for(int index = 1; index <= COUNT; index++)
		{
			equalIndex = in_str.indexOf('=', commaIndex + 1);
			commaIndex = in_str.indexOf(',', commaIndex + 1);
			int num = Integer.parseInt(in_str.substring(equalIndex+1, commaIndex));
			//System.out.println(num);
			switch (index)
			{
				case 1:
					out_msgform.setInstanceNum(num);
					break;
				case 2:
					out_msgform.setClassNum(num);
					break;
				case 3:
					out_msgform.setMethodNum(num);
					break;
				case 4:
					out_msgform.setReplyFlag(num);
					break;
				case 5:
					out_msgform.setSequenceNum(num);
					break;
				default:
					break;
			}
		}
		//解析sender reciver
		//解析sender reciver
		int senderNum1 = in_str.indexOf('\'');
		int senderNum2 = in_str.indexOf('\'',senderNum1+1);
		int receiverNum1 = in_str.indexOf('\'',senderNum2+1);
		int receiverNum2 = in_str.indexOf('\'',receiverNum1+1);
		out_msgform.setSender(in_str.substring(senderNum1+1,senderNum2));
		out_msgform.setReciver(in_str.substring(receiverNum1+1,receiverNum2));

		// 解析map
		int braceLeftIndex = in_str.indexOf('{', commaIndex + 1);
		int braceRightIndex = in_str.indexOf('}', braceLeftIndex);
		//cout << "braceLeftIndex: " << braceLeftIndex << ", braceRightIndex: " << braceRightIndex << endl;
		//cout << "sizeof(in_str) = " << sizeof(in_str) << "  in_str.length() = " << in_str.length() << endl;

		int countt = 0;
		while (braceLeftIndex < braceRightIndex)
		{
			equalIndex = in_str.indexOf('=', braceLeftIndex);
			commaIndex = in_str.indexOf(',', braceLeftIndex);
			if (-1 == equalIndex)
				break;
			//cout << "equalIndex: " << equalIndex << ", commaIndex: " << commaIndex << endl;

			if (-1 == commaIndex)
				commaIndex = in_str.indexOf('}', braceLeftIndex);

			String key = in_str.substring(braceLeftIndex + 1, equalIndex);
			String value = in_str.substring(equalIndex + 1, commaIndex);
			//cout << "key = " << key << ", value = " << value << "  "<< ++countt << endl;
			mapstr.put(key,value);

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
        String className= new Throwable().getStackTrace()[1].getMethodName();
	/*	System.out.println("String2MessageForm: "+className+"//"+out_msgform.getInstanceNum()+" "+out_msgform.getClassNum()+" "+out_msgform.getMethodNum()+" "+out_msgform.getParameter());*/


		//cout << "out_msgform.toString(): " << out_msgform.toString().c_str() << endl;

		return out_msgform;
	}



}
