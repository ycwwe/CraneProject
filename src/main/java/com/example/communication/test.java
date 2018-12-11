package com.example.communication;

import com.example.quartz.job.sender.MessageForm;

import java.beans.beancontext.BeanContext;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class test {
	public static void main(String[] args) {
		String address = "host";
		int port = 8086;
		Message message = new Message("this is a test");
		
		ComLink comLink = new ComLink();
		comLink.setAddress(address);
		comLink.setPort(port);
//		comLink.setSender("SERVER");
//		comLink.setReceiver("CLIENT");
		comLink.setServerManager(new ServerManager());
		
		comLink.serverStart();
		comLink.startReadThread();
		comLink.startWriteThread();
			comLink.send(message);
			MessageForm msgreci = comLink.getMessage();
			System.out.println("server main "+msgreci);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
//		try {
//			ServerSocket ss = new ServerSocket(8086);
//            System.out.println("������������"+"������IP:"+ss.getInetAddress()+"�˿�:"+ss.getLocalPort());              
//        	Socket s = ss.accept(); 
//        	System.out.println("�ͻ���IP:"+s.getInetAddress()+"�˿�:"+s.getPort());  
//        	System.out.println("������IP:"+s.getLocalAddress()+"�˿�:"+s.getLocalPort());
//        	while (true) {
//        		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));  
////                String mess = br.readLine();
////                System.out.println(mess);
//                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));  
//                String str = "this is a server test\n";
//                bw.write(str);  
//                bw.flush(); 
//                String string1 = br.readLine();
//                System.out.println(string1);
//                try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	
}
