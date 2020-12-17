package service;

import calculate.Constant;
import calculate.LinkQueue;
import calculate.MyData;
import view.CreateRoomDialog;
import view.GameFrame;
import view.TextDialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;

public class CreateRoomWAN {
	//CreateRoomWAN类: 创建房间WAN通信初始化
	
	private BufferedReader reader; // 创建BufferedReader对象
	private PrintWriter writer; // 创建BufferedWriter对象
	private Socket socket; // 创建Socket对象socket
	private final String serverIP="150.158.175.212";	//云服务器
//	private final String serverIP="10.249.71.210";	//宿舍
//	private final String serverIP="10.250.169.243";		//教室
	
	private final int socketNumber=20721;
	private boolean isCancelConnect=false;	//是我方主动取消连接
	private GameFrame gameFrame;
	private MyData myData;
	private LinkQueue sendMessageQueue = new LinkQueue();
	private CreateRoomDialog createRoomDialog;
	private final int writerSleep = Constant.WRITER_SLEEP;
	
	//构造方法 创建房间LAN通信初始化
	public CreateRoomWAN(CreateRoomDialog createRoomDialog) {
		this.createRoomDialog=createRoomDialog;
		gameFrame=createRoomDialog.getGameFrame();
		myData=gameFrame.getMyData();
		
		class serverAcceptThread implements Runnable{   //接收对手连接的线程
		    @Override  
		    public void run() {
		    	try {
		    		socket=new Socket(serverIP,socketNumber); // 实例化Socket对象
		            new Thread(new ReaderThread()).start(); 
		            new Thread(new WriterThread()).start(); 
		            sendMessage("7,1");
		    	} catch (IOException e2) {
		    		createRoomDialog.failConnectServer();
					stopConnect();
		    	} 
		    }
		}
		new Thread(new serverAcceptThread()).start();
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
	        		TextDialog textDialog = new TextDialog(gameFrame,"提示",300,200,"服务器连接失败!",false);
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
					System.out.println("服务端队列非空:"+sendMessageQueue.GetHead());
					writer.println(sendMessageQueue.DeQueue());
					System.out.println("服务端信息已发送");
					try {
						Thread.sleep(writerSleep);
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
	
	//返回CreateRoomDialog
	public CreateRoomDialog getCreateRoomDialog() {
		return createRoomDialog;
	}
	
	//处理收到的信息
	public void receiveMessageHandle(String readerMessage){
		System.out.println("服务端接收: "+readerMessage);
		String splitMessges[] = readerMessage.split(",");
		if(splitMessges[0].equals("8")) {
			/**
			 * 带首位8:
			 * 8,0:敌方已掉线
			 * 8,1:我方创建房间失败
			 * 8,2,房间号:我方创建房间成功
			 * 8,3:我方加入房间失败
			 * 8,4:我方加入房间成功
			 * 8,5:敌方已加入房间
			 */
			int intMessges[] = new int[splitMessges.length];
			for(int i=0;i<intMessges.length;i++) {
				intMessges[i]=Integer.parseInt(splitMessges[i]);
			}
			switch(intMessges[1]) {
			case 0:
				if(isCancelConnect==false) {
	        		stopConnect();
	        		myData.setPlayerStatus(5);
	        		gameFrame.exitOnlineMode();
	        		TextDialog textDialog = new TextDialog(gameFrame,"提示",300,200,"好友已掉线...",false);
	        		textDialog.addWindowListener(new WindowAdapter() {// 添加窗体监听
	        			public void windowClosing(WindowEvent e) {// 窗体关闭前
	        				System.out.println("创建房间窗口关闭");
	        				closeCreateRoomDialog();
	        			}
	        		});
	        		textDialog.setVisible(true);
	        	}
				break;
			case 1:
				createRoomDialog.RoomFailCreate();
				break;
			case 2:
				createRoomDialog.RoomAlreadyCreate(""+intMessges[2]);
				break;
			case 5:
	            myData.createRoomWANMode(CreateRoomWAN.this);
	            myData.setPlayerStatus(1);
				createRoomDialog.PlayerEnterRoom();
				break;
			}
			return;
		}
		else {	
			myData.codeHandle(readerMessage);
		}
	}
	
	
}
