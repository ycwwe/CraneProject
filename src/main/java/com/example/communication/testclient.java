package com.example.communication;

import com.alibaba.fastjson.JSON;
import com.example.quartz.job.filter.XJDMessageFilter;
import com.example.quartz.job.listener.XCCListener;
import com.example.quartz.job.sender.MessageForm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class testclient {
	private static XJDMessageFilter filter= new XJDMessageFilter();
	public static void main(String[] args) throws InterruptedException {
		Map map = new HashMap();
	map.put("sender", "XJD");
	map.put("reciever", "XCC");
		map.put("row", "120");
	MessageForm msgf = new MessageForm(3,100,2,map);
//		
//		try {
//			Socket socket = new Socket("192.168.1.34", 8086);
//			System.out.println("������IP:"+socket.getInetAddress()+"�˿�"+socket.getPort()); 
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            while(true) {
////            	String string1 = "this is a client test\n";
////            	System.out.println(string1);
////            	bw.write(string1);
////            	bw.flush();
//            	bw.write(msgf.toString());
//            	System.out.println(msgf.toString());
//            	bw.flush();
//            	String string = br.readLine();
//            	MessageForm msgform = Message.stringToMessage(string);
//            	
//            	try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            }
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//		Socket socket = new Socket("127.0.0.1", 8086);
//		System.out.println("������IP:"+socket.getInetAddress()+"�˿�"+socket.getPort()); 
//        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));  
//        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        while(true) {
//        	String string1 = "this is a client test\n";
//        	System.out.println(string1);
//        	bw.write(string1);
//        	bw.flush();
////        	String string = br.readLine();
////			System.out.println("5");
////        	System.out.println(string);
//        	try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        }
//        
//	} catch (UnknownHostException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	
	ComLink serverLink = new ComLink("192.168.0.101", 8086);
	Message message = new Message(msgf.toString());
	
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	    List<MessageForm> msgfl= new ArrayList<>();
		List<MessageForm> msgfToRemove= new ArrayList<>();
		serverLink.send(message);
		while (true) {
			msgfl.add(serverLink.getMessage());
			if (msgfl != null) {

				System.out.println("********************************************************");
				System.out.println("-------------接收到的XCC的返回信息：" + msgfl + "---------------");
				System.out.println("********************************************************");
				for(MessageForm messageForm:msgfl){
					/*filter.messageFilter(messageForm);*/
					System.out.println(messageForm);
					msgfToRemove.add(messageForm);
				}
			}
			msgfl.removeAll(msgfToRemove);
			msgfToRemove.clear();
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

		}
		/*MessageForm msgreci = serverLink.getMessage();*/

		/*Object o= JSON.parseObject(msgreci.toString(),MessageForm.class);
		if (msgf != null) {
			System.out.println("********************************************************");
			System.out.println("-------------接收到的XCC的返回信息：" + msgf + "---------------");
			System.out.println("********************************************************");
			*//* xjdMessageFilter.messageFilter(this.msgf);*//*
		}*/
		/*System.out.println("client main "+msgreci.getClassNum());*/



	}
		

	
}

