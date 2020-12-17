package service;

import calculate.LinkQueue;
import calculate.MyData;
import view.CreateRoomDialog;
import view.GameFrame;
import view.TextDialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;

public class CreateRoomLAN {
	//CreateRoomLAN类: 创建房间LAN通信初始化
	
	private BufferedReader reader; // 创建BufferedReader对象
	private PrintWriter writer; // 创建BufferedWriter对象
	private ServerSocket server; // 创建ServerSocket对象
	private Socket socket; // 创建Socket对象socket
	
	private final int socketNumber=20720;
	private boolean isCancelConnect=false;	//是我方主动取消连接
	private GameFrame gameFrame;
	private MyData myData;
	private LinkQueue sendMessageQueue = new LinkQueue();
	private CreateRoomDialog createRoomDialog;
	
	//构造方法 创建房间LAN通信初始化
	public CreateRoomLAN(CreateRoomDialog createRoomDialog) {
		this.createRoomDialog=createRoomDialog;
		gameFrame=createRoomDialog.getGameFrame();
		myData=gameFrame.getMyData();
		
		InetAddress ip;
		try { // try语句块捕捉可能出现的异常
			server = new ServerSocket(socketNumber); // 实例化Socket对象
			ip = InetAddress.getLocalHost(); // 实例化对象
			String localip = ip.getHostAddress(); // 获取本IP地址
			createRoomDialog.RoomAlreadyCreate(localip);
			
			class serverAcceptThread implements Runnable{   //接收对手连接的线程
			    @Override  
			    public void run() {
			    	try {
						socket=server.accept();
			            new Thread(new ReaderThread()).start(); 
			            new Thread(new WriterThread()).start(); 
			            myData.createRoomLANMode(CreateRoomLAN.this);
			            myData.setPlayerStatus(1);
						createRoomDialog.PlayerEnterRoom();
			    	} catch (IOException e2) {} 
			    }
			}
			new Thread(new serverAcceptThread()).start();
		}
		catch (Exception e) {	//获取ip地址或者实例socket出现异常
			createRoomDialog.RoomFailCreate();
			stopConnect();
		}
	}
	
	//接收信息的线程
	class ReaderThread extends Thread{
		@Override
		public void run(){
			reader=null;
	        try{  
            	reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            while(true){  
	                //读取客户端数据    
	            	receiveMessageHandle(reader.readLine());
	            	Thread.sleep(1);
	            }  
	        }catch(Exception e){  
	        	//e.printStackTrace();
	        	if(isCancelConnect==false) {
	        		stopConnect(false);
	        		myData.setPlayerStatus(5);
	        		gameFrame.exitOnlineMode();
	        		TextDialog textDialog = new TextDialog(gameFrame,"提示",300,200,"对手已掉线...",false);
	        		textDialog.addWindowListener(new WindowAdapter() {// 添加窗体监听
	        			public void windowClosing(WindowEvent e) {// 窗体关闭前
	        				System.out.println("创建房间窗口关闭");
	        				closeCreateRoomDialog();
	        			}
	        		});
	        		textDialog.setVisible(true);
	        	}
	        }
		}
	}
	
	//发送信息的线程
	class WriterThread extends Thread{
		@Override
		public void run(){
			try {
				writer=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			while(true) {
				if(!sendMessageQueue.QueueEmpty()) {
					writer.println(sendMessageQueue.DeQueue());
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	//发送信息的方法
	public void sendMessage(String message) {
		System.out.println("服务端sendMessage入队:"+message);
		sendMessageQueue.EnQueue(message);
	}
	
	//停止连接
	public void stopConnect() {
		try {
			if(server!=null) {
				server.close();
			}
			if (socket!= null) {
				socket.close(); // 关闭套接字
			}
			isCancelConnect=true;
		} catch (IOException e2) {}
	}
	
	//停止连接
	public void stopConnect(boolean isCancelConnect) {
		try {
			if (socket!= null) {
				socket.close(); // 关闭套接字
			}
			this.isCancelConnect=isCancelConnect;
		} catch (IOException e2) {}
	}
	
	//关闭CreateRoomDialog
	public void closeCreateRoomDialog() {
		createRoomDialog.dispose();
	}
	
	//处理收到的信息
	public void receiveMessageHandle(String readerMessage){
		System.out.println("服务端接收: "+readerMessage);
//		if(readerMessage.equals("null")) return;
		myData.codeHandle(readerMessage);
	}
	
	//返回CreateRoomDialog
	public CreateRoomDialog getCreateRoomDialog() {
		return createRoomDialog;
	}
}
