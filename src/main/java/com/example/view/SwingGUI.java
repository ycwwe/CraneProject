package com.example.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.Date;

public class SwingGUI extends JFrame {
    private  static Logger loggerInfo = LogManager.getLogger("demo_info");
    private  static Logger loggError = LogManager.getLogger("demo_error");
    private  boolean isModify=true;
    private static  File file;
    private static  RandomAccessFile randomAccessFile;
    private static  final String FILEPATH=System.getProperty("user.dir")+"\\check\\logs\\springboot-log4j2-demo\\demo-info.log";
    private static  final String DIRPATH=System.getProperty("user.dir")+"\\check\\logs\\springboot-log4j2-demo\\";
    private static String string=null;
    private static long pos=0;
    /*private static  final int TEXTAREA_ROWS=1000;
    private static  final int TEXTAREA_COLUMNS=150;*/
    private static  JTextArea textArea=new JTextArea(/*TEXTAREA_ROWS,TEXTAREA_COLUMNS*/);
    private  JTextPane textPane=new JTextPane();
    StyledDocument d=textPane.getStyledDocument();
    SimpleAttributeSet attr = new SimpleAttributeSet();
    private static PrintStream printStream=new PrintStream(System.out){
        public void println(String string){
            textArea.append(string);
            textArea.validate();
        }
       /*将System.out.println()的输出投到textarea*/
    };
    public SwingGUI(){
        start();
    }
    private void start(){

        try {
           /* URL imgURL=new URL("imges/icon.png");
            Image imge = ImageIO.read(imgURL);*/
            /*Image imge =getToolkit().getImage("/icon.png");*/
           /* ImageIcon icon=new ImageIcon("C:\\Users\\Administrator\\Pictures\\icon.png");
            setIconImage(icon.getImage());*/
            setSize(700,800);
            setTitle("XJD显示器");
            setVisible(true);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);/*JFrame.EXIT_ON_CLOSE*/
            JPanel topJpanel= new JPanel();
            topJpanel.setBackground(Color.red);
            JPanel downJpanel= new JPanel();
            downJpanel.setBackground(Color.red);
            add(topJpanel,BorderLayout.NORTH);
            add(downJpanel,BorderLayout.SOUTH);
            textArea.setFont(new Font("显示器",Font.PLAIN,15));
            textArea.setLineWrap(true);/*关闭横滚动条*/
            textArea.setWrapStyleWord(true);
            textArea.setBackground(Color.BLACK);
           /* JScrollPane scrollPane=new JScrollPane(textArea);
            add(scrollPane, BorderLayout.CENTER);*/
            JScrollPane scrollPane2=new JScrollPane(textPane);
            textPane.setSize(350,800);
            textPane.setBackground(Color.BLACK);
            StyleConstants.setForeground(attr, Color.white);
            StyleConstants.setBackground(attr,Color.BLACK);
            StyleConstants.setFontSize(attr,16);
            String date = String.valueOf(new Date());
            d.insertString(d.getLength(),"OUC----HP_LABORATORY  \n"+date+"\n" ,attr);
            add(scrollPane2,BorderLayout.CENTER);
            this.fileRead();
        } catch (Exception e) {
            e.printStackTrace();
        }

      /* for(int i=0;i<100;i++) {
           try {
               Thread.sleep(500);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           textArea.append("message...........................\n");
           textArea.validate();
       }*/
    }
    /*创建WatchService
      得到待检测目录的Path
      将目录登记到变化监测名单中
      执行WatchService的take()方法，直到WatchKey到来。
      得到WatchKey后遍历WatchEvent进行检测
      重置key准备下一个事件，继续等待*/
    private void fileMoniter() throws IOException, InterruptedException {
      /*  System.setOut(printStream);*/
        System.out.println("正在监控demo-info.log..........\n");

        WatchService watchService= FileSystems.getDefault().newWatchService();
        final Path path= Paths.get(DIRPATH);
        final WatchKey watchKey= path.register(watchService,StandardWatchEventKinds.ENTRY_MODIFY,StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE);
        boolean fileNotChanged=true;
        while(fileNotChanged){
            final WatchKey wk=watchService.take();
            for(WatchEvent event:wk.pollEvents()){
                final Path changed=(Path)event.context();
                System.out.println(changed+","+event.kind()+"\n");
                if(changed.endsWith("demo-info.log")){
                    System.out.println("demo-info.log已经被修改了\n");
                    isModify=true;
                    fileRead();
                }
            }
            boolean valid=wk.reset();
            if(!valid){
                System.out.println("Key has been unregisterede\n");
                isModify=false;
            }
        }
    }
    private void fileRead() {
        try {
            file = new File(FILEPATH);
            randomAccessFile = new RandomAccessFile(file, "r");
                while (isModify ) {
                    Thread.sleep(500);
                    randomAccessFile.seek(pos);
                    string = randomAccessFile.readLine();
                    if (string != null) {
                        string = new String(string.getBytes("ISO-8859-1"), "UTF-8");//将读取出来的GBK格式的代码转换成UTF-8
                        textArea.append(string + "\n");
                        d.insertString(d.getLength(),string+"\n",attr);
                        textArea.validate();
                        textArea.setCaretPosition(textArea.getText().length());/*将光标移动到最新行，实现自动滚动到最新添加的信息*/
                        textPane.setCaretPosition(textPane.getStyledDocument().getLength());
                        pos = randomAccessFile.getFilePointer();
                   /* System.out.println("@"+string);
                    System.out.println(pos);*/
                    } else {
                        /*System.out.println("################");*/
                         fileRead();/*使用轮询的方式监控文件*/
                        /*this.fileMoniter();*//*使用watchservice的方式监控文件*/

                    }
                }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    public static void main (String[]args) throws IOException, InterruptedException {
        System.out.println(System.getProperty("user.dir"));
        SwingGUI swingGUI = new SwingGUI();

        /*  s.fileMoniter();*/
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//
//               /* System.setOut(printStream);
//                System.out.println("Sytom message...........................\n");
//                logger.info("logger message...........................\n");*/
//            }
//        });
       /* File file=new File(FILEPATH);
        try {
            RandomAccessFile randomAccessFile =new RandomAccessFile(file,"r");
            String string=null;
            long pos=0;
            string=randomAccessFile.readLine();
            pos=randomAccessFile.getFilePointer();
            System.out.println("@"+string);
            System.out.println(pos);
            randomAccessFile.seek(pos);
            System.out.println("@"+randomAccessFile.readLine());
            while(true){
                Thread.sleep(1000);
                randomAccessFile.seek(pos);
                if(randomAccessFile.readLine()!=null)
                {string=randomAccessFile.readLine();
                System.out.println("@"+string);
                pos=randomAccessFile.getFilePointer();
                System.out.println(pos);}else{
                    System.out.println("################");

                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(file+":文件不存在");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


    }

}
