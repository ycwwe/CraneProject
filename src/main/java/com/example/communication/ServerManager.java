package com.example.communication;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * 
 * ��������������ͻ������ӵ�Socket
 * 
 * @author eternalchu
 *
 */
public class ServerManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(ServerManager.class);
	private String localAddress = null;
	private Socket clientSocket;
	
	
	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	/**
	 * 
	 * ����������ӵ�Socket�û���
	 * 
	 * @author eternalchu
	 *
	 */
	private class SocketSubscriber{
		
		private String clientAddress;
		private int portNum;
		
		public SocketSubscriber(String address, int port) {
			this.clientAddress = address;
			this.portNum = port;
		}
		
		public boolean isMatch(String address, int port) {
			boolean result = false;
			
			try
		      {
		        if (((address.equals(this.clientAddress)) || (address.equals(InetAddress.getByName(this.clientAddress).getHostAddress()))) && (port == this.portNum))
		        {

		          result = true;
		        }
		      }
		      catch (UnknownHostException localUnknownHostException) {}
		      
		      return result;
			
		}
		
		public boolean isMatch(int port) {
			if (port == this.portNum) {
				return true;
			}
			return false;
		}
	}
	/**
	 * 
	 * ����������Socket������
	 * 
	 * @author eternalchu
	 *
	 */
	private class SocketServerThread extends Thread{
		private String localAddress;
		private int localPort;
		private List<SocketSubscriber> subscribers;
		private final Logger logger = LoggerFactory.getLogger(getClass());
		private ServerSocket serverSocket;
		
		public SocketServerThread(String localAddress, List<SocketSubscriber> subscribers, int localPort) {
			this.localAddress = localAddress;
			this.localPort = localPort;
			this.subscribers = subscribers;
		}
		
		public void run() {
			try {
				InetAddress bindAddress = null;
				if (this.localAddress != null) {
					bindAddress = InetAddress.getByName(this.localAddress);
				}
				
				this.serverSocket = new ServerSocket();
				this.serverSocket.setReuseAddress(true);
				this.serverSocket.bind(new InetSocketAddress(bindAddress, this.localPort));
		        this.logger.debug("serverManager: Waiting for client connections on port {}", Integer.valueOf(this.localPort));

		        for (;;) {
		        	clientSocket = this.serverSocket.accept();
//		        	if (!informSubscibersGotSocket(clientSocket)) {
//		                this.logger.info("serverManager: No subscriber waiting on adress '{}'. Closing socket", clientSocket.getInetAddress().getHostAddress());
//		                clientSocket.close();
//					}
		        	break;
		        }
			}
			catch (UnknownHostException exception) {
				informSubscribersGetSocketFailed(exception.toString());
			}
			catch (IOException exception) {
				this.logger.debug("serverManager: Server socket IO exception, trying to close socket");
			}
			finally {
//				stopServerSocketThread();
//				
//				if (this.serverSocket != null) {
//					if (this.serverSocket.isClosed()) {
//			            this.logger.debug("serverManager: Server socket thread on port nr {} closed", Integer.valueOf(this.localPort));
//					}
//					else {
//			            this.logger.warn("serverManager: Failed closing server socket thread on port nr {}", Integer.valueOf(this.localPort));
//					}
//				}
			}
		}
		
		private void stopServerSocketThread() {
			if (this.serverSocket != null) {
				try {
					this.serverSocket.close();
				} catch (IOException e) {
			        this.logger.debug("serverManager: caught exception when stopping server thread", e);
				}
			}
		}

		private void informSubscribersGetSocketFailed(String string) {
			synchronized (this.subscribers) {
				for(int i = 0; i<this.subscribers.size(); i++) {
					if (((SocketSubscriber)this.subscribers.get(i)).isMatch(this.localPort) == true) {
			            this.logger.warn("serverManager: Failed getting socket {}", string);

					}
				}
			}
		}

		private boolean informSubscibersGotSocket(Socket socket) {
			boolean sentSocket = false;
			
			synchronized (this.subscribers) {
				for(int i = 0 ; i < this.subscribers.size(); i++) {
					if (((SocketSubscriber)this.subscribers.get(i)).isMatch(socket.getInetAddress().getHostAddress(), this.localPort) == true) {
						sentSocket = true;
					}
				}
			}
			return sentSocket;
		}
		
		public int getlocalPort() {
			return this.localPort;
		}
		
	}
	
	List<SocketSubscriber> subscribers = new ArrayList<>();
	
	HashMap<Integer, SocketServerThread> threads = new HashMap();

	public String getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}
	/**
	 * 
	 * ��ӿͻ���Socket
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	public Socket startSubscribeSocket(String address, int port) {
		synchronized (this.subscribers) {
			for(int i = 0; i < this.subscribers.size(); i++) {
				if (((SocketSubscriber)this.subscribers.get(i)).isMatch(address, port) == true) {
			          //LOG.warn("serverManager: Already a subscriber on adress '{}', portNr {}. Cannot add new subscriber", address, Integer.valueOf(port));
				}
			}
			
			this.subscribers.add(new SocketSubscriber(address, port));
			
			if (this.threads.get(Integer.valueOf(port)) == null) {
				LOG.debug("serverManager: No thread listening to port {}. Starting new server socket thread", Integer.valueOf(port));
		        
		        SocketServerThread newTread = new SocketServerThread(this.localAddress, this.subscribers, port);
		        
		        newTread.setName("GetSocket-" + port);
		        
		        newTread.start();
		        this.threads.put(Integer.valueOf(port), newTread);
			}
		}
		while(clientSocket == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return clientSocket;
	}
	/**
	 * 
	 * �Ƴ��ͻ���Socket
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	public boolean stopSubscribeSocket(String address, int port) {
		synchronized (this.subscribers) {
			for(int i = 0; i < this.subscribers.size(); i++) {
				 if (((SocketSubscriber)this.subscribers.get(i)).isMatch(address, port) == true) {
					 LOG.debug("serverManager: Removing subscriber on adress '{}', portNr {}", address, Integer.valueOf(port));
			         this.subscribers.remove(i);
			         return true;
				}
			}
		}
		return true;
	}
	/**
	 * 
	 * ֹͣ����Socket�߳�
	 * 
	 */
	public void stop() {
		for(SocketServerThread t : this.threads.values()) {
			LOG.debug("serverManager: Closing server socket thread on port {}", Integer.valueOf(t.getlocalPort()));
		    t.stopServerSocketThread();
		}
	}
}
