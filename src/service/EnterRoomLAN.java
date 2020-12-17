package service;

import view.EnterRoomDialog;
import view.GameFrame;
import calculate.LinkQueue;
import calculate.MyData;
import view.TextDialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;

public class EnterRoomLAN {
	//EnterRoomLAN类: 创建房间LAN通信初始化
	
	private BufferedReader reader; // 创建BufferedReader对象
	private PrintWriter writer; // 创建BufferedWriter对象
	private Socket socket; // 创建Socket对象socket
	
	private final int socketNumber=20720;
	private boolean isCancelConnect=false;	//是我方主动取消连接
	private boolean haveCreateMyData=false;
	private GameFrame gameFrame;
	private MyData myData;
	private LinkQueue sendMessageQueue = new LinkQueue();
	private EnterRoomDialog enterRoomDialog;
	
	//构造方法 加入房间LAN通信初始化
	public EnterRoomLAN(EnterRoomDialog enterRoomDialog) {
		enterRoomDialog.isConnecting();
		this.enterRoomDialog=enterRoomDialog;
		class ClientEnterRoomThread implements Runnable{   //连接房间的线程
		    @Override  
		    public void run() {
		    	try { // 捕捉异常
		    		socket=new Socket(enterRoomDialog.getRoomNumber(),socketNumber); // 实例化Socket对象
		    		new Thread(new ReaderThread()).start(); 
		    		new Thread(new WriterThread()).start();
		    		enterRoomDialog.alreadyEnter();
		    	} catch (Exception e1) {	//加入房间失败
		    		stopConnect();
		    		enterRoomDialog.failEnter();
		    	}
		    }
		}
		new Thread(new ClientEnterRoomThread()).start();
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
	            }  
	        }catch(Exception e){  
	        	//e.printStackTrace();
	        	if(isCancelConnect==false) {
	        		stopConnect(false);
	        		TextDialog textDialog = new TextDialog(gameFrame,"提示",300,200,"对手已掉线...",false);
	        		textDialog.addWindowListener(new WindowAdapter() {// 添加窗体监听
	        			@Override
	        			public void windowClosing(WindowEvent e) {// 窗体关闭前
	        				System.out.println("加入房间窗口关闭");
	        				enterRoomDialog.dispose();
	        				if(gameFrame!=null) {
	        					gameFrame.exitOnlineMode();
	        					gameFrame.dispose();
	        				}
	        				enterRoomDialog.getMenuFrame().visibleAndPlayBGM();
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
						Thread.sleep(5);
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
		System.out.println("客户端sendMessage入队:"+message);
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
	
	//隐藏menuFrame，关闭EnterRoomDialog
	public void enterGame() {
		enterRoomDialog.dispose();
		enterRoomDialog.getMenuFrame().invisibleAndStopBGM();
	}
	
	//处理收到的信息
	public void receiveMessageHandle(String readerMessage){
		System.out.println("客户端接收: "+readerMessage);
		if(readerMessage.equals("null")) return;
		if(haveCreateMyData) {
			myData.codeHandle(readerMessage);
		}
		else {
			String splitMessges[] = readerMessage.split(",");
			if(splitMessges[0].equals("0")&&splitMessges[1].equals("0")) {
				enterRoomDialog.receiveData();
				gameFrame = new GameFrame(enterRoomDialog.getMenuFrame(),3,FileDataHandler.getSex());
				myData = gameFrame.getMyData();
				myData.enterRoomLANMode(this);
				haveCreateMyData=true;
				myData.setPlayerStatus(3);
				sendMessage("0,1");
			}
		}
	}
}
