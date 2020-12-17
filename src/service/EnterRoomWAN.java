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
	//EnterRoomWAN��: ��������WANͨ�ų�ʼ��
	
	private BufferedReader reader; // ����BufferedReader����
	private PrintWriter writer; // ����BufferedWriter����
	private Socket socket; // ����Socket����socket
	private final String serverIP="150.158.175.212";	//�Ʒ�����
//	private final String serverIP="10.249.71.210";	//����
//	private final String serverIP="10.250.169.243";		//����
	
	private final int socketNumber=20721;
	private boolean isCancelConnect=false;	//���ҷ�����ȡ������
	private boolean haveCreateMyData=false;
	private GameFrame gameFrame;
	private MyData myData;
	private LinkQueue sendMessageQueue = new LinkQueue();
	private EnterRoomDialog enterRoomDialog;
	private boolean isConnectingServer = false;
	private final int writerSleep = Constant.WRITER_SLEEP;
	
	//���췽�� ���뷿��WANͨ�ų�ʼ��
	public EnterRoomWAN(EnterRoomDialog enterRoomDialog) {
		enterRoomDialog.isConnecting();
		this.enterRoomDialog=enterRoomDialog;
		class ClientEnterRoomThread implements Runnable{   //���ӷ�����߳�
		    @Override  
		    public void run() {
		    	try { // ��׽�쳣
		    		socket=new Socket(serverIP,socketNumber); // ʵ����Socket����
		    		isConnectingServer=true;
		    		new Thread(new ReaderThread()).start(); 
		    		new Thread(new WriterThread()).start();
		    		sendMessage("7,2,"+enterRoomDialog.getRoomNumber());
		    	} catch (Exception e1) {	//���ӷ�����ʧ��
		    		stopConnect();
		    		enterRoomDialog.failConnectServer();
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
	            	Thread.sleep(1);
	            }  
	        }catch(Exception e){  
	        	//e.printStackTrace();
	        	if(isCancelConnect==false) {
	        		TextDialog textDialog = new TextDialog(gameFrame,"��ʾ",300,200,"����������ʧ��!",false);
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
					System.out.println("�ͻ��˶��зǿ�:"+sendMessageQueue.GetHead());
					writer.println(sendMessageQueue.DeQueue());
					System.out.println("�ͻ�����Ϣ�ѷ���");
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
		System.out.println("�ͻ���sendMessage���:"+message);
		sendMessageQueue.EnQueue(message);
	}
	
	//ֹͣ����
	public void stopConnect() {
		try {
			if (socket!= null) {
				socket.close(); // �ر��׽���
			}
			isConnectingServer=false;
			isCancelConnect=true;
		} catch (IOException e2) {}
	}
	
	//ֹͣ����
	public void stopConnect(boolean isCancelConnect) {
		try {
			if (socket!= null) {
				socket.close(); // �ر��׽���
			}
			isConnectingServer=false;
			this.isCancelConnect=isCancelConnect;
		} catch (IOException e2) {}
	}
	
	//����menuFrame���ر�EnterRoomDialog
	public void enterGame() {
		enterRoomDialog.dispose();
		enterRoomDialog.getMenuFrame().invisibleAndStopBGM();
	}
	
	//�Ƿ��Ѿ������Ϸ�����
	public boolean getIsConnectingServer() {
		return isConnectingServer;
	}
	
	//�����յ�����Ϣ
	public void receiveMessageHandle(String readerMessage){
		System.out.println("�ͻ��˽���: "+readerMessage);
		if(readerMessage.equals("null")) return;
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
	        		TextDialog textDialog = new TextDialog(gameFrame,"��ʾ",300,200,"�����ѵ���...",false);
	        		textDialog.addWindowListener(new WindowAdapter() {// ��Ӵ������
	        			@Override
	        			public void windowClosing(WindowEvent e) {// ����ر�ǰ
	        				System.out.println("���뷿�䴰�ڹر�");
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
