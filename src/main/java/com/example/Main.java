package com.example;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.example.util.Zookeeper;
import com.example.view.SwingGUI;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

@EnableAsync
@SpringBootApplication
@MapperScan("com.example.dao.mapper")
@EnableDubbo
public class Main {
    private static boolean flag;

    /**
     * 判断端口是否被占用
     *
     * @param port
     * @return
     */

    private static boolean isLoclePortUsing(int port) {
        boolean flag = true;
        try {
            flag = isPortUsing("127.0.0.1", port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private static boolean isPortUsing(String host, int port) throws UnknownHostException {
        boolean flag = true;
        InetAddress theAddress = InetAddress.getByName(host);
        /*    System.out.println(theAddress);*/
        try {
            ServerSocket socket = new ServerSocket(port);
            flag = false;
        } catch (IOException e) {
            System.out.println("应用程序需绑定的端口：" + port + "被占用\n" + "可能是程序正在运行！！！，也有可能是别的程序占用，如果是被别的程序占用请释放端口后再运行");
        }
        return flag;
    }

    private static void check() throws IOException {
        //创建lock.txt文件
        String filePath = new File("IDRCallDll").getAbsolutePath().substring(0,
                new File("IDRCallDll").getAbsolutePath().lastIndexOf("\\"));

        File getFile = new File(filePath + "\\" + "lock.txt");
        /* System.out.println(getFile.getPath());*/

        //判断端口是否被占用
        flag = isLoclePortUsing(20520);
        //如果文件存在并且端口被占用则退出
        if (getFile.exists() && flag) {
            System.exit(0);
        }
        try {
            Socket sock = new Socket("127.0.0.1", 20520);// 创建socket,连接20520端口
        } catch (Exception e) {
            System.out.println("端口被占用！");
        }
        final Class<?> clazz = (Class<?>) Main.class;
        final boolean isWindows = System.getProperty("os.name").contains(
                "Windows");
        final List<String> args1 = new ArrayList<String>();
        args1.add(isWindows ? "javaw" : "java");
        /* args1.add("-Xmx" + 128 + "M");*//*指定虚拟机的运行时内存 -Xmx   Java Heap最大值，默认值为物理内存的1/4，最佳设值应该视物理内存大小及计算机内其他内存开销而定；
                                           -Xms   Java Heap初始值，Server端JVM最好将-Xms和-Xmx设为相同值，开发测试机JVM可以保留默认值；
                                            -Xmn   Java Heap Young区大小，不熟悉最好保留默认值；
                                           -Xss   每个线程的Stack大小，不熟悉最好保留默认值；*/
        args1.add("-cp");
        args1.add(System.getProperty("java.class.path"));
        args1.add("-Djava.library.path="
                + System.getProperty("java.library.path"));
        args1.add(clazz.getCanonicalName());

        final ProcessBuilder pb = new ProcessBuilder(args1);
        pb.redirectErrorStream(true);

        try {
            /**
             *操作系统进程的程序
             */
            pb.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        RandomAccessFile r = new RandomAccessFile(
                filePath + "\\" + "lock.txt", "rws");
        FileChannel temp = r.getChannel();
        temp.lock();/*文件上锁*/

    }

    public static void main(String[] args) throws IOException {
        check();
      /*  new Thread(()->{
            SwingGUI swingGUI=new SwingGUI();
        }).start();*/
       /* Zookeeper.getZookeeperInstance();*/
        new Thread(() -> {
            SpringApplication.run(Main.class, args);
        }).start();
    }
}
