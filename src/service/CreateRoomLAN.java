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
	//CreateRoomLAN��: ��������LANͨ�ų�ʼ��
	
	private BufferedReader reader; // ����BufferedReader����
	private PrintWriter writer; // ����BufferedWriter����
	private ServerSocket server; // ����ServerSocket����
	private Socket socket; // ����Socket����socket
	
	private final int socketNumber=20720;
	private boolean isCancelConnect=false;	//���ҷ�����ȡ������
	private GameFrame gameFrame;
	private MyData myData;
	private LinkQueue sendMessageQueue = new LinkQueue();
	private CreateRoomDialog createRoomDialog;
	
	//���췽�� ��������LANͨ�ų�ʼ��
	public CreateRoomLAN(CreateRoomDialog createRoomDialog) {
		this.createRoomDialog=createRoomDialog;
		gameFrame=createRoomDialog.getGameFrame();
		myData=gameFrame.getMyData();
		
		InetAddress ip;
		try { // try���鲶׽���ܳ��ֵ��쳣
			server = new ServerSocket(socketNumber); // ʵ����Socket����
			ip = InetAddress.getLocalHost(); // ʵ��������
			String localip = ip.getHostAddress(); // ��ȡ��IP��ַ
			createRoomDialog.RoomAlreadyCreate(localip);
			
			class serverAcceptThread implements Runnable{   //���ն������ӵ��߳�
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
		catch (Exception e) {	//��ȡip��ַ����ʵ��socket�����쳣
			createRoomDialog.RoomFailCreate();
			stopConnect();
		}
	}
	
	//������Ϣ���߳�
	class ReaderThread extends Thread{
		@Override
		public void run(){
			reader=null;
	        try{  
            	reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            while(true){  
	                //��ȡ�ͻ�������    
	            	receiveMessageHandle(reader.readLine());
	            	Thread.sleep(1);
	            }  
	        }catch(Exception e){  
	        	//e.printStackTrace();
	        	if(isCancelConnect==false) {
	        		stopConnect(false);
	        		myData.setPlayerStatus(5);
	        		gameFrame.exitOnlineMode();
	        		TextDialog textDialog = new TextDialog(gameFrame,"��ʾ",300,200,"�����ѵ���...",false);
	        		textDialog.addWindowListener(new WindowAdapter() {// ��Ӵ������
	        			public void windowClosing(WindowEvent e) {// ����ر�ǰ
	        				System.out.println("�������䴰�ڹر�");
	        				closeCreateRoomDialog();
	        			}
	        		});
	        		textDialog.setVisible(true);
	        	}
	        }
		}
	}
	
	//������Ϣ���߳�
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
	
	//������Ϣ�ķ���
	public void sendMessage(String message) {
		System.out.println("�����sendMessage���:"+message);
		sendMessageQueue.EnQueue(message);
	}
	
	//ֹͣ����
	public void stopConnect() {
		try {
			if(server!=null) {
				server.close();
			}
			if (socket!= null) {
				socket.close(); // �ر��׽���
			}
			isCancelConnect=true;
		} catch (IOException e2) {}
	}
	
	//ֹͣ����
	public void stopConnect(boolean isCancelConnect) {
		try {
			if (socket!= null) {
				socket.close(); // �ر��׽���
			}
			this.isCancelConnect=isCancelConnect;
		} catch (IOException e2) {}
	}
	
	//�ر�CreateRoomDialog
	public void closeCreateRoomDialog() {
		createRoomDialog.dispose();
	}
	
	//�����յ�����Ϣ
	public void receiveMessageHandle(String readerMessage){
		System.out.println("����˽���: "+readerMessage);
//		if(readerMessage.equals("null")) return;
		myData.codeHandle(readerMessage);
	}
	
	//����CreateRoomDialog
	public CreateRoomDialog getCreateRoomDialog() {
		return createRoomDialog;
	}
}
