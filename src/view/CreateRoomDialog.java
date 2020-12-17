package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import calculate.*;
import service.CreateRoomLAN;
import service.CreateRoomWAN;

public class CreateRoomDialog extends JDialog {
	//CreateRoomDialog��:�������䴰��
	private static final long serialVersionUID = 1L;
	private GameFrame gameFrame;
	private JLabel textLabel;
	private JButton startButton;
	private JButton returnButton;
	private boolean isLANMode;
	private int status=0;	//0:��ʼ 1:���䴴���ɹ� 2:�Է��Ѽ��뷿�� 3:���䴴��ʧ�� 4:���ڴ������� 5:���ӷ�����ʧ��
	private CreateRoomLAN createRoomLAN;
	private CreateRoomWAN createRoomWAN;
	
	public CreateRoomDialog(GameFrame gameFrame,boolean isLANMode) {
		super(gameFrame,"��������", true);
		this.isLANMode=isLANMode;
		this.gameFrame=gameFrame;
		setSize(600, 400);
		setLocationRelativeTo(null);
		setLayout(null);
		Container container = getContentPane(); // ����һ������
		container.setBackground (Color.white);
		
		textLabel=new JLabel("����������...");
		textLabel.setBounds(100,50,400,150);
		textLabel.setFont(new Font("����", Font.BOLD, 20));
		textLabel.setForeground(Color.BLACK);
		textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(textLabel); // ����������ӱ�ǩ
		
		startButton=new JButton("��ʼ��Ϸ");
		startButton.setBounds(100,250,130,50);
		startButton.setFont(new Font("����", Font.BOLD, 18));
		startButton.setForeground(Color.black);
		startButton.setContentAreaFilled(false);
		startButton.setFocusPainted(false);
		startButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(status) {
					case 0:
						new TextDialog(gameFrame,"��ʾ",300,200,"���ڴ�������,���Ժ�...");
						break;
					case 1:
						new TextDialog(gameFrame,"��ʾ",300,200,"���ڵȴ����Ѽ��뷿��...");
						break;
					case 2:
						SendingMessage();
						if(isLANMode) {
							createRoomLAN.sendMessage("0,0");
						}
						else {
							createRoomWAN.sendMessage("0,0");
						}
						break;
					case 3:
						new TextDialog(gameFrame,"��ʾ",300,200,"��������ʧ��,�������������!");
						break;
					case 4:
						new TextDialog(gameFrame,"��ʾ",300,200,"���ڴ�������,���Ժ�!");
						break;
					case 5:
						new TextDialog(gameFrame,"��ʾ",300,200,"<html>���ӷ�����ʧ��,<br/>�������������!</html>");
						break;
				}
			}
		});
		container.add(startButton);
		
		returnButton=new JButton("����");
		returnButton.setBounds(370,250,130,50);
		returnButton.setFont(new Font("����", Font.BOLD, 18));
		returnButton.setForeground(Color.black);
		returnButton.setContentAreaFilled(false);
		returnButton.setFocusPainted(false);
		returnButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				if(createRoomLAN!=null) createRoomLAN.stopConnect();
				if(createRoomWAN!=null) createRoomWAN.stopConnect();
				dispose();
			}
		});
		container.add(returnButton);

		if(isLANMode) {
			createRoomLAN = new CreateRoomLAN(this);
		}
		else {
			createRoomWAN = new CreateRoomWAN(this);
		}
		
		addWindowListener(new WindowAdapter() {// ��Ӵ������
			public void windowClosing(WindowEvent e) {// ����ر�ǰ
				if(createRoomLAN!=null) createRoomLAN.stopConnect();
				if(createRoomWAN!=null) createRoomWAN.stopConnect();
				dispose();
	        }
	    });
		setResizable(false);
		setVisible(true);
	}
	
	//���䴴���ɹ�
	public void RoomAlreadyCreate(String IP) {
		status=1;
		textLabel.setText("<html>���䴴���ɹ�! ���ڵȴ���������......<br/>�����:"+IP+
				"<br/>�ѷ���Ÿ�����ĺ��Ѱ�!</html>");
	}
	
	//���䴴��ʧ��
	public void RoomFailCreate() {
		status=3;
		textLabel.setText("���䴴��ʧ�ܣ����������������!");
	}
	
	//����Ѽ��뷿�䣬�ȴ���ʼ��Ϸ
	public void PlayerEnterRoom() {
		status=2;
		textLabel.setText("����������,���̿�ʼ��Ϸ�ɣ�");
	}
	
	//��ʼ��������
	public void SendingMessage() {
		status=4;
		textLabel.setText("���������У����Ժ�...");
	}
	
	//���ӷ�����ʧ��
	public void failConnectServer() {
		status=5;
		textLabel.setText("<html>���ӷ�����ʧ�ܣ�<br/>����������磡</html>");
	}
	
	//��ȡGameFrame
	public GameFrame getGameFrame() {
		return gameFrame;
	}
	
	//����isLANMode
	public boolean whetherLANMode() {
		return isLANMode;
	}
}
