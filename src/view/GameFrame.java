package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import calculate.*;
import service.*;

public class GameFrame extends JFrame{
	//GameFrame类: 游戏界面 (继承JFrame类)
	private static final long serialVersionUID = 1L;
	private final int unitLength=Constant.UNIT_LENGTH;
	private final int fullPower=(int)Constant.FULL_POWER;
	private final int roomMaxImage=Constant.ROOM_MAX_IMAGE;
	private MenuFrame menuFrame;
	private int beginX;
	private int beginY;
	private int sex;
	private MyData myData;	//游戏数据
	private MoveThread moveThread;	//移动线程
	private boolean leftLabelShowing;	//左侧信息标签正在显示
	private boolean middleLabelShowing;	//中侧信息标签正在显示
	private BagDialog bagDialog;
	private LinkQueue leftMessageQueue = new LinkQueue();
	private LinkQueue middleMessageQueue = new LinkQueue();
	private Sound sound = new Sound();
	private FriendMoveController friendMoveController;
	private SetChatMessageThread setChatMessageThread;
	private ChatDialog chatDialog;
	
	private JLabel playerLabel;		//玩家标签
	private JLabel friendLabel;		//好友标签
	private JLabel powerLabel;		//体力标签
	private JLabel blackLabel;		//黑色背景标签
	private JLabel roomNameLabel;	//房间名标签
	private JLabel leftMessageLabel;	//左侧信息标签
	private JLabel middleMessageLabel;	//中侧信息标签
	private JLabel chatMessageLabel;	//聊天信息标签
	private JLabel[] labels = new JLabel[roomMaxImage];	
	private JLayeredPane gameLayeredPane;	//层级面板
	
	private ImageIcon playerUpImageStop[] = new ImageIcon[2];
	private ImageIcon playerUpImageRun[] = new ImageIcon[2];
	private ImageIcon playerDownImageStop[] = new ImageIcon[2];
	private ImageIcon playerDownImageRun[] = new ImageIcon[2];
	private ImageIcon playerRightImageStop[] = new ImageIcon[2];
	private ImageIcon playerRightImageRun[] = new ImageIcon[2];
	private ImageIcon playerLeftImageStop[] = new ImageIcon[2];
	private ImageIcon playerLeftImageRun[] = new ImageIcon[2];
	
	//构造函数:生成游戏界面
	public GameFrame(MenuFrame menuFrame,int playerStatus,int sex) {
		this.menuFrame=menuFrame;
		this.sex=sex;
		myData=new MyData(this,playerStatus);
		moveThread=new MoveThread(this);
		moveThread.start();
		Room room=myData.getRoom();
		beginX=room.getBeginX();
		beginY=room.getBeginY();
		
		setLayout(null);
		setSize(1500, 830);
	    setLocationRelativeTo(null);
		
	    gameLayeredPane= new JLayeredPane();
		gameLayeredPane.setLayout(null);
	    gameLayeredPane.setSize(1500, 830);
		setContentPane(gameLayeredPane);
		
		KeyListener keyListener = new GameKeyListener(this);
		addKeyListener(keyListener);
		
		playerUpImageStop[0] = new ImageIcon("image/Boy/actor_u.png");
		playerUpImageStop[0].setImage(playerUpImageStop[0].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerUpImageRun[0] = new ImageIcon("image/Boy/actor_u.gif");
		playerUpImageRun[0].setImage(playerUpImageRun[0].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerDownImageStop[0] = new ImageIcon("image/Boy/actor_d.png");
		playerDownImageStop[0].setImage(playerDownImageStop[0].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerDownImageRun[0] = new ImageIcon("image/Boy/actor_d.gif");
		playerDownImageRun[0].setImage(playerDownImageRun[0].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerRightImageStop[0] = new ImageIcon("image/Boy/actor_r.png");
		playerRightImageStop[0].setImage(playerRightImageStop[0].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerRightImageRun[0] = new ImageIcon("image/Boy/actor_r.gif");
		playerRightImageRun[0].setImage(playerRightImageRun[0].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerLeftImageStop[0] = new ImageIcon("image/Boy/actor_l.png");
		playerLeftImageStop[0].setImage(playerLeftImageStop[0].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerLeftImageRun[0] = new ImageIcon("image/Boy/actor_l.gif");
		playerLeftImageRun[0].setImage(playerLeftImageRun[0].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		
		playerUpImageStop[1] = new ImageIcon("image/Girl/actor_u.png");
		playerUpImageStop[1].setImage(playerUpImageStop[1].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerUpImageRun[1] = new ImageIcon("image/Girl/actor_u.gif");
		playerUpImageRun[1].setImage(playerUpImageRun[1].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerDownImageStop[1] = new ImageIcon("image/Girl/actor_d.png");
		playerDownImageStop[1].setImage(playerDownImageStop[1].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerDownImageRun[1] = new ImageIcon("image/Girl/actor_d.gif");
		playerDownImageRun[1].setImage(playerDownImageRun[1].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerRightImageStop[1] = new ImageIcon("image/Girl/actor_r.png");
		playerRightImageStop[1].setImage(playerRightImageStop[1].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerRightImageRun[1] = new ImageIcon("image/Girl/actor_r.gif");
		playerRightImageRun[1].setImage(playerRightImageRun[1].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerLeftImageStop[1] = new ImageIcon("image/Girl/actor_l.png");
		playerLeftImageStop[1].setImage(playerLeftImageStop[1].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		playerLeftImageRun[1] = new ImageIcon("image/Girl/actor_l.gif");
		playerLeftImageRun[1].setImage(playerLeftImageRun[1].getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
		
		playerLabel=new JLabel(playerUpImageStop[sex]);
		playerLabel.setBounds(unitLength*myData.getX()+beginX,unitLength*myData.getY()+beginY,unitLength,unitLength);
		
		friendLabel=new JLabel(playerUpImageStop[1-sex]);
		friendLabel.setBounds(unitLength*myData.getX()+beginX,unitLength*myData.getY()+beginY,unitLength,unitLength);
		
		powerLabel=new JLabel();
		powerLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
		powerLabel.setForeground(Color.white);
		powerLabel.setBounds(8,5,135,30);
		powerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		roomNameLabel=new JLabel();
		roomNameLabel.setFont(new Font("Times New Roman", Font.BOLD, 23));
		roomNameLabel.setForeground(Color.white);
		roomNameLabel.setBounds(1385,10,100,30);
		roomNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		leftMessageLabel=new JLabel();
		leftMessageLabel.setOpaque(true);  
		leftMessageLabel.setBackground(Color.white);
		leftMessageLabel.setFont(new Font("宋体", Font.BOLD, 18));
		leftMessageLabel.setForeground(Color.black);
		leftMessageLabel.setBounds(5,300,250,150);
		leftMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		middleMessageLabel=new JLabel();
		middleMessageLabel.setOpaque(true);  
		middleMessageLabel.setBackground(Color.white);
		middleMessageLabel.setFont(new Font("宋体", Font.BOLD, 18));
		middleMessageLabel.setForeground(Color.black);
		middleMessageLabel.setBounds(500,250,500,300);
		middleMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		chatMessageLabel=new JLabel();
		chatMessageLabel.setOpaque(true);  
		chatMessageLabel.setBackground(Color.white);
		chatMessageLabel.setFont(new Font("宋体", Font.BOLD, 18));
		chatMessageLabel.setForeground(Color.black);
		chatMessageLabel.setBounds(5,680,250,100);
		chatMessageLabel.setHorizontalAlignment(SwingConstants.LEFT);
		chatMessageLabel.setVerticalAlignment(SwingConstants.CENTER);
		
		ImageIcon blackIcon = new ImageIcon("image/GameFrame/black.png");
		blackIcon.setImage(blackIcon.getImage().getScaledInstance(1500,830,Image.SCALE_DEFAULT));
		blackLabel=new JLabel(blackIcon);
		blackLabel.setBounds(0,0,1500,830);
		
		addMouseListener(new MouseAdapter(){  //匿名内部类，鼠标事件
            public void mouseClicked(MouseEvent e){   //鼠标完成点击事件
                if(e.getButton() == MouseEvent.BUTTON1){ //e.getButton就会返回点鼠标的那个键，左键还是右健，3代表右键
//                	System.out.println("点击位置: "+e.getX()+","+e.getY());
                	int x=e.getX();
                	int y=e.getY();
                	moveThread.pathSearch((x-beginX)/unitLength,(y-beginY-30)/unitLength);
                }
            }
        });
		
		ImageIcon icon = new ImageIcon("image/GameFrame/icon.png");	//窗口图标
        setIconImage(icon.getImage());
		setTitle("Exploratiaon");	// 标题
		setBackground(Color.BLACK);
		setResizable(false);
        addWindowListener(new WindowAdapter() {// 添加窗体监听
            public void windowClosing(WindowEvent e) {// 窗体关闭前
            	System.exit(0);
            }
        });	
        
	}
	
	//加载房间界面组件
	public void loadComponents() {
		Room room=myData.getRoom();
		room.initialItemImage();
		gameLayeredPane.removeAll();
		gameLayeredPane.repaint();
		
		leftLabelShowing=false;
		middleLabelShowing=false;
		int[] bounds=new int[6];
		ImageIcon icon;
		for(int i=0;i<roomMaxImage;i++) {
			if(room.imageIsNull(i)) continue;
			bounds=room.getBounds(i);
			icon = new ImageIcon("image/"+room.getFileName(i));
			icon.setImage(icon.getImage().getScaledInstance(bounds[0],bounds[1],Image.SCALE_DEFAULT));
			labels[i]=new JLabel(icon);
			labels[i].setBounds(bounds[2],bounds[3],bounds[4],bounds[5]);
			gameLayeredPane.add(labels[i],new Integer(room.getLayer(i)));
		}
		
		refreshPower();
		refreshRoomName();
		
		gameLayeredPane.add(blackLabel,new Integer(100));
		gameLayeredPane.add(powerLabel,new Integer(1000));
		gameLayeredPane.add(roomNameLabel,new Integer(1000));
		gameLayeredPane.add(playerLabel,new Integer(500));
		gameLayeredPane.add(friendLabel,new Integer(50));
		gameLayeredPane.add(leftMessageLabel,new Integer(50));
		gameLayeredPane.add(middleMessageLabel,new Integer(50));
		gameLayeredPane.add(chatMessageLabel,new Integer(50));
		
		gameLayeredPane.revalidate();
	}
	
	//初始化gameFrame界面
	public void initGameFrame() {
		refreshStopLocation(myData.getDirection());
		playerLabel.setLocation(myData.getX()*unitLength+beginX,myData.getY()*unitLength+beginY);
		loadComponents();
	}
	
	//刷新界面中角色位置
	public void refreshRunLocation(double doubleX,double doubleY,int direction){
		switch(direction) {
			case 0:
				playerLabel.setIcon(playerUpImageRun[sex]);
				break;
			case 1:
				playerLabel.setIcon(playerDownImageRun[sex]);
				break;
			case 2:
				playerLabel.setIcon(playerLeftImageRun[sex]);
				break;
			case 3:
				playerLabel.setIcon(playerRightImageRun[sex]);
				break;
		}
		playerLabel.setLocation((int)(doubleX*unitLength)+beginX,(int)(doubleY*unitLength)+beginY);
	}
	
	//界面中角色方向(静止)
	public void refreshStopLocation(int direction){
		switch(direction) {
			case 0:
				playerLabel.setIcon(playerUpImageStop[sex]);
				break;
			case 1:
				playerLabel.setIcon(playerDownImageStop[sex]);
				break;
			case 2:
				playerLabel.setIcon(playerLeftImageStop[sex]);
				break;
			case 3:
				playerLabel.setIcon(playerRightImageStop[sex]);
				break;
		}
	}
	
	//返回moveThread
	public MoveThread getMoveThread() {
		return moveThread;
	}
	
	//返回myData
	public MyData getMyData() {
		return myData;
	}
	
	//刷新体力值
	public void refreshPower() {
		powerLabel.setText("体力 "+(int)myData.getPower()+" / "+fullPower);
	}
	
	//刷新房间名
	public void refreshRoomName() {
		roomNameLabel.setText(myData.getRoom().getRoomName());
	}
	
	//左侧提示信息
	public void showLeftMessage(String message) {
		if(message.length()>13) {
			message="<html>"+message.substring(0, 13)+"<br/>"+message.substring(13)+"</html>";
		}
		leftMessageQueue.EnQueue(message);
		if(!leftLabelShowing) {
			leftLabelShowing=true;
			Runnable leftMessageThread = () -> {
				do{
					leftMessageLabel.setText(leftMessageQueue.DeQueue());
					gameLayeredPane.setLayer(leftMessageLabel,new Integer(1000));
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					gameLayeredPane.setLayer(leftMessageLabel,new Integer(50));
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//System.out.println("队列size:"+leftMessageQueue.size);
				}while(!leftMessageQueue.QueueEmpty());
				leftLabelShowing=false;
		    };
		    Thread leftMessageLine = new Thread(leftMessageThread);
		    leftMessageLine.start();
		}
	}
	
	//中侧提示信息
	public void showMiddleMessage(String message) {
		int lineLength=20;
		String tempMessage=message;
		String resultMessage="<html>";
		for(int totalLength = tempMessage.length();totalLength>lineLength;totalLength-=lineLength) {
			resultMessage+=tempMessage.substring(0, lineLength);
			tempMessage=tempMessage.substring(lineLength);
			resultMessage+="<br/>";
		}
		resultMessage+=tempMessage;
		resultMessage+= "<br/><br/>(点击空格以继续)";
		resultMessage+="</html>";
		middleMessageQueue.EnQueue(resultMessage);
		if(!middleLabelShowing) {
			middleLabelShowing=true;
			middleMessageLabel.setText(middleMessageQueue.DeQueue());
			gameLayeredPane.setLayer(middleMessageLabel,new Integer(1000));
		}
	}
	
	//中侧信息翻页
	public void turnMiddleMessage() {
		if(middleLabelShowing) {
			if(middleMessageQueue.QueueEmpty()) {
				gameLayeredPane.setLayer(middleMessageLabel,new Integer(50));
				middleLabelShowing=false;
			}
			else {
				middleMessageLabel.setText(middleMessageQueue.DeQueue());
			}
		}
	}

	//询问是否退出游戏
	public void whetherExit() {
		JDialog exitDialog = new JDialog(this,"提示",true);
		exitDialog.setLayout(null);
		exitDialog.setSize(400, 180);
		exitDialog.setLocationRelativeTo(null);
		Container container = exitDialog.getContentPane(); // 创建一个容器
		
		JLabel exitLabel=new JLabel("是否退出游戏?");
		exitLabel.setFont(new Font("宋体", Font.BOLD, 20));
		exitLabel.setForeground(Color.BLACK);
		exitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		exitLabel.setBounds(95,20,200,40);
		container.add(exitLabel); // 在容器中添加标签
		
		JButton exitButton = new JButton("退出");
		exitButton.setFont(new Font("宋体", Font.BOLD, 18));
		exitButton.setForeground(Color.BLACK);
		exitButton.setContentAreaFilled(false);
		exitButton.setFocusPainted(false);
		exitButton.setBounds(25, 100, 100, 30);
		exitButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		container.add(exitButton);
		
		JButton menuButton = new JButton("主菜单");
		menuButton.setFont(new Font("宋体", Font.BOLD, 18));
		menuButton.setForeground(Color.BLACK);
		menuButton.setContentAreaFilled(false);
		menuButton.setFocusPainted(false);
		menuButton.setBounds(145, 100, 100, 30);
		menuButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.stopPlay();
				dispose();
				menuFrame.visibleAndPlayBGM();
			}
		});
		container.add(menuButton);
		
		JButton cancelButton = new JButton("取消");
		cancelButton.setFont(new Font("宋体", Font.BOLD, 18));
		cancelButton.setForeground(Color.BLACK);
		cancelButton.setContentAreaFilled(false);
		cancelButton.setFocusPainted(false);
		cancelButton.setBounds(265, 100, 100, 30);
		cancelButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				exitDialog.dispose();
			}
		});
		container.add(cancelButton);
		
		container.setBackground (Color.white);
		exitDialog.setResizable(false);
		exitDialog.setVisible(true);
	}
	
	//改变音量
	public void changeVolume() {
		JDialog exitDialog = new JDialog(this,"音量",true);
		exitDialog.setLayout(null);
		exitDialog.setSize(320,200);
		exitDialog.setLocationRelativeTo(null);
		Container container = exitDialog.getContentPane(); // 创建一个容器
		
		String text="<html>主音量<br/><br/>&#12288音乐<br/><br/>&#12288音效</html>";
		JLabel volumeLabel=new JLabel(text);
		volumeLabel.setFont(new Font("宋体",Font.BOLD, 20));
		volumeLabel.setForeground(Color.BLACK);
		volumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		volumeLabel.setBounds(5,5,73,130);
		container.add(volumeLabel);
		
		int volumes[]=FileDataHandler.getVolumes();
		JSlider masterVolumeSlider=getSlider(volumes[0]);
		masterVolumeSlider.setBounds(80, 10,200, 40);
		masterVolumeSlider.addChangeListener(new ChangeListener() {
	            @Override
	            public void stateChanged(ChangeEvent e) {
	            	volumes[0]=masterVolumeSlider.getValue();
	            	sound.temporaryChangeVolumn(((float)volumes[0]*volumes[1])/10000);
	            }
	    });
		container.add(masterVolumeSlider);
        
		JSlider bgmVolumeSlider=getSlider(volumes[1]);
		bgmVolumeSlider.setBounds(80, 60, 200, 40);
		bgmVolumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            	volumes[1]=bgmVolumeSlider.getValue();
            	sound.temporaryChangeVolumn(((float)volumes[0]*volumes[1])/10000);
            }
		});
		container.add(bgmVolumeSlider);
		
		JSlider soundEffectVolumnVolumeSlider=getSlider(volumes[2]);
		soundEffectVolumnVolumeSlider.setBounds(80, 110, 200, 40);
		soundEffectVolumnVolumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            	volumes[2]=soundEffectVolumnVolumeSlider.getValue();
            }
		});
		container.add(soundEffectVolumnVolumeSlider);
		
		container.setBackground (Color.white);
		exitDialog.setResizable(false);
		exitDialog.addWindowListener(new WindowAdapter() {// 添加窗体监听
            public void windowClosing(WindowEvent e) {// 窗体关闭前
        		FileDataHandler.changeVolulmes(volumes);
            	FileDataHandler.saveSettings();
            }
        });
		exitDialog.setVisible(true);
	}
	
	//按格式创建一个滑块
	public JSlider getSlider(int unitValue) {
		JSlider slider=new JSlider(0,100,unitValue);
		slider.setMajorTickSpacing(20); // 设置主刻度间隔
		slider.setMinorTickSpacing(5); // 设置次刻度间隔
		slider.setPaintTicks(true);  // 绘制 刻度 和 标签
		slider.setPaintLabels(true);
		slider.setFont(new Font("微软雅黑",Font.BOLD, 10));
		slider.setForeground(Color.black);
		slider.setOpaque(false);
		return slider;
	}
	
	//新游戏开头
	public void startNewGame() {
		showMiddleMessage("你醒来了,揉了揉眼,站了起来。你面前是一本沾着你的口水的计算机导论"+
				"可能你计导课又睡着了...你发现了你的包,包里还有一些东西。这是哪？");
		showMiddleMessage("提示:如果忘记操作方式可以点击‘H’查看帮助。");
		showLeftMessage("背包 已解锁   点击'B'可查看背包!");
		myData.getBag().addItem(100);
		myData.getBag().addItem(200);
		myData.addRMB(200);
		myData.setPlayerStatus(5);
	}
	
	//询问是否创建房间
	public void whetherCreateRoom(){
		JDialog wcreateRoomDialog = new JDialog(this,"提示",true);
		wcreateRoomDialog.setLayout(null);
		wcreateRoomDialog.setSize(300, 400);
		wcreateRoomDialog.setLocationRelativeTo(null);
		Container container = wcreateRoomDialog.getContentPane(); // 创建一个容器
		
		JLabel wcreateRoomLabel=new JLabel("是否创建房间?");
		wcreateRoomLabel.setFont(new Font("宋体", Font.BOLD, 25));
		wcreateRoomLabel.setForeground(Color.BLACK);
		wcreateRoomLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wcreateRoomLabel.setBounds(45,30,200,40);
		container.add(wcreateRoomLabel); // 在容器中添加标签
		
		JButton enterLANButton = new JButton("创建局域网房间");
		enterLANButton.setFont(new Font("宋体", Font.BOLD, 18));
		enterLANButton.setForeground(Color.BLACK);
		enterLANButton.setContentAreaFilled(false);
		enterLANButton.setFocusPainted(false);
		enterLANButton.setBounds(60, 100, 180, 50);
		enterLANButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				wcreateRoomDialog.dispose();
				new CreateRoomDialog(GameFrame.this,true);
			}
		});
		container.add(enterLANButton);
		
		JButton enterWANButton = new JButton("创建广域网房间");
		enterWANButton.setFont(new Font("宋体", Font.BOLD, 18));
		enterWANButton.setForeground(Color.BLACK);
		enterWANButton.setContentAreaFilled(false);
		enterWANButton.setFocusPainted(false);
		enterWANButton.setBounds(60, 180, 180, 50);
		enterWANButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				wcreateRoomDialog.dispose();
				new CreateRoomDialog(GameFrame.this,false);
			}
		});
		container.add(enterWANButton);
		
		JButton cancelButton = new JButton("取消");
		cancelButton.setFont(new Font("宋体", Font.BOLD, 18));
		cancelButton.setForeground(Color.BLACK);
		cancelButton.setContentAreaFilled(false);
		cancelButton.setFocusPainted(false);
		cancelButton.setBounds(60, 260, 180, 40);
		cancelButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				wcreateRoomDialog.dispose();
			}
		});
		container.add(cancelButton);
		
		container.setBackground (Color.white);
		wcreateRoomDialog.setResizable(false);
		wcreateRoomDialog.setVisible(true);
	}
	
	//改变图片标签的层数
	public void changeImageLabel(int labelNumber,int layer) {	
		if(labels[labelNumber]!=null) gameLayeredPane.setLayer(labels[labelNumber],new Integer(layer));
	}
	
	//展示界面
	public void visible() {
		setVisible(true);
		sound.play("bgmGame1", true);
	}
	
	//隐藏界面
	public void invisible() {
		sound.stopPlay();
		setVisible(false);
	}
	
	//改变 BagDialog的显示状态                                                                                                     
	public void bagDialogSetVisible(boolean whetherShow) {
		if(whetherShow) {
			if(bagDialog==null) {
				bagDialog = new BagDialog(this);
				bagDialog.setVisible(true);
			}
			else {
				bagDialog.getAItems();
				bagDialog.setVisible(true);
			}
		}
		else {
			if(bagDialog!=null) {
				bagDialog.setVisible(false);
			}
		}
	}
	
	//刷新BagDialog金币显示
	public void refreshBagDialogMoney() {
		if(bagDialog!=null) {
			bagDialog.refreshRMB();
		}
	}
	
	//是否在界面上显示好友
	public void showFriendCharacter(boolean show) {
		if(show) {
			if(friendMoveController==null) {
				friendMoveController = new FriendMoveController();
			}
		}
		gameLayeredPane.setLayer(friendLabel,new Integer(show?500:50));
	}
	
	//在界面上初始化好友标签
	public void initFriendCharacter() {
		friendMoveController = new FriendMoveController();
		setChatMessageThread = new SetChatMessageThread();
		setChatMessageThread.start();
		chatDialog = new ChatDialog(this);
		showFriendCharacter(true);
	}
	
	//退出多人联机模式
	public void exitOnlineMode() {
		showFriendCharacter(false);
		if(setChatMessageThread!=null) {
			setChatMessageThread.threadContinue=false;
			setChatMessageThread=null;
		}
		if(chatDialog!=null) {
			chatDialog.dispose();
			chatDialog=null;
		}
	}
	
	//处理收到的聊天信息
	public void receiveChatMessage(String message) {
		if(chatDialog!=null) chatDialog.addMessage(message,false);
	}
	
	//显示chatDialog
	public void showChatDialog() {
		if(chatDialog!=null) chatDialog.setVisible(true);
	}
	
	//刷新好友的方向(静止)
	public void refreshFriendDirection(int direction) {
		friendMoveController.direction=direction;
		switch(direction) {
		case 0:
			friendLabel.setIcon(playerUpImageStop[1-sex]);
			break;
		case 1:
			friendLabel.setIcon(playerDownImageStop[1-sex]);
			break;
		case 2:
			friendLabel.setIcon(playerLeftImageStop[1-sex]);
			break;
		case 3:
			friendLabel.setIcon(playerRightImageStop[1-sex]);
			break;
		}
//		System.out.println("refreshFriendDirection:"+direction);
	}
	
	//刷新好友的方向(静止)
	public void refreshFriendDirection() {
		switch(friendMoveController.direction) {
		case 0:
			friendLabel.setIcon(playerUpImageStop[1-sex]);
			break;
		case 1:
			friendLabel.setIcon(playerDownImageStop[1-sex]);
			break;
		case 2:
			friendLabel.setIcon(playerLeftImageStop[1-sex]);
			break;
		case 3:
			friendLabel.setIcon(playerRightImageStop[1-sex]);
			break;
		}
//		System.out.println("refreshFriendDirection:"+direction);
	}
	
	//FriendMoveController内部类: 控制好友移动
	class FriendMoveController{
		private boolean isMoving=false;
		private int targetX;
		private int targetY;
		private double currentX;
		private double currentY;
		private double leftTime;
		private int direction;
		private final double frameTime = 1.0/Constant.FRAME_RATE;
		private final int friendAccuracy=Constant.FRIEND_ACCURACY;	//好友移动速度精确度
		
		//构造函数:初始化好友角色标签
		public FriendMoveController() {
			direction = myData.getDirection();
			switch(direction) {
			case 0:
				friendLabel.setIcon(playerUpImageStop[1-sex]);
				break;
			case 1:
				friendLabel.setIcon(playerDownImageStop[1-sex]);
				break;
			case 2:
				friendLabel.setIcon(playerLeftImageStop[1-sex]);
				break;
			case 3:
				friendLabel.setIcon(playerRightImageStop[1-sex]);
				break;
			}
			setXYLocation(myData.getX(),myData.getY());
		}
		
		//构造函数:初始化好友角色标签
		public FriendMoveController(int x,int y,int direction) {
			refreshFriendDirection(direction);
			setXYLocation(x,y);
		}
		
		//按xy设定标签位置并保存xy
		public void setXYLocation(int x,int y) {
			currentX=x;
			currentY=y;
			friendLabel.setLocation(x*unitLength+beginX,y*unitLength+beginY);
		}
		
		//按xy设定标签位置并保存xy(double)
		public void setXYLocation(double x,double y) {
			currentX=x;
			currentY=y;
			friendLabel.setLocation((int)(x*unitLength+beginX),(int)(y*unitLength+beginY));
			//System.out.println("标签位置:"+(int)(x*unitLength+beginX)+","+(int)(y*unitLength+beginY));
		}
		
		//好友移动指令
		public void move(int roomNumber,int direction,int x,int y,int intSpeed,int speedAfterPoint) {
			if(roomNumber==myData.getRoom().getRoomNumber()) {
				targetX=x;
				targetY=y;
				this.direction=direction;
				if(JLayeredPane.getLayer(friendLabel)==50) {
					setXYLocation(targetX,targetY);
					refreshFriendDirection(this.direction);
					gameLayeredPane.setLayer(friendLabel,new Integer(500));
					return;
				}
				//System.out.println("好友移动至"+targetX+","+targetY);
				double speed=((double)speedAfterPoint)*friendAccuracy+intSpeed;
				switch(this.direction) {
					case 0:
						friendLabel.setIcon(playerUpImageRun[1-sex]);
						break;
					case 1:
						friendLabel.setIcon(playerDownImageRun[1-sex]);
						break;
					case 2:
						friendLabel.setIcon(playerLeftImageRun[1-sex]);
						break;
					case 3:
						friendLabel.setIcon(playerRightImageRun[1-sex]);
						break;
				}
				leftTime = 1.0/speed;
				if(!isMoving){
					isMoving=true;
					Runnable moveThread = () -> {
						double doubleX;
						double doubleY;
						while(leftTime>0) {
							doubleX=((double)(targetX-currentX))*frameTime/leftTime+currentX;
							doubleY=((double)(targetY-currentY))*frameTime/leftTime+currentY;
							setXYLocation(doubleX,doubleY);
							//System.out.println("好友标签位置:"+currentX+","+currentY);
							leftTime-=frameTime;
							try {
								Thread.sleep((int)(1000*frameTime));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						setXYLocation(targetX,targetY);
						refreshFriendDirection();
						isMoving=false;
				    };
				    Thread moveLine = new Thread(moveThread);
				    moveLine.start();
				}
			}
			else {
				showFriendCharacter(false);
			}
		}
	}
	
	//选择好友聊天信息提示标签显示的信息
	public void refreshChatMessageText(String message) {
		if(setChatMessageThread==null) return;
		setChatMessageThread.chatLabelLeftTime=5000;
		chatMessageLabel.setText(message);
	}
	
	//接收聊天信息线程类
	class SetChatMessageThread extends Thread{
		private boolean threadContinue=true;
		private int chatLabelLeftTime=0;	//左侧信息标签显示剩余时间
		@Override
		public void run(){
			while(threadContinue) {
				if(chatLabelLeftTime>0) {
					gameLayeredPane.setLayer(chatMessageLabel,new Integer(1000));
					chatLabelLeftTime-=50;
				}
				else {
					gameLayeredPane.setLayer(chatMessageLabel,new Integer(50));
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//刷新好友移动角色位置
	public void refreshFriendRunLocation(int roomNumber,int direction,int x,int y,int intSpeed,int speedAfterPoint){
		friendMoveController.move(roomNumber, direction, x, y,intSpeed,speedAfterPoint);
	}
	
	//询问是否保存进度
	public void whetherSaveProgress() {
		JDialog saveDialog = new JDialog(this,"保存",true);
		saveDialog.setLayout(null);
		saveDialog.setSize(400, 220);
		saveDialog.setLocationRelativeTo(null);
		Container container = saveDialog.getContentPane(); // 创建一个容器
		
		JLabel saveLabel=new JLabel("<html> 是否保存当前的游戏进度?<br/>已保存的游戏进度将被覆盖</html>");
		saveLabel.setFont(new Font("宋体", Font.BOLD, 18));
		saveLabel.setForeground(Color.BLACK);
		saveLabel.setHorizontalAlignment(SwingConstants.CENTER);
		saveLabel.setBounds(0,20,400,60);
		container.add(saveLabel); // 在容器中添加标签
		
		JButton enterButton = new JButton("确定");
		enterButton.setFont(new Font("宋体", Font.BOLD, 18));
		enterButton.setForeground(Color.BLACK);
		enterButton.setContentAreaFilled(false);
		enterButton.setFocusPainted(false);
		enterButton.setBounds(55, 120, 100, 30);
		enterButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				myData.saveProgress();
				saveDialog.dispose();
				showLeftMessage("进度已保存");
			}
		});
		container.add(enterButton);
		
		JButton cancelButton = new JButton("取消");
		cancelButton.setFont(new Font("宋体", Font.BOLD, 18));
		cancelButton.setForeground(Color.BLACK);
		cancelButton.setContentAreaFilled(false);
		cancelButton.setFocusPainted(false);
		cancelButton.setBounds(235, 120, 100, 30);
		cancelButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				saveDialog.dispose();
			}
		});
		container.add(cancelButton);
		
		container.setBackground (Color.white);
		saveDialog.setResizable(false);
		saveDialog.setVisible(true);
	}
	
}
