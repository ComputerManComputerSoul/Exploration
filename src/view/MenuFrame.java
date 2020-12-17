package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import calculate.Constant;
import calculate.MyData;
import service.FileDataHandler;
import service.Sound;

public class MenuFrame extends JFrame implements ActionListener{
	//MenuFrame类: 菜单界面 
	private static final long serialVersionUID = 1L;
	private LoadingFrame loadingFrame;		//加载窗口
	private JLayeredPane layeredPane;		//层级面板
	
	private JButton newGameButton;		//创建游戏
	private JButton continueGameButton;	//继续游戏
	private JButton enterRoomButton;	//加入房间
	private JButton exitButton;			//退出游戏
	private JButton updateLogButton;	//更新日志
	private JButton helpButton;			//帮助
	private JButton developerButton;	//开发人员
	private JButton settingButton;		//设置
	private JButton returnButton;		//返回主菜单
	private JButton sexChangeButton;	//改变性别
	private JLabel backgroundLabel;		//背景图片
	private JLabel titleLabel;			//标题图片
	private JLabel helpLabel;			//帮助标签
	private JLabel developerLabel;		//开发人员标签
	private JLabel updateLogLabel;		//更新日志标签
	private JLabel sexLabel;			//性别标签
	private JScrollPane updateLogScrollPane;	//更新日志滚动条面板
	private JLabel volumeLabel;			//音量标签
	private JSlider masterVolumeSlider;	//主音量滑动条
	private JSlider bgmVolumeSlider;	//音乐音量滑动条
	private JSlider soundEffectVolumnVolumeSlider;	//音效音量滑动条
	
	private int status=0;	//当前显示状态 0：menu 1:update 2:help 3:developer 4:setting
	private int[] volumes;
	private int sex;
	private Sound sound = new Sound();	//音乐选择
	private final String[] sexString = {"男","女"};
	
	public MenuFrame(LoadingFrame sendLoadingFrame) {
		super("Exploration");
		loadingFrame=sendLoadingFrame;
		setLayout(null);
		setSize(1000, 630);
	    setLocationRelativeTo(null);
	    
		layeredPane= new JLayeredPane();
		layeredPane.setLayout(null);
		layeredPane.setSize(1000, 630);
		
		ImageIcon backgroundIcon = new ImageIcon("image/MenuFrame/background.jpg");
		backgroundIcon.setImage(backgroundIcon.getImage().getScaledInstance(1000,630,Image.SCALE_DEFAULT));
		backgroundLabel=new JLabel(backgroundIcon);
		backgroundLabel.setBounds(0, 0, 1000, 630);
		
		ImageIcon titleIcon = new ImageIcon("image/MenuFrame/title.png");
		titleIcon.setImage(titleIcon.getImage().getScaledInstance(800,180,Image.SCALE_DEFAULT));
		titleLabel=new JLabel(titleIcon);
		titleLabel.setBounds(145,130,800,200);
		loadingFrame.setProgressValue(55);//
		
		newGameButton=getButton("创建游戏",175, 380, 200, 50,25);
		continueGameButton=getButton("继续游戏",610, 380, 200, 50,25);
		enterRoomButton=getButton("加入房间",235, 470, 200, 50,25);
		exitButton=getButton("退出游戏",550, 470, 200, 50,25);
		updateLogButton=getButton("更新日志",25, 25, 130, 30,20);
		helpButton=getButton("帮助",55, 80, 100, 30,20);
		sexChangeButton=getButton("更改",550, 300, 100, 30,20);
		developerButton=getButton("开发人员",835, 25, 130, 30,20);
		settingButton=getButton("设置",835, 80, 100, 30,20);
		returnButton=getButton("返回",835, 500, 100, 30,20);
		
		loadingFrame.setProgressValue(60);//
		
		helpLabel = new JLabel(Constant.HELP_TEXT);
		helpLabel.setFont(new Font("宋体", Font.BOLD, 20));
		helpLabel.setForeground(Color.white);
		helpLabel.setBounds(0, 0, 1000, 570);
		helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loadingFrame.setProgressValue(67);//
		
		developerLabel = new JLabel(Constant.DEVELOPER_TEXT);
		developerLabel.setFont(new Font("宋体", Font.BOLD, 20));
		developerLabel.setForeground(Color.white);
		developerLabel.setBounds(0, 0, 1000, 570);
		developerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loadingFrame.setProgressValue(75);//
		
		updateLogLabel = new JLabel(Constant.UPDATELOG_TEXT);
		updateLogLabel.setFont(new Font("宋体", Font.BOLD, 20));
		updateLogLabel.setForeground(Color.white);
		updateLogLabel.setBounds(200,35,600,520);
		updateLogLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		updateLogScrollPane = new JScrollPane(updateLogLabel);
		updateLogScrollPane.getVerticalScrollBar().setUnitIncrement(15);//设置灵敏度
		updateLogScrollPane.getVerticalScrollBar().setOpaque(false);
		updateLogScrollPane.setBounds(170,0,640,595);
		updateLogScrollPane.setOpaque(false);
		updateLogScrollPane.getViewport().setOpaque(false);
		loadingFrame.setProgressValue(82);
		
		volumes=FileDataHandler.getVolumes();
		sex=FileDataHandler.getSex();
		
		sexLabel = new JLabel("当前性别: "+sexString[sex]);
		sexLabel.setFont(new Font("宋体", Font.BOLD, 20));
		sexLabel.setForeground(Color.white);
		sexLabel.setBounds(300, 300, 150, 30);
		sexLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		String text="<html>主音量<br/><br/><br/>&#12288音乐<br/><br/><br/>&#12288音效</html>";
		volumeLabel=new JLabel(text);
		volumeLabel.setFont(new Font("宋体",Font.BOLD, 20));
		volumeLabel.setForeground(Color.white);
		volumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		volumeLabel.setBounds(220,40,75,200);
		
		masterVolumeSlider=getSlider(volumes[0]);
		masterVolumeSlider.setBounds(300, 60, 400, 50);
		masterVolumeSlider.addChangeListener(new ChangeListener() {
	            @Override
	            public void stateChanged(ChangeEvent e) {
	            	volumes[0]=masterVolumeSlider.getValue();
	            	sound.temporaryChangeVolumn(((float)volumes[0]*volumes[1])/10000);
	            }
	    });
        
		bgmVolumeSlider=getSlider(volumes[1]);
		bgmVolumeSlider.setBounds(300, 130, 400, 50);
		bgmVolumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            	volumes[1]=bgmVolumeSlider.getValue();
            	sound.temporaryChangeVolumn(((float)volumes[0]*volumes[1])/10000);
            }
		});
		
		soundEffectVolumnVolumeSlider=getSlider(volumes[2]);
		soundEffectVolumnVolumeSlider.setBounds(300, 200, 400, 50);
		soundEffectVolumnVolumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            	volumes[2]=soundEffectVolumnVolumeSlider.getValue();
            }
		});
		loadingFrame.setProgressValue(90);

		layeredPane.add(backgroundLabel,new Integer(200));
		addMenuComponent();
		
		ImageIcon icon = new ImageIcon("image/MenuFrame/icon.png");	//窗口图标
        setIconImage(icon.getImage());
		addWindowListener(new WindowAdapter() {// 添加窗体监听
	        public void windowClosing(WindowEvent e) {// 窗体关闭前
	        	System.exit(0);
	        }
	    });
		setContentPane(layeredPane);
		setResizable(false);
		loadingFrame.setProgressValue(99);
	}
	
	//添加主菜单的组件
	public void addMenuComponent() {
		layeredPane.add(titleLabel,new Integer(300));
		layeredPane.add(newGameButton,new Integer(300));
		layeredPane.add(continueGameButton,new Integer(300));
		layeredPane.add(enterRoomButton,new Integer(300));
		layeredPane.add(exitButton,new Integer(300));
		layeredPane.add(updateLogButton,new Integer(300));
		layeredPane.add(helpButton,new Integer(300));
		layeredPane.add(developerButton,new Integer(300));
		layeredPane.add(settingButton,new Integer(300));
	}
	
	//移除主菜单的组件
	public void removeMenuComponent() {
		layeredPane.remove(titleLabel);
		layeredPane.remove(newGameButton);
		layeredPane.remove(continueGameButton);
		layeredPane.remove(enterRoomButton);
		layeredPane.remove(exitButton);
		layeredPane.remove(updateLogButton);
		layeredPane.remove(helpButton);
		layeredPane.remove(developerButton);
		layeredPane.remove(settingButton);
	}
	
	//添加更新日志的组件
	public void addUpdateLogComponent() {
		layeredPane.add(updateLogScrollPane,new Integer(300));
		layeredPane.add(returnButton,new Integer(300));
	}
		
	//移除更新日志的组件
	public void removeUpdateLogComponent() {
		layeredPane.remove(updateLogScrollPane);
		layeredPane.remove(returnButton);
	}
	
	//添加帮助的组件
	public void addHelpComponent() {
		layeredPane.add(helpLabel,new Integer(300));
		layeredPane.add(returnButton,new Integer(300));
	}
	
	//移除帮助的组件
	public void removeHelpComponent() {
		layeredPane.remove(helpLabel);
		layeredPane.remove(returnButton);
	}
    
	//添加开发人员的组件
	public void addDeveloperComponent() {
		layeredPane.add(developerLabel,new Integer(300));
		layeredPane.add(returnButton,new Integer(300));
	}
	
	//移除开发人员的组件
	public void removeDeveloperComponent() {
		layeredPane.remove(developerLabel);
		layeredPane.remove(returnButton);
	}
	
	//添加设置的组件
	public void addSettingComponent() {
		layeredPane.add(volumeLabel,new Integer(300));
		layeredPane.add(masterVolumeSlider,new Integer(300));
		layeredPane.add(bgmVolumeSlider,new Integer(300));
		layeredPane.add(soundEffectVolumnVolumeSlider,new Integer(300));
		layeredPane.add(sexLabel,new Integer(300));
		layeredPane.add(sexChangeButton,new Integer(300));
		layeredPane.add(returnButton,new Integer(300));
	}
	
	//移除设置的组件
	public void removeSettingComponent() {
		layeredPane.remove(volumeLabel);
		layeredPane.remove(masterVolumeSlider);
		layeredPane.remove(bgmVolumeSlider);
		layeredPane.remove(soundEffectVolumnVolumeSlider);
		layeredPane.remove(sexLabel);
		layeredPane.remove(sexChangeButton);
		layeredPane.remove(returnButton);
	}		

	//按格式创建一个滑块
	public JSlider getSlider(int unitValue) {
		JSlider slider=new JSlider(0,100,unitValue);
		slider.setMajorTickSpacing(20); // 设置主刻度间隔
		slider.setMinorTickSpacing(5); // 设置次刻度间隔
		slider.setPaintTicks(true);  // 绘制 刻度 和 标签
		slider.setPaintLabels(true);
		slider.setFont(new Font("微软雅黑",Font.BOLD, 18));
		slider.setForeground(Color.white);
		slider.setOpaque(false);
		return slider;
	}
	
	//按格式创建一个按钮
	public JButton getButton(String buttonName,int x,int y,int width,int height,int fontSize) {
		JButton button = new JButton(buttonName);
		button.setFont(new Font("宋体", Font.BOLD, fontSize));
		button.setForeground(Color.white);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setBounds(x, y, width, height);
		button.addActionListener(this);	
		return button;
	}
	
	//按钮的事件监听器方法
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newGameButton) {
			invisibleAndStopBGM();
			GameFrame gameFrame = new GameFrame(this,0,sex);
			gameFrame.loadComponents();
			gameFrame.visible();
			gameFrame.startNewGame();
		}
		if (e.getSource() == continueGameButton) {
			if(FileDataHandler.progressFileExist()) {
				invisibleAndStopBGM();
				GameFrame gameFrame = new GameFrame(this,6,sex);
				gameFrame.getMyData().loadProgress();
				gameFrame.getMyData().setPlayerStatus(5);
				gameFrame.initGameFrame();
				gameFrame.visible();
			}
			else {
				new TextDialog(this,"读取失败",350,200,"读取失败，无游戏进度文件!");
			}
		}
		if (e.getSource() == enterRoomButton) {
			new EnterRoomDialog(this);
		}
		if (e.getSource() == exitButton) {
			whetherExit();
		}
		if (e.getSource() == updateLogButton) {
			status=1;
			removeMenuComponent();
			layeredPane.repaint();
			addUpdateLogComponent();
			layeredPane.revalidate();
		}
		if (e.getSource() == helpButton) {
			status=2;
			removeMenuComponent();
			layeredPane.repaint();
			addHelpComponent();
			layeredPane.revalidate();
		}
		if (e.getSource() == developerButton) {
			status=3;
			removeMenuComponent();
			layeredPane.repaint();
			addDeveloperComponent();
			layeredPane.revalidate();
		}
		if (e.getSource() == settingButton) {
			status=4;
			volumes=FileDataHandler.getVolumes();
			sex=FileDataHandler.getSex();
			removeMenuComponent();
			layeredPane.repaint();
			addSettingComponent();
			layeredPane.revalidate();
		}
		if(e.getSource() == sexChangeButton) {
			sex=1-sex;
			sexLabel.setText("当前性别: "+sexString[sex]);
		}
		if (e.getSource() == returnButton) {
			switch(status) {
				case 1:
					removeUpdateLogComponent();
					break;
				case 2:
					removeHelpComponent();
					break;
				case 3:
					removeDeveloperComponent();
					break;
				case 4:
					removeSettingComponent();
	            	FileDataHandler.changeVolulmes(volumes);
	            	FileDataHandler.changeSex(sex);
	            	FileDataHandler.saveSettings();
					break;
			}
			status=0;
			layeredPane.repaint();
			addMenuComponent();
			layeredPane.revalidate();
		}
	}
	
	//显示界面并播放音乐
	public void visibleAndPlayBGM() {
		setVisible(true);
		sound.play("bgmMenu", true);
	}
	
	//隐藏界面并停止播放音乐
	public void invisibleAndStopBGM() {
		sound.stopPlay();
		setVisible(false);
	}
	
	//询问是否退出游戏
	public void whetherExit() {
		JDialog exitDialog = new JDialog(this,"提示",true);
		exitDialog.setLayout(null);
		exitDialog.setSize(300, 180);
		exitDialog.setLocationRelativeTo(null);
		Container container = exitDialog.getContentPane(); // 创建一个容器
		
		JLabel exitLabel=new JLabel("是否退出游戏?");
		exitLabel.setFont(new Font("宋体", Font.BOLD, 20));
		exitLabel.setForeground(Color.BLACK);
		exitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		exitLabel.setBounds(45,20,200,40);
		container.add(exitLabel); // 在容器中添加标签
		
		JButton enterButton = new JButton("确定");
		enterButton.setFont(new Font("宋体", Font.BOLD, 18));
		enterButton.setForeground(Color.BLACK);
		enterButton.setContentAreaFilled(false);
		enterButton.setFocusPainted(false);
		enterButton.setBounds(30, 100, 80, 30);
		enterButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		container.add(enterButton);
		
		JButton cancelButton = new JButton("取消");
		cancelButton.setFont(new Font("宋体", Font.BOLD, 18));
		cancelButton.setForeground(Color.BLACK);
		cancelButton.setContentAreaFilled(false);
		cancelButton.setFocusPainted(false);
		cancelButton.setBounds(180, 100, 80, 30);
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
	
}