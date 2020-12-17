package view;

import javax.swing.JDialog;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import calculate.*;
import service.EnterRoomLAN;
import service.EnterRoomWAN;

public class EnterRoomDialog extends JDialog{
	//CreateRoomDialog��:�������䴰��
	private static final long serialVersionUID = 1L;
	private MenuFrame menuFrame;
	private JButton enterLANButton;
	private JButton enterWANButton;
	private JButton returnButton;
	private JLabel textLabel;
	private JTextField EnterRoomJT=new JTextField("",20);
	private int status=0;	//0:��ʼ 1:�������ӷ��� 2:�����ӷ��� 3:���ڽ������� 4:���ӷ�����ʧ��
	private EnterRoomLAN enterRoomLAN;
	private EnterRoomWAN enterRoomWAN;
	
	public EnterRoomDialog(MenuFrame menuFrame) {
		super(menuFrame,"���뷿��", true);
		
		this.menuFrame=menuFrame;
		setSize(600, 400);
		setLocationRelativeTo(null);
		setLayout(null);
		Container container = getContentPane(); // ����һ������
		container.setBackground (Color.white);

		textLabel=new JLabel("�����뷿���:");
		textLabel.setBounds(100,50,400,70);
		textLabel.setFont(new Font("����", Font.BOLD, 20));
		textLabel.setForeground(Color.BLACK);
		textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(textLabel);
		
		EnterRoomJT.setFont(new Font("΢���ź�",0,18));
		EnterRoomJT.setBounds(100, 150, 400, 40);
		EnterRoomJT.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getRoomNumber().indexOf(".")==-1) {		//������
					switch(status) {
					case 0:
						enterRoomWAN = new EnterRoomWAN(EnterRoomDialog.this);
						break;
					case 1:
						new TextDialog(menuFrame,"��ʾ",300,200,"�������ӷ���...");
						break;
					case 2:
						new TextDialog(menuFrame,"��ʾ",300,200,"�����ӷ���!");
						break;
					case 3:
						new TextDialog(menuFrame,"��ʾ",300,200,"����ͬ������,���Ժ�...");
						break;
					}
				}
				else {			//������
					switch(status) {
					case 0:
						enterRoomLAN = new EnterRoomLAN(EnterRoomDialog.this);
						break;
					case 1:
						new TextDialog(menuFrame,"��ʾ",300,200,"�������ӷ���...");
						break;
					case 2:
						new TextDialog(menuFrame,"��ʾ",300,200,"�����ӷ���!");
						break;
					case 3:
						new TextDialog(menuFrame,"��ʾ",300,200,"����ͬ������,���Ժ�...");
						break;
					}
				}
			}
		});
		container.add(EnterRoomJT);
//		EnterRoomJT.requestFocus();
		
		enterLANButton=new JButton("�������������");
		enterLANButton.setBounds(80,250,180,50);
		enterLANButton.setFont(new Font("����", Font.BOLD, 18));
		enterLANButton.setForeground(Color.black);
		enterLANButton.setContentAreaFilled(false);
		enterLANButton.setFocusPainted(false);
		enterLANButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(status) {
				case 0:
				case 4:
					if(enterRoomWAN!=null) {
						enterRoomWAN.stopConnect();
						enterRoomWAN=null;
					}
					enterRoomLAN = new EnterRoomLAN(EnterRoomDialog.this);
					break;
				case 1:
					new TextDialog(menuFrame,"��ʾ",300,200,"�������ӷ���...");
					break;
				case 2:
					new TextDialog(menuFrame,"��ʾ",300,200,"�����ӷ���!");
					break;
				case 3:
					new TextDialog(menuFrame,"��ʾ",300,200,"����ͬ������,���Ժ�...");
					break;
				}
			}
		});
		container.add(enterLANButton);
		
		enterWANButton=new JButton("�������������");
		enterWANButton.setBounds(340,250,180,50);
		enterWANButton.setFont(new Font("����", Font.BOLD, 18));
		enterWANButton.setForeground(Color.black);
		enterWANButton.setContentAreaFilled(false);
		enterWANButton.setFocusPainted(false);
		enterWANButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(status) {
				case 0:
					if(enterRoomWAN==null) enterRoomWAN = new EnterRoomWAN(EnterRoomDialog.this);
					else enterRoomWAN.sendMessage("7,2,"+getRoomNumber());
					break;
				case 1:
					new TextDialog(menuFrame,"��ʾ",300,200,"����������...");
					break;
				case 2:
					new TextDialog(menuFrame,"��ʾ",300,200,"�����ӷ���!");
					break;
				case 3:
					new TextDialog(menuFrame,"��ʾ",300,200,"����ͬ������,���Ժ�...");
					break;
				case 4:
					new TextDialog(menuFrame,"��ʾ",300,200,"���ӷ�����ʧ��!��������!");
				}
			}
		});
		container.add(enterWANButton);
		
		returnButton=new JButton("����");
		returnButton.setBounds(20,10,100,30);
		returnButton.setFont(new Font("����", Font.BOLD, 18));
		returnButton.setForeground(Color.black);
		returnButton.setContentAreaFilled(false);
		returnButton.setFocusPainted(false);
		returnButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				if(enterRoomLAN!=null) enterRoomLAN.stopConnect();
				if(enterRoomWAN!=null) enterRoomWAN.stopConnect();
				dispose();
			}
		});
		container.add(returnButton);
		
		addWindowListener(new WindowAdapter() {// ��Ӵ������
			@Override
			public void windowClosing(WindowEvent e) {// ����ر�ǰ
				if(enterRoomLAN!=null) enterRoomLAN.stopConnect();
				if(enterRoomWAN!=null) enterRoomWAN.stopConnect();
				dispose();
	        }
			@Override
			public void windowOpened(WindowEvent e) { //�������ʱ�ı����ȡ����
				EnterRoomJT.requestFocus();
			}
	    });
		setResizable(false);
		setVisible(true);
	}
	
	//�������ӷ���
	public void isConnecting() {
		status=1;
		textLabel.setText("<html>����������...<br/>���Ժ�</html>");
	}
	
	//�Ѽ��뷿��
	public void alreadyEnter() {
		status=2;
		textLabel.setText("<html>�ɹ����뷿�䣡<br/>�ȴ�������ʼ��Ϸ...</html>");
	}
	
	//���뷿��ʧ��
	public void failEnter() {
		status=0;
		EnterRoomJT.requestFocus();
		textLabel.setText("<html>���뷿��ʧ�ܣ�<br/>�����������򷿼���Ƿ���ȷ��</html>");
	}
	
	//���ӷ�����ʧ��
	public void failConnectServer() {
		status=4;
		textLabel.setText("<html>���ӷ�����ʧ�ܣ�<br/>����������磡</html>");
	}
	
	//���ڽ�������
	public void receiveData() {
		status=3;
		textLabel.setText("<html>����ͬ��������...</html>");
	}
	
	//�������ķ����
	public String getRoomNumber() {
		return EnterRoomJT.getText();
	}
	
	//���MenuFrame
	public MenuFrame getMenuFrame() {
		return menuFrame;
	}
	
}
