package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import calculate.*;
import service.NumberProcesser;

public class BagDialog extends JDialog{  
	//BagDialog类：背包窗口
	private static final long serialVersionUID = 1L;
	private int[] items = new int[Math.max(Constant.A_MAX,Constant.B_MAX)];
	private int itemKind=0; 
	private GameFrame gameFrame;
	private MyData myData;
	private JPanel Panel = new JPanel();
	private JScrollPane ScrollPane;
	private JButton[] buttons = new JButton[Math.max(Constant.A_MAX,Constant.B_MAX)];
	private JButton buttonA = new JButton("道具");
	private JButton buttonB = new JButton("消耗品");
	private JButton useButton = new JButton("使用");
	private int choosenItem;
	private JButton choosenButton;
	private JLabel nameLabel;
	private JLabel photoLabel;
	private JLabel descriptionLabel;
	private JLabel moneyLabel;
	private FoodDictionary foodDictionary = new FoodDictionary();
	private NumberProcesser numberProcesser = new NumberProcesser();	//数字处理器
	private Bag bag;
	
	
	public BagDialog(GameFrame frame){
		super(frame,"背包", true);
		gameFrame=frame;
		myData=gameFrame.getMyData();
		bag=myData.getBag();
		
		setSize(1000, 800);
		setLocationRelativeTo(null);
		setLayout(null);
		Container container = getContentPane(); // 创建一个容器
		
		JLabel jl=new JLabel("道具");
		jl.setFont(new Font("宋体", Font.BOLD, 20));
		jl.setForeground(Color.BLACK);
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		jl.setBounds(20,20,100,30);
		container.add(jl); // 在容器中添加标签
		
		buttonA.setFont(new Font("宋体", Font.BOLD, 20));
		buttonA.setForeground(Color.BLACK);
		buttonA.setHorizontalAlignment(SwingConstants.CENTER);
		buttonA.setBounds(140,20,100,30);
		buttonA.setBackground(Color.LIGHT_GRAY);
		buttonA.setFocusPainted(false);
		buttonA.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonA.setBackground(Color.LIGHT_GRAY);
				buttonB.setBackground(Color.WHITE);
				jl.setText("道具");
				getAItems();
			}
		});
		container.add(buttonA); 
		
		buttonB.setFont(new Font("宋体", Font.BOLD, 20));
		buttonB.setForeground(Color.BLACK);
		buttonB.setHorizontalAlignment(SwingConstants.CENTER);
		buttonB.setBounds(260,20,100,30);
		buttonB.setBackground(Color.WHITE);
		buttonB.setFocusPainted(false);
		buttonB.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonA.setBackground(Color.WHITE);
				buttonB.setBackground(Color.LIGHT_GRAY);
				jl.setText("消耗品");
				getBItems();
			}
		});
		container.add(buttonB); 
		
		moneyLabel=new JLabel("RMB:"+myData.getRMB());
		moneyLabel.setFont(new Font("宋体", Font.BOLD, 20));
		moneyLabel.setForeground(Color.BLACK);
		moneyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		moneyLabel.setBounds(380,20,100,30);
		container.add(moneyLabel); // 在容器中添加标签
		
		nameLabel=new JLabel();
		nameLabel.setFont(new Font("宋体", Font.BOLD, 20));
		nameLabel.setForeground(Color.BLACK);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setBounds(550,30,400,50);
		nameLabel.setBackground(Color.LIGHT_GRAY);
		nameLabel.setOpaque(true);
		container.add(nameLabel); // 在容器中添加标签
		
		photoLabel=new JLabel();
		photoLabel.setBounds(550,100,400,300);
		photoLabel.setBackground(Color.LIGHT_GRAY);
		photoLabel.setOpaque(true);
		photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		container.add(photoLabel); // 在容器中添加标签
		
		descriptionLabel=new JLabel();
		descriptionLabel.setFont(new Font("宋体", Font.BOLD, 20));
		descriptionLabel.setForeground(Color.BLACK);
		descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		descriptionLabel.setBounds(550,420,400,300);
		descriptionLabel.setBackground(Color.LIGHT_GRAY);
		descriptionLabel.setOpaque(true);
		container.add(descriptionLabel); // 在容器中添加标签
		
		useButton.setFont(new Font("宋体", Font.BOLD, 20));
		useButton.setForeground(Color.BLACK);
		useButton.setHorizontalAlignment(SwingConstants.CENTER);
		useButton.setBounds(851,725,100,30);
		useButton.setBackground(Color.WHITE);
		useButton.setFocusPainted(false);
		useButton.addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println("choosenItem="+choosenItem);
				if(choosenItem==0) {
					new TextDialog(gameFrame,"提示",300,200,"请选择要使用的物品!");
				}
				else {
					if(numberProcesser.headSplit(choosenItem)==2) {
						bag.useItem(choosenItem);
						int gainPower=foodDictionary.getFoodPower(choosenItem);
						myData.changePower(gainPower);
						
						if(bag.getThisItemQuantity(choosenItem)>0) {
							choosenButton.setText(bag.getItemName(choosenItem)+" * "+bag.getThisItemQuantity(choosenItem));
						}
						else {
							getBItems();
						}
					}
					else if(numberProcesser.headSplit(choosenItem)==1){
						boolean itemLose=true;
/////////////////////////////////////////////////////////////////////////////////////////////////////
						switch(numberProcesser.endSplit(choosenItem)) {
							case 2:
								bag.useItem(choosenItem);
								myData.addRMB(416);
								break;
								
								
								
								
								
							default:
								itemLose=false;
								new TextDialog(gameFrame,"提示",300,200,"使用失败");
								break;
							
						}
						if(itemLose) getAItems();
					}
					else {
						new TextDialog(gameFrame,"提示",300,200,"请选择要使用的物品!");
					}
				}
			}
		});
		container.add(useButton); 
		
		Panel.setLayout(null);
		Panel.setBounds(30,60,500,680);
		
		ScrollPane= new JScrollPane(Panel);
		container.add(Panel);
		
		ScrollPane.setLayout(null);
		ScrollPane.setBounds(30,60,500,680);
		ScrollPane.getVerticalScrollBar().setUnitIncrement(25);//设置灵敏度
		container.add(ScrollPane); // 在容器中添加标签
		
		getAItems();
		container.setBackground (Color.white);
		setResizable(false);
	}
	
	//刷新面板显示AItem
	public void getAItems() {
		Panel.removeAll();
		Panel.repaint();
		refreshRMB();
		itemKind=bag.getAItemKinds();
		items=bag.getAItems();
		//System.out.println("AItemKinds:"+itemKind);
		for(int i=0;i<itemKind;i++) {
			buttons[i]=new JButton(bag.getItemName(items[i]));
			final int fi=i;
			buttons[i].addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
				@Override
				public void actionPerformed(ActionEvent e) {
					choosenItem=items[fi];
					choosenButton=buttons[fi];
					visitItem(items[fi]);
				}
			});
			buttons[i].setFont(new Font("宋体", Font.BOLD, 20));
			buttons[i].setBackground(Color.WHITE);
			buttons[i].setFocusPainted(false);
			buttons[i].setForeground(Color.BLACK);
			buttons[i].setHorizontalAlignment(SwingConstants.CENTER);
			buttons[i].setBounds(15,10+i*50,470,50);
			Panel.add(buttons[i]);
		}
		Panel.setBounds(30,60,500,Math.max(680,20+itemKind*50));
		
		nameLabel.setText("");
		photoLabel.setIcon(null);
		descriptionLabel.setText("");
		choosenItem=0;
		Panel.revalidate();
	}
	
	//刷新面板显示BItem
	public void getBItems() {
		Panel.removeAll();
		Panel.repaint();
		refreshRMB();
		itemKind=bag.getBItemKinds();
		items=bag.getBItems();
		//System.out.println("BItemKinds:"+itemKind);
		for(int i=0;i<itemKind;i++) {
			buttons[i]=new JButton(bag.getItemName(items[i])+" * "+bag.getThisItemQuantity(items[i]));
			final int finalItem=items[i];
			buttons[i].addActionListener(new ActionListener() { // 为按钮添加鼠标单击事件
				@Override
				public void actionPerformed(ActionEvent e) {
					choosenItem=finalItem;
					visitItem(finalItem);
				}
			});
			buttons[i].setFont(new Font("宋体", Font.BOLD, 20));
			buttons[i].setBackground(Color.WHITE);
			buttons[i].setFocusPainted(false);
			buttons[i].setForeground(Color.BLACK);
			buttons[i].setHorizontalAlignment(SwingConstants.CENTER);
			buttons[i].setBounds(15,10+i*50,470,50);
			Panel.add(buttons[i]);
		}
		Panel.setBounds(30,60,500,Math.max(680,20+itemKind*50));
		
		nameLabel.setText("");
		photoLabel.setIcon(null);
		descriptionLabel.setText("");
		choosenItem=0;
		Panel.revalidate();
	}
	
	//查看物品
	public void visitItem(int code) {
		nameLabel.setText(bag.getItemName(code));
		int lineLength=17;
		String tempDescription=bag.getItemDescription(code);
		String resultDescription="<html>";
		for(int totalLength = tempDescription.length();totalLength>lineLength;totalLength-=lineLength) {
			resultDescription+=tempDescription.substring(0, lineLength);
			tempDescription=tempDescription.substring(lineLength);
			resultDescription+="<br/>";
		}
		resultDescription+=tempDescription;
		resultDescription+="</html>";
		descriptionLabel.setText(resultDescription);
		//System.out.println("image/itemPhotoPng/"+code+".png");
		ImageIcon photoIcon = new ImageIcon("image/itemPhotoPng/"+code+".png");
		photoIcon.setImage(photoIcon.getImage().getScaledInstance(200,280,Image.SCALE_DEFAULT));
		photoLabel.setIcon(photoIcon);
	}
	
	//刷新RMB
	public void refreshRMB() {
		moneyLabel.setText("RMB:"+myData.getRMB());
	}
	
}