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
	//CreateRoomWAN��: ��������WANͨ�ų�ʼ��
	
	private BufferedReader reader; // ����BufferedReader����
	private PrintWriter writer; // ����BufferedWriter����
	private Socket socket; // ����Socket����socket
	private final String serverIP="150.158.175.212";	//�Ʒ�����
//	private final String serverIP="10.249.71.210";	//����
//	private final String serverIP="10.250.169.243";		//����
	
	private final int socketNumber=20721;
	private boolean isCancelConnect=false;	//���ҷ�����ȡ������
	private GameFrame gameFrame;
	private MyData myData;
	private LinkQueue sendMessageQueue = new LinkQueue();
	private CreateRoomDialog createRoomDialog;
	private final int writerSleep = Constant.WRITER_SLEEP;
	
	//���췽�� ��������LANͨ�ų�ʼ��
	public CreateRoomWAN(CreateRoomDialog createRoomDialog) {
		this.createRoomDialog=createRoomDialog;
		gameFrame=createRoomDialog.getGameFrame();
		myData=gameFrame.getMyData();
		
		class serverAcceptThread implements Runnable{   //���ն������ӵ��߳�
		    @Override  
		    public void run() {
		    	try {
		    		socket=new Socket(serverIP,socketNumber); // ʵ����Socket����
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
	        		TextDialog textDialog = new TextDialog(gameFrame,"��ʾ",300,200,"����������ʧ��!",false);
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
					System.out.println("����˶��зǿ�:"+sendMessageQueue.GetHead());
					writer.println(sendMessageQueue.DeQueue());
					System.out.println("�������Ϣ�ѷ���");
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
	
	//������Ϣ�ķ���
	public void sendMessage(String message) {
		System.out.println("�����sendMessage���:"+message);
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
	
	//�ر�CreateRoomDialog
	public void closeCreateRoomDialog() {
		createRoomDialog.dispose();
	}
	
	//����CreateRoomDialog
	public CreateRoomDialog getCreateRoomDialog() {
		return createRoomDialog;
	}
	
	//�����յ�����Ϣ
	public void receiveMessageHandle(String readerMessage){
		System.out.println("����˽���: "+readerMessage);
		String splitMessges[] = readerMessage.split(",");
		if(splitMessges[0].equals("8")) {
			/**
			 * ����λ8:
			 * 8,0:�з��ѵ���
			 * 8,1:�ҷ���������ʧ��
			 * 8,2,�����:�ҷ���������ɹ�
			 * 8,3:�ҷ����뷿��ʧ��
			 * 8,4:�ҷ����뷿��ɹ�
			 * 8,5:�з��Ѽ��뷿��
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
	        		TextDialog textDialog = new TextDialog(gameFrame,"��ʾ",300,200,"�����ѵ���...",false);
	        		textDialog.addWindowListener(new WindowAdapter() {// ��Ӵ������
	        			public void windowClosing(WindowEvent e) {// ����ر�ǰ
	        				System.out.println("�������䴰�ڹر�");
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
