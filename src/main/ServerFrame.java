package main;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ServerFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	private final int maxClient=30;
	private final int maxRoom=9;
	private final int maxSeat=maxRoom*2;
	private BufferedReader[] reader = new BufferedReader[maxClient]; // 创建BufferedReader对象
	private PrintWriter[] writer = new PrintWriter[maxClient]; // 创建BufferedWriter对象
	private int[] seatClient = new int[maxSeat]; // 座位对应的客户号
	private int[] clientSeat = new int[maxClient]; // 客户端对应的座位号
	private ServerSocket server; // 创建ServerSocket对象
	private Socket[] socket = new Socket[maxClient]; // 创建Socket对象socket
	private final int socketNumber=20721;
	private JLabel alreadyOpenLabel;
	private JLabel seatStatusLabel;
	private JLabel clientStatusLabel;
	private JButton controlButton;
	private boolean serverStatus=false;	//false：未开启 true:开启
	private Container container = getContentPane();
	private int alreadyConnect=0;
	private final Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
	
	public ServerFrame() {
		setLayout(null);
		
		alreadyOpenLabel = new JLabel("Exploration服务器未开启");
		alreadyOpenLabel.setFont(new Font("宋体", Font.BOLD, 18));
		alreadyOpenLabel.setBounds(50,10,250,30);
		alreadyOpenLabel.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(alreadyOpenLabel);
		
		seatStatusLabel = new JLabel();
		seatStatusLabel.setFont(new Font("微软雅黑", 0, 18));
		seatStatusLabel.setBounds(10,50,480,130);
		seatStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(seatStatusLabel);
		
		clientStatusLabel = new JLabel();
		clientStatusLabel.setFont(new Font("微软雅黑", 0, 18));
		clientStatusLabel.setBounds(10,180,480,180);
		clientStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(clientStatusLabel);
		
		controlButton = new JButton("开启");
		controlButton.setFont(new Font("宋体", Font.BOLD, 18));
		controlButton.setBounds(350,10,100,30);
		controlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!serverStatus) {
					openServer();
				}
				else {
					stopAllConnect();
				}
			}
		});
		container.add(controlButton);
		
		for(int i=0;i<maxSeat;i++) {
			seatClient[i]=-1;
		}
		for(int i=0;i<maxClient;i++) {
			clientSeat[i]=-1;
		}
		refreshLabels();
		
		setBounds((int)screensize.getWidth()/2-250,(int)screensize.getHeight()/2-200, 500, 400);// 设置横纵坐标和宽高
		setTitle("Exploration服务器");// 标题
		setResizable(false);
        addWindowListener(new WindowAdapter() {// 添加窗体监听
            public void windowClosing(WindowEvent e) {// 窗体关闭前
            	System.exit(0);
            }
        });
        setVisible(true);
	}
	
	//向client发送信息
	public void sendMessage(String message,int clientNumber) {
		if(socket[clientNumber]!=null) {
			writer[clientNumber].println(message);
			System.out.println("向"+clientNumber+"发送:"+message);
		}
	}
	
	//关闭服务器
	public void stopAllConnect() {
		try {
			for(int i=0;i<maxClient;i++) {
				stopSingleConnect(i);
			}
			if(server!=null) {
				server.close();
			}
			for(int i=0;i<maxSeat;i++) {
				seatClient[i]=-1;
			}
			for(int i=0;i<maxClient;i++) {
				clientSeat[i]=-1;
			}
			alreadyConnect=0;
			serverStatus=false;
		} catch (IOException e2) {}
		controlButton.setText("开启");
		refreshLabels();
		alreadyOpenLabel.setText("Exploration服务器未开启");
	}
	
	//停止单一socket连接
	public void stopSingleConnect(int clientNumber) {
		try {
			if(socket[clientNumber]!=null) {
				socket[clientNumber].close(); // 关闭套接字
				socket[clientNumber]=null;
				alreadyConnect--;
				if(clientSeat[clientNumber]!=-1) {
					if(seatClient[getSeatMateNumber(clientSeat[clientNumber])]!=-1) {
						sendMessage("8,0",seatClient[getSeatMateNumber(clientSeat[clientNumber])]);
					}
					seatClient[clientSeat[clientNumber]]=-1;
					clientSeat[clientNumber]=-1;
				}
			}
			if(writer[clientNumber]!=null) {
				writer[clientNumber].close(); // 关闭套接字
				writer[clientNumber]=null;
			}
			if(reader[clientNumber]!=null) {
				reader[clientNumber].close(); // 关闭套接字
				reader[clientNumber]=null;
			}
		} catch (IOException e2) {}
		refreshLabels();
	}

	//打开服务器
	public void openServer() {
		alreadyOpenLabel.setText("开启服务器中...");
		try { // try语句块捕捉可能出现的异常
			server = new ServerSocket(socketNumber); // 实例化Socket对象
			class serverAcceptThread implements Runnable{   //接收对手连接的线程
			    @Override  
			    public void run() {
			    	serverStatus=true;
					alreadyOpenLabel.setText("Exploration服务器已开启");
					controlButton.setText("关闭");
					Socket tempsocket;
					int tempClientNumber=-1;
			    	while(true) {
			    		if(alreadyConnect<maxClient) {
			    			try {
					    		tempsocket=server.accept();
					    		alreadyConnect++;
					    		tempClientNumber=getEmptyCilent();
					    		socket[tempClientNumber]=tempsocket;
								writer[tempClientNumber]=new PrintWriter(new OutputStreamWriter(socket[tempClientNumber].getOutputStream()),true);
					            new Thread(new ReaderThread(tempClientNumber)).start(); 
					    	} catch (IOException e2) {} 
			    		}
			    		try {
							Thread.sleep(10);
						} catch (InterruptedException e) {}
			    	}
			    }
			}
			new Thread(new serverAcceptThread()).start();
		}
		catch (Exception e) {	//获取ip地址或者实例socket出现异常
			alreadyOpenLabel.setText("服务器开启失败！请检查你的网络连接!");
			stopAllConnect();
		}
	}
	
	//加入房间申请
	public void enterRoom(int roomNumber,int clientNumber) {
		System.out.println(clientNumber+"申请加入房间"+roomNumber);
		if(clientSeat[clientNumber]!=-1) {
			System.out.println("重复发送加入房间请求");
			return;
		}
		if(roomNumber<0||roomNumber>=maxRoom) {
			sendMessage("8,3",clientNumber);
		}
		else {
			if(seatClient[roomNumber*2+1]==-1&&seatClient[roomNumber*2]!=-1) {
				seatClient[roomNumber*2+1]=clientNumber;
				clientSeat[clientNumber]=roomNumber*2+1;
				sendMessage("8,4",clientNumber);
				sendMessage("8,5",seatClient[roomNumber*2]);
			}
			else {
				sendMessage("8,3",clientNumber);
			}
		}
		refreshLabels();
	}
	
	//创建房间申请
	public void createRoom(int clientNumber) {
		if(clientSeat[clientNumber]!=-1) return;
		for(int i=0;i<maxRoom;i++) {
			if(seatClient[i*2]==-1) {
				seatClient[i*2]=clientNumber;
				clientSeat[clientNumber]=i*2;
				sendMessage("8,2,"+i,clientNumber);
				refreshLabels();
				return;
			}
		}
		sendMessage("8,1",clientNumber);
		return;
	}
	
	//找到client空位置
	public int getEmptyCilent() {
		for(int i=0;i<maxClient;i++) {
			if(socket[i]==null) {
				return i;
			}
		}
		return -1;
	}
		
	//接收信息的线程
	class ReaderThread extends Thread{
		private int clientNumber;
		public ReaderThread(int clientNumber) {
			this.clientNumber=clientNumber;
		}
		@Override
		public void run(){
			System.out.println("ReaderThread:"+clientNumber+"已连接");
			reader[clientNumber]=null;
	        try{  
	            while(true){  
	                //读取客户端数据    
	            	reader[clientNumber]=new BufferedReader(new InputStreamReader(socket[clientNumber].getInputStream()));
	            	receiveMessageHandle(reader[clientNumber].readLine(), clientNumber);
	            	Thread.sleep(1);
	            }  
	        }catch(Exception e){ 
	        	//e.printStackTrace();
	        	stopSingleConnect(clientNumber); 
	        	refreshLabels();
	        	System.out.println("ReaderThread:"+clientNumber+"已掉线");
	        }
		}
	}
	
	//处理获取的信息
	private void receiveMessageHandle(String readerMessage,int clientNumber) {
		/*messageType 读取信息类型: 
		 * 0:服务器与客户端的功能信息
		 * 1:服务器向客户端发出的初始化背包数据
		 * 2:服务器向客户端发出的改变房间数据
		 * 3:改变对方角色数据
		 * 4:改变对方房间数据
		 * 5:改变对方房间中自己的位置
		 * 7:客户端向服务器发送的消息
		 * 8:服务器向客户端发送的信息
		 * 9:聊天
		**/
		System.out.println("从"+clientNumber+"接收信息:"+readerMessage);
		String splitMessges[] = readerMessage.split(",");
		if(Integer.parseInt(splitMessges[0])==7)
		{		
			/***
			 * 带首位7:
			 * 7,1:客户端创建房间请求
			 * 7,2,房间号:客户端加入房间请求
			 */
			switch(Integer.parseInt(splitMessges[1])) {
				case 1:
					createRoom(clientNumber);
					break;
				case 2:
					enterRoom(Integer.parseInt(splitMessges[2]), clientNumber);
					break;
			}
		}
		else {
			sendMessage(readerMessage,seatClient[getSeatMateNumber(clientSeat[clientNumber])]);
		}
	}
	
	public void refreshSeatStatus() {
		String status="<html>seats:<br/>";
		for(int i=0;i<maxSeat;i++) {
			status+=i;
			status+=":";
			status+=seatClient[i];
			status+=",";
			if(i%7==6) status+="<br/>";
		}
		status+="</html>";
		seatStatusLabel.setText(status);
	}
	
	public void refreshClientStatus() {
		String status="<html>clients:<br/>";
		for(int i=0;i<maxClient;i++) {
			status+=i;
			status+=":";
			status+=clientSeat[i];
			status+=",";
			if(i%7==6) status+="<br/>";
		}
		status+="</html>";
		clientStatusLabel.setText(status);
	}
	
	public void refreshLabels() {
		refreshClientStatus();
		refreshSeatStatus();
	}
	
	public int getSeatMateNumber(int number) {
		if(number%2==0) return number+1;
		else return number-1;
	}
	
	public static void main(String[] args) {
		new ServerFrame();// 创建主窗体
	}
}
