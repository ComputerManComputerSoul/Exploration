package calculate;

//ChangeItemDictionary类:存储房间中可改变Item的改变结果链表
public class ChangeableItemList {
	private ChangeableItemNode changeableItemHead = new ChangeableItemNode();
	private int roomNumber;
	public ChangeableItemList(int roomNumber) {
		this.roomNumber=roomNumber;
	}
	
	//查看x,y坐标对应值
	public int checkItem(int x,int y) {
		for(ChangeableItemNode node = changeableItemHead.next;node!=null;node=node.next) {
			if(node.x==x&&node.y==y) return node.locationMessage;
		}
		return -1;
	}
		
	//添加词条，已有则更改
	public void addItem(int x,int y,int locationMessage) {
		for(ChangeableItemNode node = changeableItemHead.next;node!=null;node=node.next) {
			if(node.x==x&&node.y==y) {
				node.locationMessage = locationMessage;
				return;
			}
		}
		ChangeableItemNode node = new ChangeableItemNode(x,y,locationMessage,changeableItemHead.next);
		changeableItemHead.next = node;
	}
	
	//查找词条数目
	public int getAmount() {
		int i=0;
		for(ChangeableItemNode node = changeableItemHead.next;node!=null;node=node.next) {
			i++;
		}
		return i;
	}

	//获得链表对应的房间号
	public int getRoomNumber() {
		return roomNumber;
	}
	
	//根据链表更改地图中locationMessage
	public void changeLocationMessage(int[][] map) {
		for(ChangeableItemNode node = changeableItemHead.next;node!=null;node=node.next) {
			map[node.x][node.y]=node.locationMessage;
		}
	}
	
	//获得包装信息码数组
	public String[] getCodes() {
		String[] codes = new String[getAmount()]; 
		int i=0;
		for(ChangeableItemNode node = changeableItemHead.next;node!=null;node=node.next) {
			codes[i]="2,"+roomNumber+","+node.x+","+node.y+","+node.locationMessage;
			//System.out.println(codes[i]);
			i++;
		}
		return codes;
	}
	
	//ChangeableItemNode内部类: 房间中可改变Item的改变结果结点
	class ChangeableItemNode {
		//物品的序号对应数组存放的位置
		private int x;	
		private int y;
		private int locationMessage;
		private ChangeableItemNode next;
		
		//构造函数：结点的初始化
		public ChangeableItemNode(int x,int y,int locationMessage){
			this.x=x;
			this.y=y;
			this.locationMessage=locationMessage;
			next=null;
		}
		
		//构造函数：结点的初始化
		public ChangeableItemNode(int x,int y,int locationMessage,ChangeableItemNode next){
			this.x=x;
			this.y=y;
			this.locationMessage=locationMessage;
			this.next=next;
		}
		
		//构造函数：用于头节点的构造
		public ChangeableItemNode(){
			next = null;
		}
	}
	
}
