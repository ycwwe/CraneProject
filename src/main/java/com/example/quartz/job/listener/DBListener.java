package com.example.quartz.job.listener;

import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DBListener /*implements Job */ {
    private static Logger loggerInfo = LoggerFactory.getLogger("demo_info");
    private static Logger loggError = LoggerFactory.getLogger("demo_error");
    public static final int PORT = 18002;//监听的端口号

       /* public static void main(String[] args) {
            System.out.println("服务器启动...\n");
            DBListener serverListener = new DBListener();
            serverListener.init();
        }*/

    public void init() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                // 一旦有堵塞, 则表示服务器与客户端获得了连接
                Socket client = serverSocket.accept();
                // 处理这次连接
                new HandlerThread(client);
            }
        } catch (Exception e) {
            System.out.println("服务器异常: " + e.getMessage());
        }
    }

    private class HandlerThread implements Runnable {
        private Queue<Integer> clientInputStrList = new ConcurrentLinkedDeque<>();
        private Socket socket;

        public HandlerThread(Socket client) {
            socket = client;
            new Thread(this).start();
        }

        public void invokeDAO() {
            while (true) {
                if (socket.isClosed() && !clientInputStrList.isEmpty()) {
                    System.out.println("socket连接已经断开:clientInputStrList" + clientInputStrList);
                    clientInputStrList.clear();
                }
            }
        }

        public void run() {
            try {
                // 读取客户端数据
                DataInputStream input = new DataInputStream(socket.getInputStream());
                int clientInputStr = input.read();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
                // 处理客户端数据
                clientInputStrList.add(clientInputStr);
                System.out.println("客户端发过来的内容:" + clientInputStrList);
                input.close();
            } catch (Exception e) {
                System.out.println("服务器 run 异常: " + e.getMessage());
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        socket = null;
                        System.out.println("服务端 finally 异常:" + e.getMessage());
                    }
                }
            } /*invokeDAO();*/
        }
    }

   /* @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        init();
        System.out.println("服务器启动...正在监听端口号：18002\n");

    }*/
}
