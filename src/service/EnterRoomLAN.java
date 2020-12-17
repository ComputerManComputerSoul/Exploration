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
	//EnterRoomLAN��: ��������LANͨ�ų�ʼ��
	
	private BufferedReader reader; // ����BufferedReader����
	private PrintWriter writer; // ����BufferedWriter����
	private Socket socket; // ����Socket����socket
	
	private final int socketNumber=20720;
	private boolean isCancelConnect=false;	//���ҷ�����ȡ������
	private boolean haveCreateMyData=false;
	private GameFrame gameFrame;
	private MyData myData;
	private LinkQueue sendMessageQueue = new LinkQueue();
	private EnterRoomDialog enterRoomDialog;
	
	//���췽�� ���뷿��LANͨ�ų�ʼ��
	public EnterRoomLAN(EnterRoomDialog enterRoomDialog) {
		enterRoomDialog.isConnecting();
		this.enterRoomDialog=enterRoomDialog;
		class ClientEnterRoomThread implements Runnable{   //���ӷ�����߳�
		    @Override  
		    public void run() {
		    	try { // ��׽�쳣
		    		socket=new Socket(enterRoomDialog.getRoomNumber(),socketNumber); // ʵ����Socket����
		    		new Thread(new ReaderThread()).start(); 
		    		new Thread(new WriterThread()).start();
		    		enterRoomDialog.alreadyEnter();
		    	} catch (Exception e1) {	//���뷿��ʧ��
		    		stopConnect();
		    		enterRoomDialog.failEnter();
		    	}
		    }
		}
		new Thread(new ClientEnterRoomThread()).start();
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
	            }  
	        }catch(Exception e){  
	        	//e.printStackTrace();
	        	if(isCancelConnect==false) {
	        		stopConnect(false);
	        		TextDialog textDialog = new TextDialog(gameFrame,"��ʾ",300,200,"�����ѵ���...",false);
	        		textDialog.addWindowListener(new WindowAdapter() {// ��Ӵ������
	        			@Override
	        			public void windowClosing(WindowEvent e) {// ����ر�ǰ
	        				System.out.println("���뷿�䴰�ڹر�");
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
		
	//������Ϣ�ķ���
	public void sendMessage(String message) {
		System.out.println("�ͻ���sendMessage���:"+message);
		sendMessageQueue.EnQueue(message);
	}
	
	//ֹͣ����
	public void stopConnect() {
		try {
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
	
	//����menuFrame���ر�EnterRoomDialog
	public void enterGame() {
		enterRoomDialog.dispose();
		enterRoomDialog.getMenuFrame().invisibleAndStopBGM();
	}
	
	//�����յ�����Ϣ
	public void receiveMessageHandle(String readerMessage){
		System.out.println("�ͻ��˽���: "+readerMessage);
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
