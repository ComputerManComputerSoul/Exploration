package calculate;

import java.util.Arrays;
import service.NumberProcesser;
import view.BagDialog;
import view.GameFrame;

//������Ϊ�����Ʒ�ı��������д���˵��ߵ�ͷ�ڵ���������Ʒ������
/*���������������صĲ���*/

public class Bag{
	private final int AMax = Constant.A_MAX;	//A�������
	private final int BMax = Constant.B_MAX;	//B���鳤��
	private Dictionary dictionary = new Dictionary();
	private FoodDictionary foodDictionary = new FoodDictionary();
	private FoodCalculator foodCalculator = new FoodCalculator(this);
	private int BItemArray[] = new int[BMax];           //���ô��B����Ʒ������
	private AItemNode AItemHead = new AItemNode();   //A����������ͷ���
	private NumberProcesser numberProcesser = new NumberProcesser();	//���ִ�����
	private GameFrame gameFrame;
	private BagDialog bagDialog;
	private MyData myData;
	
	//���캯��:��ʼ��������Ϣ
	public Bag(MyData myData)
	{
		this.myData=myData;
		gameFrame=myData.getGameFrame();
		Arrays.fill(BItemArray,0);
	}
	
	//���پͲ�
	public void quickEat() {
		int calculatedBItems[] = foodCalculator.Calculate((int)myData.getPower());
		int usedAmount;
		for(int i=0;i<BMax;i++) {
			usedAmount=BItemArray[i]-calculatedBItems[i];
			if(usedAmount>0) {
				useItem(200+i,usedAmount);
				myData.changePower(usedAmount*foodDictionary.getFoodPower(200+i));
			}
		}
	}
	
	//�鿴�Ƿ�����Ʒ
	public boolean checkItem(int code) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//��Ϸ����
			for(AItemNode node = AItemHead.next;node!=null;node=node.next) {
				if(node.item==code) return true;
			}
			return false;
		}
		else if(itemKind==2) {		//����Ʒ
			if(BItemArray[itemNumber]>0) return true;
			else return false;
		}
		else return false;
	}
	
	//�鿴����Ʒ����
	public int getThisItemQuantity(int code) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//��Ϸ����
			for(AItemNode node = AItemHead.next;node!=null;node=node.next) {
				if(node.item==code) return 1;
			}
			return 0;
		}
		else if(itemKind==2) {		//����Ʒ
			return BItemArray[itemNumber];
		}
		else return -1;
	}
	
	//�����Ʒ
	public void addItem(int code) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//��Ϸ����
			if(!checkItem(code)) {
				AItemNode node = new AItemNode(code,AItemHead.next);
				AItemHead.next=node;
				if(myData.getPlayerStatus()!=3||myData.getPlayerStatus()!=6) gameFrame.showLeftMessage("��� "+getItemName(code));
				if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,1,"+code+",1");
			}
		}
		else if(itemKind==2) {		//����Ʒ
			BItemArray[itemNumber]++;
			if(myData.getPlayerStatus()!=3||myData.getPlayerStatus()!=6) gameFrame.showLeftMessage("��� "+getItemName(code)+" *1");
			if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,1,"+code+",1");
		}
	}
	
	//�����Ʒ
	public void addItem(int code,int quantity) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//��Ϸ����
			if(!checkItem(code)) {
				AItemNode node = new AItemNode(code,AItemHead.next);
				AItemHead.next=node;
				if(myData.getPlayerStatus()!=3&&myData.getPlayerStatus()!=6) gameFrame.showLeftMessage("��� "+getItemName(code));
				if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,1,"+code+","+quantity);
			}
		}
		else if(itemKind==2) {		//����Ʒ
			BItemArray[itemNumber]+=quantity;
			if(myData.getPlayerStatus()!=3&&myData.getPlayerStatus()!=6) gameFrame.showLeftMessage("��� "+getItemName(code)+" *"+quantity);
			if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,1,"+code+","+quantity);
		}
	}
	
	//��ͬ�����Ʒ
	public void addItemTogether(int code,int quantity) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//��Ϸ����
			if(!checkItem(code)) {
				AItemNode node = new AItemNode(code,AItemHead.next);
				AItemHead.next=node;
				if(myData.getPlayerStatus()!=3||myData.getPlayerStatus()!=6) gameFrame.showLeftMessage("��� "+getItemName(code));
			}
		}
		else if(itemKind==2) {		//����Ʒ
			BItemArray[itemNumber]+=quantity;
			if(myData.getPlayerStatus()!=3||myData.getPlayerStatus()!=6) gameFrame.showLeftMessage("��� "+getItemName(code)+" *"+quantity);
		}
	}
	
	//ʹ����Ʒ
	public boolean useItem(int code) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//��Ϸ����
			for(AItemNode node = AItemHead;node.next!=null;node=node.next) {
				if(node.next.item==code) {
					node.next=node.next.next;
					gameFrame.showLeftMessage("ʹ�� "+getItemName(code));
					if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,2,"+code+",1");
					return true;
				}
			}
			return false;
		}
		else if(itemKind==2) {		//����Ʒ
			if(BItemArray[itemNumber]>0) {
				BItemArray[itemNumber]--;
				gameFrame.showLeftMessage("���� "+getItemName(code)+" *1");
				if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,2,"+code+",1");
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	//ʹ����Ʒ
	public boolean useItem(int code,int quantity) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//��Ϸ����
			if(quantity!=1) return false;
			for(AItemNode node = AItemHead;node.next!=null;node=node.next) {
				if(node.next.item==code) {
					node.next=node.next.next;
					gameFrame.showLeftMessage("ʹ�� "+getItemName(code));
					if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,2,"+code+","+quantity);
					return true;
				}
			}
			return false;
		}
		else if(itemKind==2) {		//����Ʒ
			if(BItemArray[itemNumber]>=quantity) {
				BItemArray[itemNumber]-=quantity;
				gameFrame.showLeftMessage("���� "+getItemName(code)+" *"+quantity);
				if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,2,"+code+","+quantity);
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	//��ͬʹ����Ʒ
	public boolean useItemTogether(int code,int quantity) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//��Ϸ����
			if(quantity!=1) return false;
			for(AItemNode node = AItemHead;node.next!=null;node=node.next) {
				if(node.next.item==code) {
					node.next=node.next.next;
					gameFrame.showLeftMessage("ʹ�� "+getItemName(code));
					return true;
				}
			}
			return false;
		}
		else if(itemKind==2) {		//����Ʒ
			if(BItemArray[itemNumber]>=quantity) {
				BItemArray[itemNumber]-=quantity;
				gameFrame.showLeftMessage("���� "+getItemName(code)+" *"+quantity);
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	//�������Ʒ������
	public int getTotalItemKinds() {
		return getAItemKinds()+getBItemKinds();
	}
	
	//�����Ʒ��
	public String getItemName(int code) {
		return dictionary.getName(code);
	}
	
	//�����Ʒ����
	public String getItemDescription(int code) {
		return dictionary.getDescription(code);
	}
	
	//AItemNode�ڲ���: A��Ʒ ���ֻ�ɻ��һ�ε���Ʒ������ڵ㣬����ʵ���˻���Ԫ�صĴ�ȡ
	class AItemNode {
		//��Ʒ����Ŷ�Ӧ�����ŵ�λ��
		
		private int item;	//��Ʒ�����
		private AItemNode next;   //��һ�����
		
		//���캯������Ʒ��Ϣ�ĳ�ʼ��
		public AItemNode(int item){
			this.item = item;
			next=null;
		}
		
		//���캯������Ʒ��Ϣ�ĳ�ʼ��
		public AItemNode(int item,AItemNode next){
			this.item = item;
			this.next=next;
		}
		
		//���캯��������ͷ�ڵ�Ĺ���
		public AItemNode(){
			next = null;
		}
	}
		
	//��ȡ������A��Ʒ������
	public int getAItemKinds(){
		int length=0;
		for(AItemNode node = AItemHead;node.next!=null;node=node.next) {
			length++;
		}
		return length;
	}
	
	//����A������Ʒ
	public int[] getAItems(){
		int length=getAItemKinds();
		int[] AItems = new int[length];
		int i=0;
		for(AItemNode node = AItemHead.next;node!=null;node=node.next,i++) {
			AItems[i]=node.item;
		}
		return AItems;
	}
	
	//��ȡ������B��Ʒ������
	public int getBItemKinds(){
		int length=0;
		for(int i=0;i<BMax;i++) {
			if(BItemArray[i]>0) length++;
		}
		return length;
	}
		
	//����B������Ʒ
	public int[] getBItems(){
		int length=getBItemKinds();
		int[] BItems = new int[length];
		int j=0;
		for(int i=0;i<BMax;i++) {
			if(BItemArray[i]>0){ 
				BItems[j]=200+i;
				j++;
			}
		}
		return BItems;
	}

	//�ʵ��� ���ұ�������Ʒ��Ϣ�ֵ�
	class Dictionary {
		private String[] nameA=new String[AMax];
		private String[] descriptionA=new String[AMax];
		private String[] nameB=new String[AMax];
		private String[] descriptionB=new String[AMax];
		private NumberProcesser numberProcesser = new NumberProcesser();	//���ִ�����
		
		public Dictionary() {
			initialDictionary();
		}
		
		//�Ӵʵ��л�ȡ��Ʒ��
		public String getName(int code) {
			//System.out.println("getName: "+code);
			int itemKind=numberProcesser.headSplit(code);
			int itemNumber=numberProcesser.endSplit(code);
			if(itemKind==1) {			//��Ϸ����
				return nameA[itemNumber];
			}
			else if(itemKind==2) {		//����Ʒ
				return nameB[itemNumber];
			}
			else return null;
		}
		
		//�Ӵʵ��л�ȡ��Ʒ����
		public String getDescription(int code) {
			//System.out.println("getDescription: "+code);
			int itemKind=numberProcesser.headSplit(code);
			int itemNumber=numberProcesser.endSplit(code);
			if(itemKind==1) {			//��Ϸ����
				return descriptionA[itemNumber];
			}
			else if(itemKind==2) {		//����Ʒ
				return descriptionB[itemNumber];
			}
			else return null;
		}
		
		//�ڴʵ�����Ӵ���
		public void addDirectoryItem(int code,String name,String description) {
			int itemKind=numberProcesser.headSplit(code);
			int itemNumber=numberProcesser.endSplit(code);
			if(itemKind==1) {			//��Ϸ����
				if(itemNumber<AMax) {
					nameA[itemNumber]=name;
					descriptionA[itemNumber]=description;
				}
				else {
					System.out.println("����1��Ʒ�ʵ䳬��");
				}
			}
			else if(itemKind==2) {		//����Ʒ
				if(itemNumber<BMax) {
					nameB[itemNumber]=name;
					descriptionB[itemNumber]=description;
				}
				else {
					System.out.println("����2��Ʒ�ʵ䳬��");
				}
			}
		}
		
		//��ʼ���ʵ�
		public void initialDictionary() {
		////////////////////////////////////////////////////////////////////////////////////////////////////
			addDirectoryItem(100,"��ѧ�����-����˼ά����","��ѧ�����-����˼ά����,���Ϊ���Ƶ���,"
					+ "�������֮Ϊ����ƻ���,�ɹ�����ս�³����ڱ�д,�����ɹ����������������ôѧ,��ֻ�ܻش���:"
					+ "��ս��,������!");
			addDirectoryItem(200,"ũ��ɽȪ","ũ��ɽȪ,�е���!");
			addDirectoryItem(101,"�ߵ���ѧ(���߰�)�ϲ�","һ�����Ʋ����ĸߵ���ѧ�̲ģ���¼����������������ս��...");
			addDirectoryItem(102,"����ֽ��","һ���Źֵ�Сֽ��������д�š������͡�416.00��������Ĺĵ�...");
		}
	}
}