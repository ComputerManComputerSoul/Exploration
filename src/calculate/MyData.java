package calculate;

import service.CreateRoomLAN;
import service.CreateRoomWAN;
import service.EnterRoomLAN;
import service.EnterRoomWAN;
import service.FileDataHandler;
import service.NumberProcesser;
import view.GameFrame;
import view.TextDialog;

public class MyData {
	//MyData类:角色个人数据
	
	private Room room;
	private int x=Constant.INITIAL_X;	//角色x坐标
	private int y=Constant.INITIAL_Y;	//角色y坐标
	private final double fullPower=Constant.FULL_POWER;		//满体力值
	private final int maxRoom = Constant.MAX_ROOM;	
	private NumberProcesser numberProcesser = new NumberProcesser();	//数字处理器
	private ChangeableItemList[] changeableItemLists = new ChangeableItemList[maxRoom];
	private int direction;				//方向
	private GameFrame gameFrame;
	private int RMB ;					//钱
	private double power=fullPower;			//当前体力值
	private Bag bag;	//背包
	private int playerStatus = 5; //0:单机模式初始化 1:联机模式发送信息阶段 2:联机模式开房间游戏阶段
								// 3:联机模式加房间接收信息阶段  4:联机模式加房间游戏阶段 5:单机模式 6:继续游戏传入数据中
	private EnterRoomLAN enterRoomLAN;
	private EnterRoomWAN enterRoomWAN;
	private CreateRoomLAN createRoomLAN;
	private CreateRoomWAN createRoomWAN;
	
	//构造函数:初始化成员变量值
	public MyData(GameFrame gameFrame,int[][] map,int x,int y,int z,Bag bag){
		this.bag=bag;
		this.x=x;
		this.y=y;
	}
	public MyData(GameFrame gameFrame,int playerStatus) {
		this.gameFrame=gameFrame;
		this.playerStatus=playerStatus;
		initial();
	}
	
	//初始化角色数据
	public void initial() {
		x=Constant.INITIAL_X;
		y=Constant.INITIAL_Y;
		room = new Room(502);
		direction=0;
		RMB=0;
		bag = new Bag(this);
	}
	
	//加入房间模式
	public void enterRoomLANMode(EnterRoomLAN enterRoomLAN) {
		createRoomLAN = null;
		createRoomWAN = null;
		enterRoomWAN = null;
		this.enterRoomLAN=enterRoomLAN;
	}
	
	//创建房间模式
	public void createRoomLANMode(CreateRoomLAN createRoomLAN) {
		enterRoomLAN = null;
		createRoomWAN = null;
		enterRoomWAN = null;
		this.createRoomLAN=createRoomLAN;
	}
	
	//创建房间模式
	public void createRoomWANMode(CreateRoomWAN createRoomWAN) {
		enterRoomLAN = null;
		createRoomLAN = null;
		enterRoomWAN = null;
		this.createRoomWAN=createRoomWAN;
	}
	
	//加入房间模式
	public void enterRoomWANMode(EnterRoomWAN enterRoomWAN) {
		enterRoomLAN = null;
		createRoomLAN = null;
		createRoomWAN = null;
		this.enterRoomWAN=enterRoomWAN;
	}
	
	//设定游戏模式
	public void setPlayerStatus(int targetStatus) {
		playerStatus = targetStatus;
	}
	
	//获取游戏模式
	public int getPlayerStatus() {
		//System.out.println("playerStatus:"+playerStatus);
		return playerStatus;
	}

	//角色移动  输入int方向,返回boolean移动是否成功
	public boolean move(int direction) {
		switch (direction){
			case 0:
				if(numberProcesser.headSplit(room.seeLocation(x,y-1))!=1) return false;
				else {
					y--;
					return true;
				}
			case 1:
				if(numberProcesser.headSplit(room.seeLocation(x,y+1))!=1) return false;
				else{
					y++;
					return true;
				}
			case 2:
				if(numberProcesser.headSplit(room.seeLocation(x-1,y))!=1)  return false;
				else {
					x--;
					return true;
				}
			case 3:
				if(numberProcesser.headSplit(room.seeLocation(x+1,y))!=1)  return false;
				else {
					x++;
					return true;
				}
			default:
				return false;
		}
	}

	//访问坐标
	public void visit() {
		int visitX=x;
		int visitY=y;
		switch(direction){
			case 0:
				visitY--;
				break;
			case 1:
				visitY++;
				break;
			case 2:
				visitX--;
				break;
			case 3:
				visitX++;
				break;
		}
		int leftLessage=room.seeLocation(visitX,visitY);
		System.out.println("visit:"+visitX+","+visitY+":"+leftLessage);
		int head=numberProcesser.headSplit(leftLessage);
		leftLessage=numberProcesser.endSplit(leftLessage);
////////////////////////////////////////////////////////////////////////////////////////////////////
		switch(head) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				switch(leftLessage) {
					case 1:
						changeImageLabel(35,false);
						bag.addItem(101);
						changeLocation(visitX,visitY,201);
						break;
					case 2:
						changeImageLabel(36,false);
						bag.addItem(102);
						changeLocation(visitX,visitY,202);
						break;
					case 3:
						break;
					default:
						break;
				}
				break;
			case 5:
				head=numberProcesser.headSplit(leftLessage);
				leftLessage=numberProcesser.endSplit(leftLessage);
				switch(head) {
					case 1:
						String text="<html>这个门紧锁着,再用力<br/>也无法打开...</html>";
						new TextDialog(gameFrame,"提示",250,180,text);
						break;
					case 2:
						break;
					case 3:
						break;
					default:
						break;
				}
			default:
				break;
		}
	}
	
	//改变标签
	public void changeImageLabel(int labelNumber,int layer) {
		gameFrame.changeImageLabel(labelNumber,layer);
		if(playerStatus==2||playerStatus==4) sendMessage("4,"+room.getRoomNumber()+","+labelNumber+","+layer);
	}
	
	//改变标签
	public void changeImageLabel(int labelNumber,boolean isToShow) {
		changeImageLabel(labelNumber,isToShow?800:50);
	}
	
	//共同改变标签
	public void changeImageLabelTogether(int roomNumber,int labelNumber,int layer) {
		if(roomNumber==room.getRoomNumber()) gameFrame.changeImageLabel(labelNumber,layer);
	}
	
	//返回x
	public int getX() {
		return x;
	}
	
	//返回y
	public int getY() {
		return y;
	}
	
	//返回room
	public Room getRoom() {
		return room;
	}
	
	//返回体力
	public double getPower() {
		return power;
	}
	
	//返回方向
	public int getDirection() {
		return direction;
	}
	
	//改变方向
	public void setDirection(int targetDirection) {
		direction=targetDirection;
	}
	
	//改变体力
	public void changePower(double change) {
		if(change<0) {
			if(power+change>0) power+=change;
			else power=0;
		}
		else {
			if(power+change<fullPower) power+=change;
			else power=fullPower;
			gameFrame.showLeftMessage("恢复体力 "+(int)change+" 点");
			if(playerStatus==2||playerStatus==4) sendMessage("3,4,"+(int)change);
		}
		gameFrame.refreshPower();
	}
	
	//共同增加体力
	public void gainPowerTogether(int change) {
		if(power+change<fullPower) power+=change;
		else power=fullPower;
		gameFrame.showLeftMessage("恢复体力 "+(int)change+" 点");
		gameFrame.refreshPower();
	}
	
	//查看剩余多少钱
	public int getRMB() {
		return RMB;
	}

	//增加钱
	public void addRMB(int getRMB) {
		RMB+=getRMB;
		if(playerStatus!=3) gameFrame.showLeftMessage("获得 RMB * "+getRMB);
		gameFrame.refreshBagDialogMoney();
		if(playerStatus==2||playerStatus==4) sendMessage("3,3,"+getRMB);
	}
	
	//花钱
	public boolean useRMB(int useRMB) {
		if(RMB>=useRMB) {
			RMB-=useRMB;
			gameFrame.showLeftMessage("消耗 RMB * "+useRMB);
			gameFrame.refreshBagDialogMoney();
			if(playerStatus==2||playerStatus==4) sendMessage("3,3,"+(-useRMB));
			return true;
		}
		else return false;
	}
	
	//共同改变钱
	public void changeRMBTogether(int changeMoney) {
		if(changeMoney>0) {
			RMB+=changeMoney;
			if(playerStatus!=3) {
				gameFrame.showLeftMessage("获得 RMB * "+changeMoney);
				gameFrame.refreshBagDialogMoney();
			}
		}
		else if(changeMoney<0) {
			if(RMB+changeMoney>=0) {
				RMB+=changeMoney;
				gameFrame.showLeftMessage("消耗 RMB * "+(-changeMoney));
				gameFrame.refreshBagDialogMoney();
			}
		}
	}
	
	//获得背包
 	public Bag getBag() {
		return bag;
	}
	
	//改变房间中坐标值
	public void changeLocation(int x,int y,int targetNumber) {
		if(playerStatus==2||playerStatus==4) sendMessage("2,"+room.getRoomNumber()+","+x+","+y+","+targetNumber);
		room.changeLocation(x, y, targetNumber);
		int saveNullNumber=-1;
		for(int i=0;i<maxRoom;i++) {
			if(changeableItemLists[i]==null) {
				if(saveNullNumber==-1) saveNullNumber=i;
			}
			else {
				if(changeableItemLists[i].getRoomNumber()==room.getRoomNumber()) {
					changeableItemLists[i].addItem(x, y, targetNumber);
					return;
				}
			}
		}
		changeableItemLists[saveNullNumber] = new ChangeableItemList(room.getRoomNumber());
		changeableItemLists[saveNullNumber].addItem(x, y, targetNumber);
	}
	
	//改变某房间的坐标值
	public void changeLocation(int roomNumber,int x,int y,int targetNumber) {
		if(playerStatus==2||playerStatus==4) sendMessage("2,"+roomNumber+","+x+","+y+","+targetNumber);
		if(room.getRoomNumber()==roomNumber) room.changeLocation(x, y, targetNumber);
		int saveNullNumber=-1;
		for(int i=0;i<maxRoom;i++) {
			if(changeableItemLists[i]==null) {
				if(saveNullNumber==-1) saveNullNumber=i;
			}
			else {
				if(changeableItemLists[i].getRoomNumber()==roomNumber) {
					changeableItemLists[i].addItem(x, y, targetNumber);
					return;
				}
			}
		}
		changeableItemLists[saveNullNumber] = new ChangeableItemList(roomNumber);
		changeableItemLists[saveNullNumber].addItem(x, y, targetNumber);
	}
	
	//共同改变某房间的坐标值
	public void changeLocationTogether(int roomNumber,int x,int y,int targetNumber) {
		if(room.getRoomNumber()==roomNumber) room.changeLocation(x, y, targetNumber);
		int saveNullNumber=-1;
		for(int i=0;i<maxRoom;i++) {
			if(changeableItemLists[i]==null) {
				if(saveNullNumber==-1) saveNullNumber=i;
			}
			else {
				if(changeableItemLists[i].getRoomNumber()==roomNumber) {
					changeableItemLists[i].addItem(x, y, targetNumber);
					return;
				}
			}
		}
		changeableItemLists[saveNullNumber] = new ChangeableItemList(roomNumber);
		changeableItemLists[saveNullNumber].addItem(x, y, targetNumber);
	}
	
	//进度信息码对列填充
	public LinkQueue getProgressQueue() {
		LinkQueue progressQueue = new LinkQueue();
		initCodesEnQueue(progressQueue);
		bagItemCodesEnQueue(progressQueue);
		changeableListCodesEnQueue(progressQueue);
		progressQueue.EnQueue("0,2");
		return progressQueue;
	}
	
	//保存游戏进度
	public void saveProgress() {
		FileDataHandler.saveProgress(getProgressQueue());
	}
	
	//保存游戏进度
	public void loadProgress() {
		LinkQueue progressQueue = FileDataHandler.loadProgress();
		String s;
		while(!progressQueue.QueueEmpty()) {
			s=progressQueue.DeQueue();
			if(!s.equals("0,2")) {
				codeHandle(s);
			}
		}
	}
	
	//初始化信息码入队
	public void initCodesEnQueue(LinkQueue progressQueue) {
		progressQueue.EnQueue("3,0,"+room.getRoomNumber()+","+x+","+y+","+direction+","+RMB+","+(int)power);
	}
	
	//背包的信息码入队
	public void bagItemCodesEnQueue(LinkQueue progressQueue) {
		int[] AItems = bag.getAItems();
		int[] BItems = bag.getBItems();
		int AKinds = bag.getAItemKinds();
		int BKinds = bag.getBItemKinds();
		int itemNumber = 0;
		for(itemNumber=0;itemNumber<AKinds;itemNumber++) {
			progressQueue.EnQueue("1,"+AItems[itemNumber]+",1");
		}
		for(;itemNumber<AKinds+BKinds;itemNumber++) {
			progressQueue.EnQueue("1,"+BItems[itemNumber-AKinds]+","+bag.getThisItemQuantity(BItems[itemNumber-AKinds]));
		}
	}
	
	
	//全部链表的信息码入队
	public void changeableListCodesEnQueue(LinkQueue progressQueue) {
		for(int i=0;i<maxRoom;i++) {
			if(changeableItemLists[i]!=null) {
				int maxj=changeableItemLists[i].getAmount();
				String[] tempChangeableListCodes = changeableItemLists[i].getCodes();
				for(int j=0;j<maxj;j++) {
					progressQueue.EnQueue(tempChangeableListCodes[j]);
				}
			}
		}
	}
	
	//发送全部链表的信息码
	public void sendChangeableListCodes() {
		if(playerStatus!=1) return;
		for(int i=0;i<maxRoom;i++) {
			if(changeableItemLists[i]!=null) {
				int maxj=changeableItemLists[i].getAmount();
				String[] tempChangeableListCodes = changeableItemLists[i].getCodes();
				for(int j=0;j<maxj;j++) {
					sendMessage(tempChangeableListCodes[j]);
				}
			}
		}
	}
	
	//发送背包的信息码
	public void sendBagItemCodes() {
		int[] AItems = bag.getAItems();
		int[] BItems = bag.getBItems();
		int AKinds = bag.getAItemKinds();
		int BKinds = bag.getBItemKinds();
		int itemNumber = 0;
		for(itemNumber=0;itemNumber<AKinds;itemNumber++) {
			sendMessage("1,"+AItems[itemNumber]+",1");
		}
		for(;itemNumber<AKinds+BKinds;itemNumber++) {
			sendMessage("1,"+BItems[itemNumber-AKinds]+","+bag.getThisItemQuantity(BItems[itemNumber-AKinds]));
		}
	}
	
	//发送初始化信息码
	public void sendInitCodes() {
		sendMessage("3,0,"+room.getRoomNumber()+","+x+","+y+","+direction+","+RMB+","+(int)power);
	}
	
	//发送信息的方法
	public void sendMessage(String message) {
		switch(playerStatus) {
			case 0:
			case 5:
				break;
			case 1:
			case 2:
				if(createRoomWAN==null) createRoomLAN.sendMessage(message);
				else if(createRoomLAN==null) createRoomWAN.sendMessage(message);
				break;
			case 3:
			case 4:
				if(enterRoomWAN==null) enterRoomLAN.sendMessage(message);
				else if(enterRoomLAN==null) enterRoomWAN.sendMessage(message);
				break;
		}
	}
	
	//处理信息的方法
	public void codeHandle(String readerMessage){
		/*messageType 读取信息类型: 
		 * 0:服务器与客户端的功能信息
		 * 1:服务器向客户端发出的初始化数据
		 * 2:服务器向客户端发出的改变房间数据
		 * 3:改变对方角色数据
		 * 4:改变对方房间数据
		 * 5:改变对方房间中自己的位置
		 * 7:客户端向服务器发送的消息
		 * 8:服务器向客户端发送的信息
		 * 9:聊天
		**/
		String splitMessges[] = readerMessage.split(",");
		if(Integer.parseInt(splitMessges[0])!=9)
		{		
			int intMessges[] = new int[splitMessges.length];
			for(int i=0;i<intMessges.length;i++) {
				intMessges[i]=Integer.parseInt(splitMessges[i]);
			}
			switch(intMessges[0]) {
				case 0:
					switch(intMessges[1]) {
						case 1:		//0,1 客户端告诉服务端myData创建完成，准备接收数据
							if(playerStatus==1) {
								LinkQueue progressQueue = getProgressQueue();
								while(!progressQueue.QueueEmpty()) {
									sendMessage(progressQueue.DeQueue());
								}
							}
							break;
						case 2:		//0,2 服务端告诉客户端数据已全部发送
							if(playerStatus==3) {
								playerStatus=4;
								gameFrame.initGameFrame();
								gameFrame.initFriendCharacter();
								if(enterRoomLAN!=null) enterRoomLAN.enterGame();
								else if(enterRoomWAN!=null) enterRoomWAN.enterGame();
								gameFrame.visible();
								sendMessage("0,3");
							}
							break;
						case 3:		//0,3 客户端告诉服务端数据已全部接收，可以继续游戏
							if(playerStatus==1) {
								playerStatus=2;
								gameFrame.initFriendCharacter();
								if(createRoomLAN!=null) createRoomLAN.closeCreateRoomDialog();
								else if(createRoomWAN!=null) createRoomWAN.closeCreateRoomDialog();
							}
							break;
					}
					break;
				case 1:	//1,物品号,数量
					bag.addItem(intMessges[1],intMessges[2]);
					break;
				case 2: //2,roomNumber,x,y,locationMessage	改变房间数据
					changeLocationTogether(intMessges[1],intMessges[2],intMessges[3],intMessges[4]);
					break;
				case 3:
					switch(intMessges[1]) {
						case 0:		//3,0,roomNumber,x,y,direction,RMB,power 传递初始人物值
							if(playerStatus==3||playerStatus==6) {
								room = new Room(intMessges[2]);
								x=intMessges[3];
								y=intMessges[4];
								direction=intMessges[5];
								RMB=intMessges[6];
								power=intMessges[7];
							}
							break;
						case 1:		//3,1,itemNumber,数量   共同获得物品 
							bag.addItemTogether(intMessges[2],intMessges[3]);
							break;
						case 2:		//3,2,itemNumber,数量   共同减少物品 
							bag.useItemTogether(intMessges[2],intMessges[3]);
							break;
						case 3:		//3,3,changeRMB 共同改变金钱
							changeRMBTogether(intMessges[2]);
							break;
						case 4:		//3,4,gainPower	共同增加体力
							gainPowerTogether(intMessges[2]);
							break;
					}
					break;
				case 4:	//4,roomNumber,imageNumber,layer
					changeImageLabelTogether(intMessges[1],intMessges[2],intMessges[3]);
					break;
				case 5:	
					switch(intMessges[1]) {
						case 0:		//5,0,direction
							gameFrame.refreshFriendDirection(intMessges[2]);
							break;
						case 1:		//5,1,roomNumber,direction,x,y,intSpeed,speedAfterPoint
							gameFrame.refreshFriendRunLocation(intMessges[2],intMessges[3],intMessges[4],intMessges[5],intMessges[6],intMessges[7]);
							break;
					}
					break;
			}
		}
		else {
			gameFrame.receiveChatMessage(splitMessges[1]);
		}
	}
	
	//获得GameFame
	public GameFrame getGameFrame() {
		return gameFrame;
	}
}
