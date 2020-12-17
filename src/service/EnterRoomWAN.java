package service;

import view.EnterRoomDialog;
import view.GameFrame;
import calculate.Constant;
import calculate.LinkQueue;
import calculate.MyData;
import view.TextDialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;

public class EnterRoomWAN {
	//EnterRoomWAN类: 创建房间WAN通信初始化
	
	private BufferedReader reader; // 创建BufferedReader对象
	private PrintWriter writer; // 创建BufferedWriter对象
	private Socket socket; // 创建Socket对象socket
	private final String serverIP="150.158.175.212";	//云服务器
//	private final String serverIP="10.249.71.210";	//宿舍
//	private final String serverIP="10.250.169.243";		//教室
	
	private final int socketNumber=20721;
	private boolean isCancelConnect=false;	//是我方主动取消连接
	private boolean haveCreateMyData=false;
	private GameFrame gameFrame;
	private MyData myData;
	private LinkQueue sendMessageQueue = new LinkQueue();
	private EnterRoomDialog enterRoomDialog;
	private boolean isConnectingServer = false;
	private final int writerSleep = Constant.WRITER_SLEEP;
	
	//构造方法 加入房间WAN通信初始化
	public EnterRoomWAN(EnterRoomDialog enterRoomDialog) {
		enterRoomDialog.isConnecting();
		this.enterRoomDialog=enterRoomDialog;
		class ClientEnterRoomThread implements Runnable{   //连接房间的线程
		    @Override  
		    public void run() {
		    	try { // 捕捉异常
		    		socket=new Socket(serverIP,socketNumber); // 实例化Socket对象
		    		isConnectingServer=true;
		    		new Thread(new ReaderThread()).start(); 
		    		new Thread(new WriterThread()).start();
		    		sendMessage("7,2,"+enterRoomDialog.getRoomNumber());
		    	} catch (Exception e1) {	//连接服务器失败
		    		stopConnect();
		    		enterRoomDialog.failConnectServer();
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
	            	Thread.sleep(1);
	            }  
	        }catch(Exception e){  
	        	//e.printStackTrace();
	        	if(isCancelConnect==false) {
	        		TextDialog textDialog = new TextDialog(gameFrame,"提示",300,200,"服务器连接失败!",false);
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
					System.out.println("客户端队列非空:"+sendMessageQueue.GetHead());
					writer.println(sendMessageQueue.DeQueue());
					System.out.println("客户端信息已发送");
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
		System.out.println("客户端sendMessage入队:"+message);
		sendMessageQueue.EnQueue(message);
	}
	
	//停止连接
	public void stopConnect() {
		try {
			if (socket!= null) {
				socket.close(); // 关闭套接字
			}
			isConnectingServer=false;
			isCancelConnect=true;
		} catch (IOException e2) {}
	}
	
	//停止连接
	public void stopConnect(boolean isCancelConnect) {
		try {
			if (socket!= null) {
				socket.close(); // 关闭套接字
			}
			isConnectingServer=false;
			this.isCancelConnect=isCancelConnect;
		} catch (IOException e2) {}
	}
	
	//隐藏menuFrame，关闭EnterRoomDialog
	public void enterGame() {
		enterRoomDialog.dispose();
		enterRoomDialog.getMenuFrame().invisibleAndStopBGM();
	}
	
	//是否已经连接上服务器
	public boolean getIsConnectingServer() {
		return isConnectingServer;
	}
	
	//处理收到的信息
	public void receiveMessageHandle(String readerMessage){
		System.out.println("客户端接收: "+readerMessage);
		if(readerMessage.equals("null")) return;
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
	        		TextDialog textDialog = new TextDialog(gameFrame,"提示",300,200,"好友已掉线...",false);
	        		textDialog.addWindowListener(new WindowAdapter() {// 添加窗体监听
	        			@Override
	        			public void windowClosing(WindowEvent e) {// 窗体关闭前
	        				System.out.println("加入房间窗口关闭");
	        				enterRoomDialog.dispose();
	        				if(gameFrame!=null) {
	        					gameFrame.dispose();
	        				}
	        				enterRoomDialog.getMenuFrame().visibleAndPlayBGM();
	        			}
	        		});
	        		textDialog.setVisible(true);
	        	}
				break;
			case 3:
				enterRoomDialog.failEnter();
				break;
			case 4:
				enterRoomDialog.alreadyEnter();
				break;
			}
			return;
		}	
		if(haveCreateMyData) {
			myData.codeHandle(readerMessage);
			return;
		}
		if(splitMessges[0].equals("0")&&splitMessges[1].equals("0")) {
			enterRoomDialog.receiveData();
			gameFrame = new GameFrame(enterRoomDialog.getMenuFrame(),3,FileDataHandler.getSex());
			myData = gameFrame.getMyData();
			myData.enterRoomWANMode(this);
			haveCreateMyData=true;
			myData.setPlayerStatus(3);
			sendMessage("0,1");
		}
	}
}
