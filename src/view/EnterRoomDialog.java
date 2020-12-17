package view;

import javax.swing.JDialog;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import calculate.*;
import service.EnterRoomLAN;
import service.EnterRoomWAN;

public class EnterRoomDialog extends JDialog{
	//CreateRoomDialog类:创建房间窗口
	private static final long serialVersionUID = 1L;
	private MenuFrame menuFrame;
	private JButton enterLANButton;
	private JButton enterWANButton;
	private JButton returnButton;
	private JLabel textLabel;
	private JTextField EnterRoomJT=new JTextField("",20);
	private int status=0;	//0:初始 1:正在连接房间 2:已连接房间 3:正在接收数据 4:连接服务器失败
	private EnterRoomLAN enterRoomLAN;
	private EnterRoomWAN enterRoomWAN;
	
	public EnterRoomDialog(MenuFrame menuFrame) {
		super(menuFrame,"加入房间", true);
		
		this.menuFrame=menuFrame;
		setSize(600, 400);
		setLocationRelativeTo(null);
		setLayout(null);
		Container container = getContentPane(); // 创建一个容器
		container.setBackground (Color.white);

		textLabel=new JLabel("请输入房间号:");
		textLabel.setBounds(100,50,400,70);
		textLabel.setFont(new Font("宋体", Font.BOLD, 20));
		textLabel.setForeground(Color.BLACK);
		textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(textLabel);
		
		EnterRoomJT.setFont(new Font("微软雅黑",0,18));
		EnterRoomJT.setBounds(100, 150, 400, 40);
		EnterRoomJT.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getRoomNumber().indexOf(".")==-1) {		//广域网
					switch(status) {
					case 0:
						enterRoomWAN = new EnterRoomWAN(EnterRoomDialog.this);
						break;
					case 1:
						new TextDialog(menuFrame,"提示",300,200,"正在连接房间...");
						break;
					case 2:
						new TextDialog(menuFrame,"提示",300,200,"已连接房间!");
						break;
					case 3:
						new TextDialog(menuFrame,"提示",300,200,"正在同步数据,请稍候...");
						break;
					}
				}
				else {			//局域网
					switch(status) {
					case 0:
						enterRoomLAN = new EnterRoomLAN(EnterRoomDialog.this);
						break;
					case 1:
						new TextDialog(menuFrame,"提示",300,200,"正在连接房间...");
						break;
					case 2:
						new TextDialog(menuFrame,"提示",300,200,"已连接房间!");
						break;
					case 3:
						new TextDialog(menuFrame,"提示",300,200,"正在同步数据,请稍候...");
						break;
					}
				}
			}
		});
		container.add(EnterRoomJT);
//		EnterRoomJT.requestFocus();
		
		enterLANButton=new JButton("加入局域网房间");
		enterLANButton.setBounds(80,250,180,50);
		enterLANButton.setFont(new Font("宋体", Font.BOLD, 18));
		enterLANButton.setForeground(Color.black);
		enterLANButton.setContentAreaFilled(false);
		enterLANButton.setFocusPainted(false);
		enterLANButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
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
					new TextDialog(menuFrame,"提示",300,200,"正在连接房间...");
					break;
				case 2:
					new TextDialog(menuFrame,"提示",300,200,"已连接房间!");
					break;
				case 3:
					new TextDialog(menuFrame,"提示",300,200,"正在同步数据,请稍候...");
					break;
				}
			}
		});
		container.add(enterLANButton);
		
		enterWANButton=new JButton("加入广域网房间");
		enterWANButton.setBounds(340,250,180,50);
		enterWANButton.setFont(new Font("宋体", Font.BOLD, 18));
		enterWANButton.setForeground(Color.black);
		enterWANButton.setContentAreaFilled(false);
		enterWANButton.setFocusPainted(false);
		enterWANButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(status) {
				case 0:
					if(enterRoomWAN==null) enterRoomWAN = new EnterRoomWAN(EnterRoomDialog.this);
					else enterRoomWAN.sendMessage("7,2,"+getRoomNumber());
					break;
				case 1:
					new TextDialog(menuFrame,"提示",300,200,"正在连接中...");
					break;
				case 2:
					new TextDialog(menuFrame,"提示",300,200,"已连接房间!");
					break;
				case 3:
					new TextDialog(menuFrame,"提示",300,200,"正在同步数据,请稍候...");
					break;
				case 4:
					new TextDialog(menuFrame,"提示",300,200,"连接服务器失败!请检查网络!");
				}
			}
		});
		container.add(enterWANButton);
		
		returnButton=new JButton("返回");
		returnButton.setBounds(20,10,100,30);
		returnButton.setFont(new Font("宋体", Font.BOLD, 18));
		returnButton.setForeground(Color.black);
		returnButton.setContentAreaFilled(false);
		returnButton.setFocusPainted(false);
		returnButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				if(enterRoomLAN!=null) enterRoomLAN.stopConnect();
				if(enterRoomWAN!=null) enterRoomWAN.stopConnect();
				dispose();
			}
		});
		container.add(returnButton);
		
		addWindowListener(new WindowAdapter() {// 添加窗体监听
			@Override
			public void windowClosing(WindowEvent e) {// 窗体关闭前
				if(enterRoomLAN!=null) enterRoomLAN.stopConnect();
				if(enterRoomWAN!=null) enterRoomWAN.stopConnect();
				dispose();
	        }
			@Override
			public void windowOpened(WindowEvent e) { //弹出框打开时文本框获取焦点
				EnterRoomJT.requestFocus();
			}
	    });
		setResizable(false);
		setVisible(true);
	}
	
	//正在连接房间
	public void isConnecting() {
		status=1;
		textLabel.setText("<html>正在连接中...<br/>请稍候</html>");
	}
	
	//已加入房间
	public void alreadyEnter() {
		status=2;
		textLabel.setText("<html>成功加入房间！<br/>等待房主开始游戏...</html>");
	}
	
	//加入房间失败
	public void failEnter() {
		status=0;
		EnterRoomJT.requestFocus();
		textLabel.setText("<html>加入房间失败！<br/>请检查你的网络或房间号是否正确！</html>");
	}
	
	//连接服务器失败
	public void failConnectServer() {
		status=4;
		textLabel.setText("<html>连接服务器失败！<br/>请检查你的网络！</html>");
	}
	
	//正在接收数据
	public void receiveData() {
		status=3;
		textLabel.setText("<html>正在同步数据中...</html>");
	}
	
	//获得输入的房间号
	public String getRoomNumber() {
		return EnterRoomJT.getText();
	}
	
	//获得MenuFrame
	public MenuFrame getMenuFrame() {
		return menuFrame;
	}
	
}
