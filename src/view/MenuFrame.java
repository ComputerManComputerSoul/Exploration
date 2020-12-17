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
	//MenuFrame��: �˵����� 
	private static final long serialVersionUID = 1L;
	private LoadingFrame loadingFrame;		//���ش���
	private JLayeredPane layeredPane;		//�㼶���
	
	private JButton newGameButton;		//������Ϸ
	private JButton continueGameButton;	//������Ϸ
	private JButton enterRoomButton;	//���뷿��
	private JButton exitButton;			//�˳���Ϸ
	private JButton updateLogButton;	//������־
	private JButton helpButton;			//����
	private JButton developerButton;	//������Ա
	private JButton settingButton;		//����
	private JButton returnButton;		//�������˵�
	private JButton sexChangeButton;	//�ı��Ա�
	private JLabel backgroundLabel;		//����ͼƬ
	private JLabel titleLabel;			//����ͼƬ
	private JLabel helpLabel;			//������ǩ
	private JLabel developerLabel;		//������Ա��ǩ
	private JLabel updateLogLabel;		//������־��ǩ
	private JLabel sexLabel;			//�Ա��ǩ
	private JScrollPane updateLogScrollPane;	//������־���������
	private JLabel volumeLabel;			//������ǩ
	private JSlider masterVolumeSlider;	//������������
	private JSlider bgmVolumeSlider;	//��������������
	private JSlider soundEffectVolumnVolumeSlider;	//��Ч����������
	
	private int status=0;	//��ǰ��ʾ״̬ 0��menu 1:update 2:help 3:developer 4:setting
	private int[] volumes;
	private int sex;
	private Sound sound = new Sound();	//����ѡ��
	private final String[] sexString = {"��","Ů"};
	
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
		
		newGameButton=getButton("������Ϸ",175, 380, 200, 50,25);
		continueGameButton=getButton("������Ϸ",610, 380, 200, 50,25);
		enterRoomButton=getButton("���뷿��",235, 470, 200, 50,25);
		exitButton=getButton("�˳���Ϸ",550, 470, 200, 50,25);
		updateLogButton=getButton("������־",25, 25, 130, 30,20);
		helpButton=getButton("����",55, 80, 100, 30,20);
		sexChangeButton=getButton("����",550, 300, 100, 30,20);
		developerButton=getButton("������Ա",835, 25, 130, 30,20);
		settingButton=getButton("����",835, 80, 100, 30,20);
		returnButton=getButton("����",835, 500, 100, 30,20);
		
		loadingFrame.setProgressValue(60);//
		
		helpLabel = new JLabel(Constant.HELP_TEXT);
		helpLabel.setFont(new Font("����", Font.BOLD, 20));
		helpLabel.setForeground(Color.white);
		helpLabel.setBounds(0, 0, 1000, 570);
		helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loadingFrame.setProgressValue(67);//
		
		developerLabel = new JLabel(Constant.DEVELOPER_TEXT);
		developerLabel.setFont(new Font("����", Font.BOLD, 20));
		developerLabel.setForeground(Color.white);
		developerLabel.setBounds(0, 0, 1000, 570);
		developerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loadingFrame.setProgressValue(75);//
		
		updateLogLabel = new JLabel(Constant.UPDATELOG_TEXT);
		updateLogLabel.setFont(new Font("����", Font.BOLD, 20));
		updateLogLabel.setForeground(Color.white);
		updateLogLabel.setBounds(200,35,600,520);
		updateLogLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		updateLogScrollPane = new JScrollPane(updateLogLabel);
		updateLogScrollPane.getVerticalScrollBar().setUnitIncrement(15);//����������
		updateLogScrollPane.getVerticalScrollBar().setOpaque(false);
		updateLogScrollPane.setBounds(170,0,640,595);
		updateLogScrollPane.setOpaque(false);
		updateLogScrollPane.getViewport().setOpaque(false);
		loadingFrame.setProgressValue(82);
		
		volumes=FileDataHandler.getVolumes();
		sex=FileDataHandler.getSex();
		
		sexLabel = new JLabel("��ǰ�Ա�: "+sexString[sex]);
		sexLabel.setFont(new Font("����", Font.BOLD, 20));
		sexLabel.setForeground(Color.white);
		sexLabel.setBounds(300, 300, 150, 30);
		sexLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		String text="<html>������<br/><br/><br/>&#12288����<br/><br/><br/>&#12288��Ч</html>";
		volumeLabel=new JLabel(text);
		volumeLabel.setFont(new Font("����",Font.BOLD, 20));
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
		
		ImageIcon icon = new ImageIcon("image/MenuFrame/icon.png");	//����ͼ��
        setIconImage(icon.getImage());
		addWindowListener(new WindowAdapter() {// ��Ӵ������
	        public void windowClosing(WindowEvent e) {// ����ر�ǰ
	        	System.exit(0);
	        }
	    });
		setContentPane(layeredPane);
		setResizable(false);
		loadingFrame.setProgressValue(99);
	}
	
	//������˵������
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
	
	//�Ƴ����˵������
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
	
	//��Ӹ�����־�����
	public void addUpdateLogComponent() {
		layeredPane.add(updateLogScrollPane,new Integer(300));
		layeredPane.add(returnButton,new Integer(300));
	}
		
	//�Ƴ�������־�����
	public void removeUpdateLogComponent() {
		layeredPane.remove(updateLogScrollPane);
		layeredPane.remove(returnButton);
	}
	
	//��Ӱ��������
	public void addHelpComponent() {
		layeredPane.add(helpLabel,new Integer(300));
		layeredPane.add(returnButton,new Integer(300));
	}
	
	//�Ƴ����������
	public void removeHelpComponent() {
		layeredPane.remove(helpLabel);
		layeredPane.remove(returnButton);
	}
    
	//��ӿ�����Ա�����
	public void addDeveloperComponent() {
		layeredPane.add(developerLabel,new Integer(300));
		layeredPane.add(returnButton,new Integer(300));
	}
	
	//�Ƴ�������Ա�����
	public void removeDeveloperComponent() {
		layeredPane.remove(developerLabel);
		layeredPane.remove(returnButton);
	}
	
	//������õ����
	public void addSettingComponent() {
		layeredPane.add(volumeLabel,new Integer(300));
		layeredPane.add(masterVolumeSlider,new Integer(300));
		layeredPane.add(bgmVolumeSlider,new Integer(300));
		layeredPane.add(soundEffectVolumnVolumeSlider,new Integer(300));
		layeredPane.add(sexLabel,new Integer(300));
		layeredPane.add(sexChangeButton,new Integer(300));
		layeredPane.add(returnButton,new Integer(300));
	}
	
	//�Ƴ����õ����
	public void removeSettingComponent() {
		layeredPane.remove(volumeLabel);
		layeredPane.remove(masterVolumeSlider);
		layeredPane.remove(bgmVolumeSlider);
		layeredPane.remove(soundEffectVolumnVolumeSlider);
		layeredPane.remove(sexLabel);
		layeredPane.remove(sexChangeButton);
		layeredPane.remove(returnButton);
	}		

	//����ʽ����һ������
	public JSlider getSlider(int unitValue) {
		JSlider slider=new JSlider(0,100,unitValue);
		slider.setMajorTickSpacing(20); // �������̶ȼ��
		slider.setMinorTickSpacing(5); // ���ôο̶ȼ��
		slider.setPaintTicks(true);  // ���� �̶� �� ��ǩ
		slider.setPaintLabels(true);
		slider.setFont(new Font("΢���ź�",Font.BOLD, 18));
		slider.setForeground(Color.white);
		slider.setOpaque(false);
		return slider;
	}
	
	//����ʽ����һ����ť
	public JButton getButton(String buttonName,int x,int y,int width,int height,int fontSize) {
		JButton button = new JButton(buttonName);
		button.setFont(new Font("����", Font.BOLD, fontSize));
		button.setForeground(Color.white);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setBounds(x, y, width, height);
		button.addActionListener(this);	
		return button;
	}
	
	//��ť���¼�����������
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
				new TextDialog(this,"��ȡʧ��",350,200,"��ȡʧ�ܣ�����Ϸ�����ļ�!");
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
			sexLabel.setText("��ǰ�Ա�: "+sexString[sex]);
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
	
	//��ʾ���沢��������
	public void visibleAndPlayBGM() {
		setVisible(true);
		sound.play("bgmMenu", true);
	}
	
	//���ؽ��沢ֹͣ��������
	public void invisibleAndStopBGM() {
		sound.stopPlay();
		setVisible(false);
	}
	
	//ѯ���Ƿ��˳���Ϸ
	public void whetherExit() {
		JDialog exitDialog = new JDialog(this,"��ʾ",true);
		exitDialog.setLayout(null);
		exitDialog.setSize(300, 180);
		exitDialog.setLocationRelativeTo(null);
		Container container = exitDialog.getContentPane(); // ����һ������
		
		JLabel exitLabel=new JLabel("�Ƿ��˳���Ϸ?");
		exitLabel.setFont(new Font("����", Font.BOLD, 20));
		exitLabel.setForeground(Color.BLACK);
		exitLabel.setHorizontalAlignment(SwingConstants.CENTER);
		exitLabel.setBounds(45,20,200,40);
		container.add(exitLabel); // ����������ӱ�ǩ
		
		JButton enterButton = new JButton("ȷ��");
		enterButton.setFont(new Font("����", Font.BOLD, 18));
		enterButton.setForeground(Color.BLACK);
		enterButton.setContentAreaFilled(false);
		enterButton.setFocusPainted(false);
		enterButton.setBounds(30, 100, 80, 30);
		enterButton.addActionListener(new ActionListener() { // Ϊ��ť�����굥���¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		container.add(enterButton);
		
		JButton cancelButton = new JButton("ȡ��");
		cancelButton.setFont(new Font("����", Font.BOLD, 18));
		cancelButton.setForeground(Color.BLACK);
		cancelButton.setContentAreaFilled(false);
		cancelButton.setFocusPainted(false);
		cancelButton.setBounds(180, 100, 80, 30);
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
	
}