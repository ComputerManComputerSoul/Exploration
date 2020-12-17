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
	//MyData��:��ɫ��������
	
	private Room room;
	private int x=Constant.INITIAL_X;	//��ɫx����
	private int y=Constant.INITIAL_Y;	//��ɫy����
	private final double fullPower=Constant.FULL_POWER;		//������ֵ
	private final int maxRoom = Constant.MAX_ROOM;	
	private NumberProcesser numberProcesser = new NumberProcesser();	//���ִ�����
	private ChangeableItemList[] changeableItemLists = new ChangeableItemList[maxRoom];
	private int direction;				//����
	private GameFrame gameFrame;
	private int RMB ;					//Ǯ
	private double power=fullPower;			//��ǰ����ֵ
	private Bag bag;	//����
	private int playerStatus = 5; //0:����ģʽ��ʼ�� 1:����ģʽ������Ϣ�׶� 2:����ģʽ��������Ϸ�׶�
								// 3:����ģʽ�ӷ��������Ϣ�׶�  4:����ģʽ�ӷ�����Ϸ�׶� 5:����ģʽ 6:������Ϸ����������
	private EnterRoomLAN enterRoomLAN;
	private EnterRoomWAN enterRoomWAN;
	private CreateRoomLAN createRoomLAN;
	private CreateRoomWAN createRoomWAN;
	
	//���캯��:��ʼ����Ա����ֵ
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
	
	//��ʼ����ɫ����
	public void initial() {
		x=Constant.INITIAL_X;
		y=Constant.INITIAL_Y;
		room = new Room(502);
		direction=0;
		RMB=0;
		bag = new Bag(this);
	}
	
	//���뷿��ģʽ
	public void enterRoomLANMode(EnterRoomLAN enterRoomLAN) {
		createRoomLAN = null;
		createRoomWAN = null;
		enterRoomWAN = null;
		this.enterRoomLAN=enterRoomLAN;
	}
	
	//��������ģʽ
	public void createRoomLANMode(CreateRoomLAN createRoomLAN) {
		enterRoomLAN = null;
		createRoomWAN = null;
		enterRoomWAN = null;
		this.createRoomLAN=createRoomLAN;
	}
	
	//��������ģʽ
	public void createRoomWANMode(CreateRoomWAN createRoomWAN) {
		enterRoomLAN = null;
		createRoomLAN = null;
		enterRoomWAN = null;
		this.createRoomWAN=createRoomWAN;
	}
	
	//���뷿��ģʽ
	public void enterRoomWANMode(EnterRoomWAN enterRoomWAN) {
		enterRoomLAN = null;
		createRoomLAN = null;
		createRoomWAN = null;
		this.enterRoomWAN=enterRoomWAN;
	}
	
	//�趨��Ϸģʽ
	public void setPlayerStatus(int targetStatus) {
		playerStatus = targetStatus;
	}
	
	//��ȡ��Ϸģʽ
	public int getPlayerStatus() {
		//System.out.println("playerStatus:"+playerStatus);
		return playerStatus;
	}

	//��ɫ�ƶ�  ����int����,����boolean�ƶ��Ƿ�ɹ�
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

	//��������
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
						String text="<html>����Ž�����,������<br/>Ҳ�޷���...</html>";
						new TextDialog(gameFrame,"��ʾ",250,180,text);
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
	
	//�ı��ǩ
	public void changeImageLabel(int labelNumber,int layer) {
		gameFrame.changeImageLabel(labelNumber,layer);
		if(playerStatus==2||playerStatus==4) sendMessage("4,"+room.getRoomNumber()+","+labelNumber+","+layer);
	}
	
	//�ı��ǩ
	public void changeImageLabel(int labelNumber,boolean isToShow) {
		changeImageLabel(labelNumber,isToShow?800:50);
	}
	
	//��ͬ�ı��ǩ
	public void changeImageLabelTogether(int roomNumber,int labelNumber,int layer) {
		if(roomNumber==room.getRoomNumber()) gameFrame.changeImageLabel(labelNumber,layer);
	}
	
	//����x
	public int getX() {
		return x;
	}
	
	//����y
	public int getY() {
		return y;
	}
	
	//����room
	public Room getRoom() {
		return room;
	}
	
	//��������
	public double getPower() {
		return power;
	}
	
	//���ط���
	public int getDirection() {
		return direction;
	}
	
	//�ı䷽��
	public void setDirection(int targetDirection) {
		direction=targetDirection;
	}
	
	//�ı�����
	public void changePower(double change) {
		if(change<0) {
			if(power+change>0) power+=change;
			else power=0;
		}
		else {
			if(power+change<fullPower) power+=change;
			else power=fullPower;
			gameFrame.showLeftMessage("�ָ����� "+(int)change+" ��");
			if(playerStatus==2||playerStatus==4) sendMessage("3,4,"+(int)change);
		}
		gameFrame.refreshPower();
	}
	
	//��ͬ��������
	public void gainPowerTogether(int change) {
		if(power+change<fullPower) power+=change;
		else power=fullPower;
		gameFrame.showLeftMessage("�ָ����� "+(int)change+" ��");
		gameFrame.refreshPower();
	}
	
	//�鿴ʣ�����Ǯ
	public int getRMB() {
		return RMB;
	}

	//����Ǯ
	public void addRMB(int getRMB) {
		RMB+=getRMB;
		if(playerStatus!=3) gameFrame.showLeftMessage("��� RMB * "+getRMB);
		gameFrame.refreshBagDialogMoney();
		if(playerStatus==2||playerStatus==4) sendMessage("3,3,"+getRMB);
	}
	
	//��Ǯ
	public boolean useRMB(int useRMB) {
		if(RMB>=useRMB) {
			RMB-=useRMB;
			gameFrame.showLeftMessage("���� RMB * "+useRMB);
			gameFrame.refreshBagDialogMoney();
			if(playerStatus==2||playerStatus==4) sendMessage("3,3,"+(-useRMB));
			return true;
		}
		else return false;
	}
	
	//��ͬ�ı�Ǯ
	public void changeRMBTogether(int changeMoney) {
		if(changeMoney>0) {
			RMB+=changeMoney;
			if(playerStatus!=3) {
				gameFrame.showLeftMessage("��� RMB * "+changeMoney);
				gameFrame.refreshBagDialogMoney();
			}
		}
		else if(changeMoney<0) {
			if(RMB+changeMoney>=0) {
				RMB+=changeMoney;
				gameFrame.showLeftMessage("���� RMB * "+(-changeMoney));
				gameFrame.refreshBagDialogMoney();
			}
		}
	}
	
	//��ñ���
 	public Bag getBag() {
		return bag;
	}
	
	//�ı䷿��������ֵ
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
	
	//�ı�ĳ���������ֵ
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
	
	//��ͬ�ı�ĳ���������ֵ
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
	
	//������Ϣ��������
	public LinkQueue getProgressQueue() {
		LinkQueue progressQueue = new LinkQueue();
		initCodesEnQueue(progressQueue);
		bagItemCodesEnQueue(progressQueue);
		changeableListCodesEnQueue(progressQueue);
		progressQueue.EnQueue("0,2");
		return progressQueue;
	}
	
	//������Ϸ����
	public void saveProgress() {
		FileDataHandler.saveProgress(getProgressQueue());
	}
	
	//������Ϸ����
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
	
	//��ʼ����Ϣ�����
	public void initCodesEnQueue(LinkQueue progressQueue) {
		progressQueue.EnQueue("3,0,"+room.getRoomNumber()+","+x+","+y+","+direction+","+RMB+","+(int)power);
	}
	
	//��������Ϣ�����
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
	
	
	//ȫ���������Ϣ�����
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
	
	//����ȫ���������Ϣ��
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
	
	//���ͱ�������Ϣ��
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
	
	//���ͳ�ʼ����Ϣ��
	public void sendInitCodes() {
		sendMessage("3,0,"+room.getRoomNumber()+","+x+","+y+","+direction+","+RMB+","+(int)power);
	}
	
	//������Ϣ�ķ���
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
	
	//������Ϣ�ķ���
	public void codeHandle(String readerMessage){
		/*messageType ��ȡ��Ϣ����: 
		 * 0:��������ͻ��˵Ĺ�����Ϣ
		 * 1:��������ͻ��˷����ĳ�ʼ������
		 * 2:��������ͻ��˷����ĸı䷿������
		 * 3:�ı�Է���ɫ����
		 * 4:�ı�Է���������
		 * 5:�ı�Է��������Լ���λ��
		 * 7:�ͻ�������������͵���Ϣ
		 * 8:��������ͻ��˷��͵���Ϣ
		 * 9:����
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
						case 1:		//0,1 �ͻ��˸��߷����myData������ɣ�׼����������
							if(playerStatus==1) {
								LinkQueue progressQueue = getProgressQueue();
								while(!progressQueue.QueueEmpty()) {
									sendMessage(progressQueue.DeQueue());
								}
							}
							break;
						case 2:		//0,2 ����˸��߿ͻ���������ȫ������
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
						case 3:		//0,3 �ͻ��˸��߷����������ȫ�����գ����Լ�����Ϸ
							if(playerStatus==1) {
								playerStatus=2;
								gameFrame.initFriendCharacter();
								if(createRoomLAN!=null) createRoomLAN.closeCreateRoomDialog();
								else if(createRoomWAN!=null) createRoomWAN.closeCreateRoomDialog();
							}
							break;
					}
					break;
				case 1:	//1,��Ʒ��,����
					bag.addItem(intMessges[1],intMessges[2]);
					break;
				case 2: //2,roomNumber,x,y,locationMessage	�ı䷿������
					changeLocationTogether(intMessges[1],intMessges[2],intMessges[3],intMessges[4]);
					break;
				case 3:
					switch(intMessges[1]) {
						case 0:		//3,0,roomNumber,x,y,direction,RMB,power ���ݳ�ʼ����ֵ
							if(playerStatus==3||playerStatus==6) {
								room = new Room(intMessges[2]);
								x=intMessges[3];
								y=intMessges[4];
								direction=intMessges[5];
								RMB=intMessges[6];
								power=intMessges[7];
							}
							break;
						case 1:		//3,1,itemNumber,����   ��ͬ�����Ʒ 
							bag.addItemTogether(intMessges[2],intMessges[3]);
							break;
						case 2:		//3,2,itemNumber,����   ��ͬ������Ʒ 
							bag.useItemTogether(intMessges[2],intMessges[3]);
							break;
						case 3:		//3,3,changeRMB ��ͬ�ı��Ǯ
							changeRMBTogether(intMessges[2]);
							break;
						case 4:		//3,4,gainPower	��ͬ��������
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
	
	//���GameFame
	public GameFrame getGameFrame() {
		return gameFrame;
	}
}
