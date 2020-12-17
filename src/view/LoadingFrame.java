package view;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import calculate.Constant;
import service.FileDataHandler;
import service.Sound;

public class LoadingFrame extends JFrame implements MouseListener{
	//LoadingFrame类: 加载界面
	private static final long serialVersionUID = 1L;
	private boolean canShowMenuFrame=false;
	private MenuFrame menuFrame;
//	private Sound sound = new Sound();
	private JLayeredPane loadingLayeredPane;	//层级面板
	private JLabel backgroundLabel;				//背景图片
	private JLabel explorationLabel;			//Exploration图片
	private JLabel projectTeamLabel;			//项目组图片
	private JLabel progressStatusLabel;			//加载状态标签
	private JProgressBar progressBar;			//进度条
	private int frameRate=Constant.FRAME_RATE;	//动画帧率
	private int sleepTime=800/frameRate;	//每帧休眠时间
	
	
	//构造方法:加载组件，创建加载MenuFrame线程
	public LoadingFrame() {
		super("Exploration");
		//sound.playBGMMenu();
		setLayout(null);
		setSize(600, 600);
	    setLocationRelativeTo(null);
	    
	    loadingLayeredPane= new JLayeredPane();
	    loadingLayeredPane.setLayout(null);
	    loadingLayeredPane.setSize(600, 600);
		
	    progressBar=new JProgressBar();
	    progressBar.setStringPainted(true);
	    progressBar.setBounds(380,500,180,30);
	    setProgressValue(0);
	    
	    ImageIcon backgroundIcon = new ImageIcon("image/LoadingFrame/background.jpg");
		backgroundIcon.setImage(backgroundIcon.getImage().getScaledInstance(700,600,Image.SCALE_DEFAULT));
		backgroundLabel=new JLabel(backgroundIcon);
		backgroundLabel.setBounds(-90, 0, 700, 600);
		
		ImageIcon explorationIcon = new ImageIcon("image/LoadingFrame/Exploration.png");
		explorationIcon.setImage(explorationIcon.getImage().getScaledInstance(500,110,Image.SCALE_DEFAULT));
		explorationLabel=new JLabel(explorationIcon);
		explorationLabel.setBounds(50,70,500,110);
		
		ImageIcon projectTeamIcon = new ImageIcon("image/LoadingFrame/projectTeam.png");
		projectTeamIcon.setImage(projectTeamIcon.getImage().getScaledInstance(200,90,Image.SCALE_DEFAULT));
		projectTeamLabel=new JLabel(projectTeamIcon);
		projectTeamLabel.setBounds(0,0,200,90);
		
		progressStatusLabel=new JLabel("加载中");
		progressStatusLabel.setFont(new Font("宋体", Font.BOLD, 20));
		progressStatusLabel.setForeground(Color.white);
		progressStatusLabel.setBounds(50,500,150,30);
		progressStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		loadingLayeredPane.add(progressBar,new Integer(300));
		loadingLayeredPane.add(backgroundLabel,new Integer(200));
		loadingLayeredPane.add(explorationLabel,new Integer(300));
		loadingLayeredPane.add(progressStatusLabel,new Integer(300));
		loadingLayeredPane.add(projectTeamLabel,new Integer(300));
		
		ImageIcon icon = new ImageIcon("image/LoadingFrame/icon.png");	//窗口图标
        setIconImage(icon.getImage());
		addWindowListener(new WindowAdapter() {// 添加窗体监听
	        public void windowClosing(WindowEvent e) {// 窗体关闭前
	        	System.exit(0);
	        }
	    });
		addMouseListener(this);
		setContentPane(loadingLayeredPane);
		setResizable(false);
		setVisible(true);
	    
		Runnable projectTeamThread = () -> {
			int singleFrameNumbers=frameRate/3;
			int x;
			for(int i=0;i<frameRate;i++) {
				switch (i/singleFrameNumbers) {
				case 0:
					projectTeamLabel.setLocation(0,170*i/singleFrameNumbers);
					break;
				case 1:
					x=100*(i-singleFrameNumbers)/singleFrameNumbers;
					projectTeamLabel.setLocation(x,(x-100)*x/50+170);
					break;
				case 2:
					x=100*(i-2*singleFrameNumbers)/singleFrameNumbers;
					projectTeamLabel.setLocation(x+100,(x-100)*x/50+170);
					break;
				}
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
	    };
	    Thread projectTeamLine = new Thread(projectTeamThread);
	    projectTeamLine.start();
		
		Runnable loadingThread = () -> {
			setProgressValue(0);
			FileDataHandler.initSettings();
			setProgressValue(50);
			menuFrame=new MenuFrame(this);
			setProgressValue(100);
			canShowMenuFrame=true;
			progressStatusLabel.setText("点击任意处继续");
	    };
	    Thread loadingLine = new Thread(loadingThread);
	    loadingLine.start();
	     
	}

	//设置加载进度
	public void setProgressValue(int n) {
		progressBar.setValue(n);
	}
	
	
	//鼠标点击显示菜单界面
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		if(canShowMenuFrame) {
			dispose();
			menuFrame.visibleAndPlayBGM();
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	
	
	
	
}
