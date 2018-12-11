package com.example.communication;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import com.alibaba.fastjson.JSON;
import com.example.quartz.job.listener.XccListener2;
import com.example.quartz.job.sender.MessageForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;


public class ComLink2 implements ComlinkInterface, InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(ComLink.class);

    private String address;
    private int port;
    private  String clientAddress;
    private  int clientPort;
    private boolean server = true;
    private Map<Integer, ComLink> instances = new HashMap();
    private String sender;
    private String receiver;
    private volatile Thread readThread;
    private volatile Thread writeThread;
    private volatile Thread connectThread;
    private volatile boolean isStopRequested;
    private volatile boolean disconnecting;
    private volatile boolean writeStopRequested;
    private volatile Socket socket;
    private volatile InputStream in;
    private volatile OutputStream out;
    private final BlockingDeque<Message> sendQueue = new LinkedBlockingDeque();
    private final BlockingDeque<MessageForm> reciQueue = new LinkedBlockingDeque();
    private ServerManager serverManager = null;
    private volatile Socket newSocket;
    private volatile Socket serverSocket;
    private volatile String socketErrorString = null;
    private volatile ComLinkState state = ComLinkState.Disconnected;


    public String getClientAddress() {
        return clientAddress;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public  void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public ComLink2() {
    }

    public ComLink2(String clientAddress, int clientPort) {
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;

        this.connectThread = new Thread(new Runnable() {
            public void run() {
                ComLink2.this.doConnectThreadWork();
            }
        });
        int intCurrentPriority = this.connectThread.getPriority();
        this.connectThread.setPriority(intCurrentPriority + 1);
        this.connectThread.setName("connect-" + this);
        this.connectThread.start();
    }
    protected void doConnectThreadWork() {
        LOG.info("ComLink[{}]: connect thread started.", this);
        this.state = ComLinkState.WaitingOnServer;
        try {
            LOG.info("ComLink[{}]: waiting on connect to the server.", this);
            serverSocket = new Socket(clientAddress, clientPort);
            this.state = ComLinkState.Connected;
            LOG.info("ComLink[{}]: connected to the server.", this.serverSocket);
            this.in = serverSocket.getInputStream();
            this.out = serverSocket.getOutputStream();
            startReadThread();
            startWriteThread();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private BlockingDeque<Message> getSendQueue() {
        return sendQueue;
    }
    private Socket getNewSocket() {
        return newSocket;
    }
    private boolean setNewSocket(Socket newSocket) {
        this.newSocket = newSocket;
        return true;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    private boolean isServer() {
        return server;
    }
    private void setServer(boolean server) {
        this.server = server;
    }
    private void setState(ComLinkState newState) {
        if (newState != this.state) {
            this.state = newState;

            if (newState == ComLinkState.Disconnected) {
                System.out.println("clearAllMessageTraces()");
            }

            System.out.println("StateChanged��"+newState);
        }
    }
    private String getSender() {
        return sender;
    }
    private void setSender(String sender) {
        this.sender = sender;
    }
    private String getReceiver() {
        return receiver;
    }
    private void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public ServerManager getServerManager() {
        return serverManager;
    }
    public void setServerManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }


    public void serverStart() {
        try {
            this.connectAsServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void clientConnect() {

    }
    private boolean connectAsServer() throws IOException {
        setState(ComLinkState.WaitingOnClient);

        Socket oldSocket = this.socket;
        if (oldSocket != null) {
            oldSocket.close();
            oldSocket = null;
        }
        this.socket = null;
        this.newSocket = null;
        this.socketErrorString = null;

        newSocket = this.serverManager.startSubscribeSocket(this.address, this.port);
//		if (!this.serverManager.startSubscribeSocket(this.address, this.port)) {
//			LOG.warn("ComLink[{}]: unable to subscribe to socket connections at port {} from client {} since there is already another ComLink that subscribes to connections from this client.", new Object[] { this, Integer.valueOf(this.port), this.address });
//		    return false;
//		}
        try {
            while (this.state == ComLinkState.WaitingOnClient) {
                if (this.newSocket != null) {
                    this.socket = this.newSocket;

                    this.in = this.socket.getInputStream();
                    this.out = this.socket.getOutputStream();

                    setState(ComLinkState.Connected);
                    LOG.info("ComLink[{}]: connected as server", this);

                    return true;
                }

                if (this.socketErrorString != null) {
                    setState(ComLinkState.Disconnected);
                    return false;
                }
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException exception) {
                    setState(ComLinkState.Disconnected);
                    return false;
                }
            }
        }
        finally {
            this.serverManager.stopSubscribeSocket(address, port);//TODO
        }

        return false;
    }
    public boolean send(Message message) {
        if (this.state == ComLinkState.Connected) {
            this.sendQueue.addLast(message);
            //LOG.debug("ComLink[{}]: sending message : {} method: {}", new Object[] { this, Integer.valueOf(message.getClassNo()), Integer.valueOf(message.getMethodNo()) });
            return true;
        }

        /*LOG.debug("ComLink[{}]: message not sent.: {} method: {}  Link is down ", new Object[] { this, Integer.valueOf(message.getClassNo()), Integer.valueOf(message.getMethodNo()) });*/
        return false;
    }
    public void startReadThread() {
        this.readThread = new Thread(new Runnable() {
            public void run() {
                ComLink2.this.doReadThreadWork();
            }
        });
        int intCurrentPriority = this.readThread.getPriority();
        this.readThread.setPriority(intCurrentPriority + 1);
        this.readThread.setName("read-" + this);
        this.readThread.start();
    }
    public void startWriteThread() {
        this.writeStopRequested = false;

        this.writeThread = new Thread(new Runnable() {
            public void run() {
                ComLink2.this.doWriteThreadWork();
            }
        });
        int intCurrentPriority = this.writeThread.getPriority();
        this.writeThread.setPriority(intCurrentPriority + 1);
        this.writeThread.setName("write-" + this);
        this.writeThread.start();
    }
    private void doReadThreadWork() {
        LOG.debug("ComLink[{}]: read thread started.", this);
//	    if (!connect())
//	    {
//	      LOG.debug("ComLink[{}]: read thread stopped.", this);
//	      return;
//	    }

        while (!this.isStopRequested) {
            try {
                MessageForm msg = readMessage();
                XccListener2.setMsgflist(msg);
               /* reciQueue.addLast(msg);*/
//				if (!this.isStopRequested) {
//					LOG.warn("ComLink[{}]: broken. Trying to reconnect...", this);
//			        disconnect();
//			        this.disconnecting = false;
//			        if (!connect()) {
//			        	LOG.debug("ComLink[{}]: read thread stopped.", this);
//			            return;
//					}
//				}
            } catch (Exception e) {
                this.isStopRequested=true;
                LOG.error("ComLink[{}]: Error reading message", e);
            }

        }
    }
    public MessageForm getMessage() {
        MessageForm msgToRec = null;
        try {
            msgToRec =  reciQueue.takeFirst();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return msgToRec;
    }
    private void disconnect() {
        this.disconnecting = true;
        if (this.state == ComLinkState.WaitingOnClient) {
            setState(ComLinkState.Disconnected);
        }
        if ((this.state == ComLinkState.WaitingOnServer) || (this.state == ComLinkState.Connected)) {
            Socket socketToClose = this.socket;
            if (socketToClose != null) {
                if (this.server) {
                    LOG.debug("ComLink[{}]: closing socket (server) {}:{}", new Object[] { this, socketToClose.getInetAddress(), Integer.valueOf(socketToClose.getLocalPort()) });
                }else {
                    LOG.debug("ComLink[{}]: closing socket (client) {}:{}", new Object[] { this, socketToClose.getInetAddress(), Integer.valueOf(socketToClose.getPort()) });
                }
                try {
                    setState(ComLinkState.Disconnected);
                    if (this.in != null) {
                        this.in.close();
                    }
                    socketToClose.close();
                } catch (IOException ex) {
                    Thread threadToInterrupt;
                    LOG.debug("ComLink[{}]: unexcepted exception in disconnect method", this, ex);
                }finally {
                    Thread threadToInterrupt = this.writeThread;
                    if (threadToInterrupt != null) {
                        this.writeStopRequested = true;
                        threadToInterrupt.interrupt();
                    }
                }
            }
        }
        if (this.state == ComLinkState.Disconnected) {
            Thread threadToInterrupt = this.readThread;
            if (threadToInterrupt != null) {
                threadToInterrupt.interrupt();
            }
        }

    }
    private MessageForm readMessage() throws IOException {
//		byte[] msgBytes = new byte[1024];
//		int length = this.in.read(msgBytes);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String  str= br.readLine();
        Message msg = new Message(str);
        MessageForm msgf=Message.stringToMessage(str);

        return msgf;
    }
    private void doWriteThreadWork() {
        LOG.debug("ComLink[{}]: write thread started.", this);
        Message msgToSend = null;
        try {
            while((!this.isStopRequested) && (!this.writeStopRequested)) {
                msgToSend = (Message)this.sendQueue.takeFirst();
                msgToSend.setSender(this.sender);
                msgToSend.setReceiver(this.receiver);
		        /*
		        if (msgToSend.getReplyFlag() != Message.getReplyFlagIsReply())
		        {
		          if (this.lastSequenceNumber > 2147483637)
		          {
		            this.lastSequenceNumber = (this.useEvenSequenceNumber ? 0 : 1);
		          }
		          int seqNo = this.lastSequenceNumber + 2;
		          this.lastSequenceNumber += 2;
		          msgToSend.setSequenceNo(seqNo);
		        }
		        else
		        {
		          if (msgToSend.getSequenceNo() == 0)
		          {
		            LOG.warn("AALP ComLink[{}]: sending message with zero sequence number class: {} method: {}", new Object[] { this, Integer.valueOf(msgToSend.getClassNo()), Integer.valueOf(msgToSend.getMethodNo()) });
		          }
		          addReplyTrace(msgToSend);
		        }

		        msgToSend.setCurrentDateAndTime();
		        */
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
                bw.write(msgToSend.getString()+"\n");
                bw.flush();
//		        byte[] msgBytes = msgToSend.getBytes();
//		        System.out.println("doWriteThreadWork"+msgBytes);
//		        this.out.write(msgBytes);
//		        System.out.println("doWriteThreadWork   write over");
                msgToSend = null;
            }
        } catch (SocketException ex) {
            List<Message> clearedMessages;
            LOG.debug("ComLink[{}]: unexcepted SocketException in write thread", this, ex);
        }
        catch (Exception ex) {
            List<Message> clearedMessages;
            if ((!this.disconnecting) && (!this.writeStopRequested))
            {
                LOG.warn("ComLink[{}]: unexcepted exception in write thread", this, ex);
            }
            else
            {
                LOG.debug("ComLink[{}]: unexcepted exception in write thread 2", this, ex);
            }
        }
        finally {
            this.writeStopRequested = false;

            List<Message> clearedMessages = new ArrayList();
            this.sendQueue.drainTo(clearedMessages);
            if (clearedMessages.size() > 0)
            {
                LOG.info("ComLink[{}]: write thread died with {} unsent messages in queue. Count includes any ping()/pong() messages.", this, Integer.valueOf(clearedMessages.size()));
            }
            try {
                this.out.close();
            } catch (IOException ex) { LOG.debug("AALP ComLink[{}]: unexcepted exception in write thread 3", this, ex);
            }
            LOG.debug("ComLink[{}]: write thread stopped.", this);
        }
    }
    private boolean connect() {
        this.isStopRequested = false;

        String lastErrorMessage = null;
        boolean connected = false;

        while((!connected) && (!this.isStopRequested)) {
            try {
                this.disconnecting = false;

                if (this.server) {
                    connected = connectAsServer();
                }
                if (connected == true) {
                    startWriteThread();
                }
            } catch (Exception ex) {
                if (!this.disconnecting) {
                    if (((lastErrorMessage == null) && (ex.getMessage() != null)) || ((lastErrorMessage != null) &&
                            (!lastErrorMessage.equals(ex.getMessage())))) {
                        LOG.warn("ComLink[{}]: unable to connect due to '{}'. Will retry periodically. ", this, ex.getMessage());
                        lastErrorMessage = ex.getMessage();
                    }else {
                        LOG.debug("ComLink[{}]: unable to connect due to '{}'. Will retry periodically. ", this, ex.getMessage());
                    }
                }
            }
        }

        if ((!connected) && (!this.isStopRequested) && (!this.disconnecting)) {
            try
            {
                Thread.sleep(10000L);
            }
            catch (InterruptedException ex2)
            {
                if (this.isStopRequested)
                {
                    return false;
                }
            }
        }

        return connected;
    }
    public void stop() {
        this.isStopRequested = true;
        disconnect();
    }

    /*初始化socket连接*/
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet方法被调用");
        this.connectThread = new Thread(new Runnable() {
            public void run() {
                ComLink2.this.doConnectThreadWork();
            }
        });
        int intCurrentPriority = this.connectThread.getPriority();
        this.connectThread.setPriority(intCurrentPriority + 1);
        this.connectThread.setName("connect-" + this);
        this.connectThread.start();
    }
}

