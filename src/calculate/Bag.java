package calculate;

import java.util.Arrays;
import service.NumberProcesser;
import view.BagDialog;
import view.GameFrame;

//；该类为存放物品的背包，其中存放了道具的头节点与存放消耗品的数组
/*存放链表与数组相关的操作*/

public class Bag{
	private final int AMax = Constant.A_MAX;	//A最大数量
	private final int BMax = Constant.B_MAX;	//B数组长度
	private Dictionary dictionary = new Dictionary();
	private FoodDictionary foodDictionary = new FoodDictionary();
	private FoodCalculator foodCalculator = new FoodCalculator(this);
	private int BItemArray[] = new int[BMax];           //设置存放B类物品的数组
	private AItemNode AItemHead = new AItemNode();   //A类道具链表的头结点
	private NumberProcesser numberProcesser = new NumberProcesser();	//数字处理器
	private GameFrame gameFrame;
	private BagDialog bagDialog;
	private MyData myData;
	
	//构造函数:初始化背包信息
	public Bag(MyData myData)
	{
		this.myData=myData;
		gameFrame=myData.getGameFrame();
		Arrays.fill(BItemArray,0);
	}
	
	//快速就餐
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
	
	//查看是否有物品
	public boolean checkItem(int code) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//游戏道具
			for(AItemNode node = AItemHead.next;node!=null;node=node.next) {
				if(node.item==code) return true;
			}
			return false;
		}
		else if(itemKind==2) {		//消耗品
			if(BItemArray[itemNumber]>0) return true;
			else return false;
		}
		else return false;
	}
	
	//查看该物品数量
	public int getThisItemQuantity(int code) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//游戏道具
			for(AItemNode node = AItemHead.next;node!=null;node=node.next) {
				if(node.item==code) return 1;
			}
			return 0;
		}
		else if(itemKind==2) {		//消耗品
			return BItemArray[itemNumber];
		}
		else return -1;
	}
	
	//获得物品
	public void addItem(int code) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//游戏道具
			if(!checkItem(code)) {
				AItemNode node = new AItemNode(code,AItemHead.next);
				AItemHead.next=node;
				if(myData.getPlayerStatus()!=3||myData.getPlayerStatus()!=6) gameFrame.showLeftMessage("获得 "+getItemName(code));
				if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,1,"+code+",1");
			}
		}
		else if(itemKind==2) {		//消耗品
			BItemArray[itemNumber]++;
			if(myData.getPlayerStatus()!=3||myData.getPlayerStatus()!=6) gameFrame.showLeftMessage("获得 "+getItemName(code)+" *1");
			if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,1,"+code+",1");
		}
	}
	
	//获得物品
	public void addItem(int code,int quantity) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//游戏道具
			if(!checkItem(code)) {
				AItemNode node = new AItemNode(code,AItemHead.next);
				AItemHead.next=node;
				if(myData.getPlayerStatus()!=3&&myData.getPlayerStatus()!=6) gameFrame.showLeftMessage("获得 "+getItemName(code));
				if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,1,"+code+","+quantity);
			}
		}
		else if(itemKind==2) {		//消耗品
			BItemArray[itemNumber]+=quantity;
			if(myData.getPlayerStatus()!=3&&myData.getPlayerStatus()!=6) gameFrame.showLeftMessage("获得 "+getItemName(code)+" *"+quantity);
			if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,1,"+code+","+quantity);
		}
	}
	
	//共同获得物品
	public void addItemTogether(int code,int quantity) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//游戏道具
			if(!checkItem(code)) {
				AItemNode node = new AItemNode(code,AItemHead.next);
				AItemHead.next=node;
				if(myData.getPlayerStatus()!=3||myData.getPlayerStatus()!=6) gameFrame.showLeftMessage("获得 "+getItemName(code));
			}
		}
		else if(itemKind==2) {		//消耗品
			BItemArray[itemNumber]+=quantity;
			if(myData.getPlayerStatus()!=3||myData.getPlayerStatus()!=6) gameFrame.showLeftMessage("获得 "+getItemName(code)+" *"+quantity);
		}
	}
	
	//使用物品
	public boolean useItem(int code) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//游戏道具
			for(AItemNode node = AItemHead;node.next!=null;node=node.next) {
				if(node.next.item==code) {
					node.next=node.next.next;
					gameFrame.showLeftMessage("使用 "+getItemName(code));
					if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,2,"+code+",1");
					return true;
				}
			}
			return false;
		}
		else if(itemKind==2) {		//消耗品
			if(BItemArray[itemNumber]>0) {
				BItemArray[itemNumber]--;
				gameFrame.showLeftMessage("消耗 "+getItemName(code)+" *1");
				if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,2,"+code+",1");
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	//使用物品
	public boolean useItem(int code,int quantity) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//游戏道具
			if(quantity!=1) return false;
			for(AItemNode node = AItemHead;node.next!=null;node=node.next) {
				if(node.next.item==code) {
					node.next=node.next.next;
					gameFrame.showLeftMessage("使用 "+getItemName(code));
					if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,2,"+code+","+quantity);
					return true;
				}
			}
			return false;
		}
		else if(itemKind==2) {		//消耗品
			if(BItemArray[itemNumber]>=quantity) {
				BItemArray[itemNumber]-=quantity;
				gameFrame.showLeftMessage("消耗 "+getItemName(code)+" *"+quantity);
				if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) myData.sendMessage("3,2,"+code+","+quantity);
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	//共同使用物品
	public boolean useItemTogether(int code,int quantity) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==1) {			//游戏道具
			if(quantity!=1) return false;
			for(AItemNode node = AItemHead;node.next!=null;node=node.next) {
				if(node.next.item==code) {
					node.next=node.next.next;
					gameFrame.showLeftMessage("使用 "+getItemName(code));
					return true;
				}
			}
			return false;
		}
		else if(itemKind==2) {		//消耗品
			if(BItemArray[itemNumber]>=quantity) {
				BItemArray[itemNumber]-=quantity;
				gameFrame.showLeftMessage("消耗 "+getItemName(code)+" *"+quantity);
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	//获得总物品种类数
	public int getTotalItemKinds() {
		return getAItemKinds()+getBItemKinds();
	}
	
	//获得物品名
	public String getItemName(int code) {
		return dictionary.getName(code);
	}
	
	//获得物品描述
	public String getItemDescription(int code) {
		return dictionary.getDescription(code);
	}
	
	//AItemNode内部类: A物品 存放只可获得一次的物品的链表节点，其中实现了基本元素的存取
	class AItemNode {
		//物品的序号对应数组存放的位置
		
		private int item;	//物品的序号
		private AItemNode next;   //下一个结点
		
		//构造函数：物品信息的初始化
		public AItemNode(int item){
			this.item = item;
			next=null;
		}
		
		//构造函数：物品信息的初始化
		public AItemNode(int item,AItemNode next){
			this.item = item;
			this.next=next;
		}
		
		//构造函数：用于头节点的构造
		public AItemNode(){
			next = null;
		}
	}
		
	//获取背包中A物品种类数
	public int getAItemKinds(){
		int length=0;
		for(AItemNode node = AItemHead;node.next!=null;node=node.next) {
			length++;
		}
		return length;
	}
	
	//遍历A表中物品
	public int[] getAItems(){
		int length=getAItemKinds();
		int[] AItems = new int[length];
		int i=0;
		for(AItemNode node = AItemHead.next;node!=null;node=node.next,i++) {
			AItems[i]=node.item;
		}
		return AItems;
	}
	
	//获取背包中B物品种类数
	public int getBItemKinds(){
		int length=0;
		for(int i=0;i<BMax;i++) {
			if(BItemArray[i]>0) length++;
		}
		return length;
	}
		
	//遍历B表中物品
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

	//词典类 查找背包中物品信息字典
	class Dictionary {
		private String[] nameA=new String[AMax];
		private String[] descriptionA=new String[AMax];
		private String[] nameB=new String[AMax];
		private String[] descriptionB=new String[AMax];
		private NumberProcesser numberProcesser = new NumberProcesser();	//数字处理器
		
		public Dictionary() {
			initialDictionary();
		}
		
		//从词典中获取物品名
		public String getName(int code) {
			//System.out.println("getName: "+code);
			int itemKind=numberProcesser.headSplit(code);
			int itemNumber=numberProcesser.endSplit(code);
			if(itemKind==1) {			//游戏道具
				return nameA[itemNumber];
			}
			else if(itemKind==2) {		//消耗品
				return nameB[itemNumber];
			}
			else return null;
		}
		
		//从词典中获取物品描述
		public String getDescription(int code) {
			//System.out.println("getDescription: "+code);
			int itemKind=numberProcesser.headSplit(code);
			int itemNumber=numberProcesser.endSplit(code);
			if(itemKind==1) {			//游戏道具
				return descriptionA[itemNumber];
			}
			else if(itemKind==2) {		//消耗品
				return descriptionB[itemNumber];
			}
			else return null;
		}
		
		//在词典中添加词条
		public void addDirectoryItem(int code,String name,String description) {
			int itemKind=numberProcesser.headSplit(code);
			int itemNumber=numberProcesser.endSplit(code);
			if(itemKind==1) {			//游戏道具
				if(itemNumber<AMax) {
					nameA[itemNumber]=name;
					descriptionA[itemNumber]=description;
				}
				else {
					System.out.println("类型1物品词典超界");
				}
			}
			else if(itemKind==2) {		//消耗品
				if(itemNumber<BMax) {
					nameB[itemNumber]=name;
					descriptionB[itemNumber]=description;
				}
				else {
					System.out.println("类型2物品词典超界");
				}
			}
		}
		
		//初始化词典
		public void initialDictionary() {
		////////////////////////////////////////////////////////////////////////////////////////////////////
			addDirectoryItem(100,"大学计算机-计算思维导论","大学计算机-计算思维导论,简称为‘计导’,"
					+ "本部亦称之为‘大计基’,由哈工大战德臣教授编写,其高深不可估量。如果你问我怎么学,我只能回答你:"
					+ "信战神,得永生!");
			addDirectoryItem(200,"农夫山泉","农夫山泉,有点甜!");
			addDirectoryItem(101,"高等数学(第七版)上册","一本残破不堪的高等数学教材，记录着属于他的曾经的战斗...");
			addDirectoryItem(102,"不明纸袋","一个古怪的小纸袋，上面写着‘保’和‘416.00’，里面鼓鼓的...");
		}
	}
}