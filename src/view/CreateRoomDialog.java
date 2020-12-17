package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import calculate.*;
import service.CreateRoomLAN;
import service.CreateRoomWAN;

public class CreateRoomDialog extends JDialog {
	//CreateRoomDialog类:创建房间窗口
	private static final long serialVersionUID = 1L;
	private GameFrame gameFrame;
	private JLabel textLabel;
	private JButton startButton;
	private JButton returnButton;
	private boolean isLANMode;
	private int status=0;	//0:初始 1:房间创建成功 2:对方已加入房间 3:房间创建失败 4:正在传输数据 5:连接服务器失败
	private CreateRoomLAN createRoomLAN;
	private CreateRoomWAN createRoomWAN;
	
	public CreateRoomDialog(GameFrame gameFrame,boolean isLANMode) {
		super(gameFrame,"创建房间", true);
		this.isLANMode=isLANMode;
		this.gameFrame=gameFrame;
		setSize(600, 400);
		setLocationRelativeTo(null);
		setLayout(null);
		Container container = getContentPane(); // 创建一个容器
		container.setBackground (Color.white);
		
		textLabel=new JLabel("创建房间中...");
		textLabel.setBounds(100,50,400,150);
		textLabel.setFont(new Font("宋体", Font.BOLD, 20));
		textLabel.setForeground(Color.BLACK);
		textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(textLabel); // 在容器中添加标签
		
		startButton=new JButton("开始游戏");
		startButton.setBounds(100,250,130,50);
		startButton.setFont(new Font("宋体", Font.BOLD, 18));
		startButton.setForeground(Color.black);
		startButton.setContentAreaFilled(false);
		startButton.setFocusPainted(false);
		startButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(status) {
					case 0:
						new TextDialog(gameFrame,"提示",300,200,"正在创建房间,请稍候...");
						break;
					case 1:
						new TextDialog(gameFrame,"提示",300,200,"正在等待好友加入房间...");
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
						new TextDialog(gameFrame,"提示",300,200,"创建房间失败,请检查网络后重试!");
						break;
					case 4:
						new TextDialog(gameFrame,"提示",300,200,"正在传输数据,请稍候!");
						break;
					case 5:
						new TextDialog(gameFrame,"提示",300,200,"<html>连接服务器失败,<br/>请检查网络后重试!</html>");
						break;
				}
			}
		});
		container.add(startButton);
		
		returnButton=new JButton("返回");
		returnButton.setBounds(370,250,130,50);
		returnButton.setFont(new Font("宋体", Font.BOLD, 18));
		returnButton.setForeground(Color.black);
		returnButton.setContentAreaFilled(false);
		returnButton.setFocusPainted(false);
		returnButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
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
		
		addWindowListener(new WindowAdapter() {// 添加窗体监听
			public void windowClosing(WindowEvent e) {// 窗体关闭前
				if(createRoomLAN!=null) createRoomLAN.stopConnect();
				if(createRoomWAN!=null) createRoomWAN.stopConnect();
				dispose();
	        }
	    });
		setResizable(false);
		setVisible(true);
	}
	
	//房间创建成功
	public void RoomAlreadyCreate(String IP) {
		status=1;
		textLabel.setText("<html>房间创建成功! 正在等待对手连接......<br/>房间号:"+IP+
				"<br/>把房间号告诉你的好友吧!</html>");
	}
	
	//房间创建失败
	public void RoomFailCreate() {
		status=3;
		textLabel.setText("房间创建失败！请检查你的网络连接!");
	}
	
	//玩家已加入房间，等待开始游戏
	public void PlayerEnterRoom() {
		status=2;
		textLabel.setText("好友已连接,立刻开始游戏吧！");
	}
	
	//开始传输数据
	public void SendingMessage() {
		status=4;
		textLabel.setText("传输数据中，请稍候...");
	}
	
	//连接服务器失败
	public void failConnectServer() {
		status=5;
		textLabel.setText("<html>连接服务器失败！<br/>请检查你的网络！</html>");
	}
	
	//获取GameFrame
	public GameFrame getGameFrame() {
		return gameFrame;
	}
	
	//返回isLANMode
	public boolean whetherLANMode() {
		return isLANMode;
	}
}
