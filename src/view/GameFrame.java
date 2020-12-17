package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import calculate.*;
import service.*;

public class GameFrame extends JFrame{
	//GameFrame��: ��Ϸ���� (�̳�JFrame��)
	private static final long serialVersionUID = 1L;
	private final int unitLength=Constant.UNIT_LENGTH;
	private final int fullPower=(int)Constant.FULL_POWER;
	private final int roomMaxImage=Constant.ROOM_MAX_IMAGE;
	private MenuFrame menuFrame;
	private int beginX;
	private int beginY;
	private int sex;
	private MyData myData;	//��Ϸ����
	private MoveThread moveThread;	//�ƶ��߳�
	private boolean leftLabelShowing;	//�����Ϣ��ǩ������ʾ
	private boolean middleLabelShowing;	//�в���Ϣ��ǩ������ʾ
	private BagDialog bagDialog;
	private LinkQueue leftMessageQueue = new LinkQueue();
	private LinkQueue middleMessageQueue = new LinkQueue();
	private Sound sound = new Sound();
	private FriendMoveController friendMoveController;
	private SetChatMessageThread setChatMessageThread;
	private ChatDialog chatDialog;
	
	private JLabel playerLabel;		//��ұ�ǩ
	private JLabel friendLabel;		//���ѱ�ǩ
	private JLabel powerLabel;		//������ǩ
	private JLabel blackLabel;		//��ɫ������ǩ
	private JLabel roomNameLabel;	//��������ǩ
	private JLabel leftMessageLabel;	//�����Ϣ��ǩ
	private JLabel middleMessageLabel;	//�в���Ϣ��ǩ
	private JLabel chatMessageLabel;	//������Ϣ��ǩ
	private JLabel[] labels = new JLabel[roomMaxImage];	
	private JLayeredPane gameLayeredPane;	//�㼶���
	
	private ImageIcon playerUpImageStop[] = new ImageIcon[2];
	private ImageIcon playerUpImageRun[] = new ImageIcon[2];
	private ImageIcon playerDownImageStop[] = new ImageIcon[2];
	private ImageIcon playerDownImageRun[] = new ImageIcon[2];
	private ImageIcon playerRightImageStop[] = new ImageIcon[2];
	private ImageIcon playerRightImageRun[] = new ImageIcon[2];
	private ImageIcon playerLeftImageStop[] = new ImageIcon[2];
	private ImageIcon playerLeftImageRun[] = new ImageIcon[2];
	
	//���캯��:������Ϸ����
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
		powerLabel.setFont(new Font("΢���ź�", Font.BOLD, 15));
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
		leftMessageLabel.setFont(new Font("����", Font.BOLD, 18));
		leftMessageLabel.setForeground(Color.black);
		leftMessageLabel.setBounds(5,300,250,150);
		leftMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		middleMessageLabel=new JLabel();
		middleMessageLabel.setOpaque(true);  
		middleMessageLabel.setBackground(Color.white);
		middleMessageLabel.setFont(new Font("����", Font.BOLD, 18));
		middleMessageLabel.setForeground(Color.black);
		middleMessageLabel.setBounds(500,250,500,300);
		middleMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		chatMessageLabel=new JLabel();
		chatMessageLabel.setOpaque(true);  
		chatMessageLabel.setBackground(Color.white);
		chatMessageLabel.setFont(new Font("����", Font.BOLD, 18));
		chatMessageLabel.setForeground(Color.black);
		chatMessageLabel.setBounds(5,680,250,100);
		chatMessageLabel.setHorizontalAlignment(SwingConstants.LEFT);
		chatMessageLabel.setVerticalAlignment(SwingConstants.CENTER);
		
		ImageIcon blackIcon = new ImageIcon("image/GameFrame/black.png");
		blackIcon.setImage(blackIcon.getImage().getScaledInstance(1500,830,Image.SCALE_DEFAULT));
		blackLabel=new JLabel(blackIcon);
		blackLabel.setBounds(0,0,1500,830);
		
		addMouseListener(new MouseAdapter(){  //�����ڲ��࣬����¼�
            public void mouseClicked(MouseEvent e){   //�����ɵ���¼�
                if(e.getButton() == MouseEvent.BUTTON1){ //e.getButton�ͻ᷵�ص������Ǹ�������������ҽ���3�����Ҽ�
//                	System.out.println("���λ��: "+e.getX()+","+e.getY());
                	int x=e.getX();
                	int y=e.getY();
                	moveThread.pathSearch((x-beginX)/unitLength,(y-beginY-30)/unitLength);
                }
            }
        });
		
		ImageIcon icon = new ImageIcon("image/GameFrame/icon.png");	//����ͼ��
        setIconImage(icon.getImage());
		setTitle("Exploratiaon");	// ����
		setBackground(Color.BLACK);
		setResizable(false);
        addWindowListener(new WindowAdapter() {// ��Ӵ������
            public void windowClosing(WindowEvent e) {// ����ر�ǰ
            	System.exit(0);
            }
        });	
        
	}
	
	//���ط���������
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
	
	//��ʼ��gameFrame����
	public void initGameFrame() {
		refreshStopLocation(myData.getDirection());
		playerLabel.setLocation(myData.getX()*unitLength+beginX,myData.getY()*unitLength+beginY);
		loadComponents();
	}
	
	//ˢ�½����н�ɫλ��
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
	
	//�����н�ɫ����(��ֹ)
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
	
	//����moveThread
	public MoveThread getMoveThread() {
		return moveThread;
	}
	
	//����myData
	public MyData getMyData() {
		return myData;
	}
	
	//ˢ������ֵ
	public void refreshPower() {
		powerLabel.setText("���� "+(int)myData.getPower()+" / "+fullPower);
	}
	
	//ˢ�·�����
	public void refreshRoomName() {
		roomNameLabel.setText(myData.getRoom().getRoomName());
	}
	
	//�����ʾ��Ϣ
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
					//System.out.println("����size:"+leftMessageQueue.size);
				}while(!leftMessageQueue.QueueEmpty());
				leftLabelShowing=false;
		    };
		    Thread leftMessageLine = new Thread(leftMessageThread);
		    leftMessageLine.start();
		}
	}
	
	//�в���ʾ��Ϣ
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
		resultMessage+= "<br/><br/>(����ո��Լ���)";
		resultMessage+="</html>";
		middleMessageQueue.EnQueue(resultMessage);
		if(!middleLabelShowing) {
			middleLabelShowing=true;
			middleMessageLabel.setText(middleMessageQueue.DeQueue());
			gameLayeredPane.setLayer(middleMessageLabel,new Integer(1000));
		}
	}
	
	//�в���Ϣ��ҳ
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

	//ѯ���Ƿ��˳���Ϸ
	public void whetherExit() {
		JDialog exitDialog = new JDialog(this,"��ʾ",true);
		exitDialog.setLayout(null);
		exitDialog.setSize(400, 180);
		exitDialog.setLocationRelativeTo(null);
		Container container = exitDialog.getContentPane(); // ����һ������
		
		JLabel exitLabel=new JLabel("�Ƿ��˳���Ϸ?");
		exitLabel.setFont(new Font("����", Font.BOLD, 20));
		exitLabel.setForeground(Color.BLACK);
		exitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		exitLabel.setBounds(95,20,200,40);
		container.add(exitLabel); // ����������ӱ�ǩ
		
		JButton exitButton = new JButton("�˳�");
		exitButton.setFont(new Font("����", Font.BOLD, 18));
		exitButton.setForeground(Color.BLACK);
		exitButton.setContentAreaFilled(false);
		exitButton.setFocusPainted(false);
		exitButton.setBounds(25, 100, 100, 30);
		exitButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		container.add(exitButton);
		
		JButton menuButton = new JButton("���˵�");
		menuButton.setFont(new Font("����", Font.BOLD, 18));
		menuButton.setForeground(Color.BLACK);
		menuButton.setContentAreaFilled(false);
		menuButton.setFocusPainted(false);
		menuButton.setBounds(145, 100, 100, 30);
		menuButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.stopPlay();
				dispose();
				menuFrame.visibleAndPlayBGM();
			}
		});
		container.add(menuButton);
		
		JButton cancelButton = new JButton("ȡ��");
		cancelButton.setFont(new Font("����", Font.BOLD, 18));
		cancelButton.setForeground(Color.BLACK);
		cancelButton.setContentAreaFilled(false);
		cancelButton.setFocusPainted(false);
		cancelButton.setBounds(265, 100, 100, 30);
		cancelButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
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
	
	//�ı�����
	public void changeVolume() {
		JDialog exitDialog = new JDialog(this,"����",true);
		exitDialog.setLayout(null);
		exitDialog.setSize(320,200);
		exitDialog.setLocationRelativeTo(null);
		Container container = exitDialog.getContentPane(); // ����һ������
		
		String text="<html>������<br/><br/>&#12288����<br/><br/>&#12288��Ч</html>";
		JLabel volumeLabel=new JLabel(text);
		volumeLabel.setFont(new Font("����",Font.BOLD, 20));
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
		exitDialog.addWindowListener(new WindowAdapter() {// ��Ӵ������
            public void windowClosing(WindowEvent e) {// ����ر�ǰ
        		FileDataHandler.changeVolulmes(volumes);
            	FileDataHandler.saveSettings();
            }
        });
		exitDialog.setVisible(true);
	}
	
	//����ʽ����һ������
	public JSlider getSlider(int unitValue) {
		JSlider slider=new JSlider(0,100,unitValue);
		slider.setMajorTickSpacing(20); // �������̶ȼ��
		slider.setMinorTickSpacing(5); // ���ôο̶ȼ��
		slider.setPaintTicks(true);  // ���� �̶� �� ��ǩ
		slider.setPaintLabels(true);
		slider.setFont(new Font("΢���ź�",Font.BOLD, 10));
		slider.setForeground(Color.black);
		slider.setOpaque(false);
		return slider;
	}
	
	//����Ϸ��ͷ
	public void startNewGame() {
		showMiddleMessage("��������,��������,վ������������ǰ��һ��մ����Ŀ�ˮ�ļ��������"+
				"������Ƶ�����˯����...�㷢������İ�,���ﻹ��һЩ�����������ģ�");
		showMiddleMessage("��ʾ:������ǲ�����ʽ���Ե����H���鿴������");
		showLeftMessage("���� �ѽ���   ���'B'�ɲ鿴����!");
		myData.getBag().addItem(100);
		myData.getBag().addItem(200);
		myData.addRMB(200);
		myData.setPlayerStatus(5);
	}
	
	//ѯ���Ƿ񴴽�����
	public void whetherCreateRoom(){
		JDialog wcreateRoomDialog = new JDialog(this,"��ʾ",true);
		wcreateRoomDialog.setLayout(null);
		wcreateRoomDialog.setSize(300, 400);
		wcreateRoomDialog.setLocationRelativeTo(null);
		Container container = wcreateRoomDialog.getContentPane(); // ����һ������
		
		JLabel wcreateRoomLabel=new JLabel("�Ƿ񴴽�����?");
		wcreateRoomLabel.setFont(new Font("����", Font.BOLD, 25));
		wcreateRoomLabel.setForeground(Color.BLACK);
		wcreateRoomLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wcreateRoomLabel.setBounds(45,30,200,40);
		container.add(wcreateRoomLabel); // ����������ӱ�ǩ
		
		JButton enterLANButton = new JButton("��������������");
		enterLANButton.setFont(new Font("����", Font.BOLD, 18));
		enterLANButton.setForeground(Color.BLACK);
		enterLANButton.setContentAreaFilled(false);
		enterLANButton.setFocusPainted(false);
		enterLANButton.setBounds(60, 100, 180, 50);
		enterLANButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				wcreateRoomDialog.dispose();
				new CreateRoomDialog(GameFrame.this,true);
			}
		});
		container.add(enterLANButton);
		
		JButton enterWANButton = new JButton("��������������");
		enterWANButton.setFont(new Font("����", Font.BOLD, 18));
		enterWANButton.setForeground(Color.BLACK);
		enterWANButton.setContentAreaFilled(false);
		enterWANButton.setFocusPainted(false);
		enterWANButton.setBounds(60, 180, 180, 50);
		enterWANButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				wcreateRoomDialog.dispose();
				new CreateRoomDialog(GameFrame.this,false);
			}
		});
		container.add(enterWANButton);
		
		JButton cancelButton = new JButton("ȡ��");
		cancelButton.setFont(new Font("����", Font.BOLD, 18));
		cancelButton.setForeground(Color.BLACK);
		cancelButton.setContentAreaFilled(false);
		cancelButton.setFocusPainted(false);
		cancelButton.setBounds(60, 260, 180, 40);
		cancelButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
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
	
	//�ı�ͼƬ��ǩ�Ĳ���
	public void changeImageLabel(int labelNumber,int layer) {	
		if(labels[labelNumber]!=null) gameLayeredPane.setLayer(labels[labelNumber],new Integer(layer));
	}
	
	//չʾ����
	public void visible() {
		setVisible(true);
		sound.play("bgmGame1", true);
	}
	
	//���ؽ���
	public void invisible() {
		sound.stopPlay();
		setVisible(false);
	}
	
	//�ı� BagDialog����ʾ״̬                                                                                                     
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
	
	//ˢ��BagDialog�����ʾ
	public void refreshBagDialogMoney() {
		if(bagDialog!=null) {
			bagDialog.refreshRMB();
		}
	}
	
	//�Ƿ��ڽ�������ʾ����
	public void showFriendCharacter(boolean show) {
		if(show) {
			if(friendMoveController==null) {
				friendMoveController = new FriendMoveController();
			}
		}
		gameLayeredPane.setLayer(friendLabel,new Integer(show?500:50));
	}
	
	//�ڽ����ϳ�ʼ�����ѱ�ǩ
	public void initFriendCharacter() {
		friendMoveController = new FriendMoveController();
		setChatMessageThread = new SetChatMessageThread();
		setChatMessageThread.start();
		chatDialog = new ChatDialog(this);
		showFriendCharacter(true);
	}
	
	//�˳���������ģʽ
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
	
	//�����յ���������Ϣ
	public void receiveChatMessage(String message) {
		if(chatDialog!=null) chatDialog.addMessage(message,false);
	}
	
	//��ʾchatDialog
	public void showChatDialog() {
		if(chatDialog!=null) chatDialog.setVisible(true);
	}
	
	//ˢ�º��ѵķ���(��ֹ)
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
	
	//ˢ�º��ѵķ���(��ֹ)
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
	
	//FriendMoveController�ڲ���: ���ƺ����ƶ�
	class FriendMoveController{
		private boolean isMoving=false;
		private int targetX;
		private int targetY;
		private double currentX;
		private double currentY;
		private double leftTime;
		private int direction;
		private final double frameTime = 1.0/Constant.FRAME_RATE;
		private final int friendAccuracy=Constant.FRIEND_ACCURACY;	//�����ƶ��ٶȾ�ȷ��
		
		//���캯��:��ʼ�����ѽ�ɫ��ǩ
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
		
		//���캯��:��ʼ�����ѽ�ɫ��ǩ
		public FriendMoveController(int x,int y,int direction) {
			refreshFriendDirection(direction);
			setXYLocation(x,y);
		}
		
		//��xy�趨��ǩλ�ò�����xy
		public void setXYLocation(int x,int y) {
			currentX=x;
			currentY=y;
			friendLabel.setLocation(x*unitLength+beginX,y*unitLength+beginY);
		}
		
		//��xy�趨��ǩλ�ò�����xy(double)
		public void setXYLocation(double x,double y) {
			currentX=x;
			currentY=y;
			friendLabel.setLocation((int)(x*unitLength+beginX),(int)(y*unitLength+beginY));
			//System.out.println("��ǩλ��:"+(int)(x*unitLength+beginX)+","+(int)(y*unitLength+beginY));
		}
		
		//�����ƶ�ָ��
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
				//System.out.println("�����ƶ���"+targetX+","+targetY);
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
							//System.out.println("���ѱ�ǩλ��:"+currentX+","+currentY);
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
	
	//ѡ�����������Ϣ��ʾ��ǩ��ʾ����Ϣ
	public void refreshChatMessageText(String message) {
		if(setChatMessageThread==null) return;
		setChatMessageThread.chatLabelLeftTime=5000;
		chatMessageLabel.setText(message);
	}
	
	//����������Ϣ�߳���
	class SetChatMessageThread extends Thread{
		private boolean threadContinue=true;
		private int chatLabelLeftTime=0;	//�����Ϣ��ǩ��ʾʣ��ʱ��
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
	
	//ˢ�º����ƶ���ɫλ��
	public void refreshFriendRunLocation(int roomNumber,int direction,int x,int y,int intSpeed,int speedAfterPoint){
		friendMoveController.move(roomNumber, direction, x, y,intSpeed,speedAfterPoint);
	}
	
	//ѯ���Ƿ񱣴����
	public void whetherSaveProgress() {
		JDialog saveDialog = new JDialog(this,"����",true);
		saveDialog.setLayout(null);
		saveDialog.setSize(400, 220);
		saveDialog.setLocationRelativeTo(null);
		Container container = saveDialog.getContentPane(); // ����һ������
		
		JLabel saveLabel=new JLabel("<html> �Ƿ񱣴浱ǰ����Ϸ����?<br/>�ѱ������Ϸ���Ƚ�������</html>");
		saveLabel.setFont(new Font("����", Font.BOLD, 18));
		saveLabel.setForeground(Color.BLACK);
		saveLabel.setHorizontalAlignment(SwingConstants.CENTER);
		saveLabel.setBounds(0,20,400,60);
		container.add(saveLabel); // ����������ӱ�ǩ
		
		JButton enterButton = new JButton("ȷ��");
		enterButton.setFont(new Font("����", Font.BOLD, 18));
		enterButton.setForeground(Color.BLACK);
		enterButton.setContentAreaFilled(false);
		enterButton.setFocusPainted(false);
		enterButton.setBounds(55, 120, 100, 30);
		enterButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				myData.saveProgress();
				saveDialog.dispose();
				showLeftMessage("�����ѱ���");
			}
		});
		container.add(enterButton);
		
		JButton cancelButton = new JButton("ȡ��");
		cancelButton.setFont(new Font("����", Font.BOLD, 18));
		cancelButton.setForeground(Color.BLACK);
		cancelButton.setContentAreaFilled(false);
		cancelButton.setFocusPainted(false);
		cancelButton.setBounds(235, 120, 100, 30);
		cancelButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
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
