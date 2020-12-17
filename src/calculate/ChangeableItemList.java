package calculate;

//ChangeItemDictionary��:�洢�����пɸı�Item�ĸı�������
public class ChangeableItemList {
	private ChangeableItemNode changeableItemHead = new ChangeableItemNode();
	private int roomNumber;
	public ChangeableItemList(int roomNumber) {
		this.roomNumber=roomNumber;
	}
	
	//�鿴x,y�����Ӧֵ
	public int checkItem(int x,int y) {
		for(ChangeableItemNode node = changeableItemHead.next;node!=null;node=node.next) {
			if(node.x==x&&node.y==y) return node.locationMessage;
		}
		return -1;
	}
		
	//��Ӵ��������������
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
	
	//���Ҵ�����Ŀ
	public int getAmount() {
		int i=0;
		for(ChangeableItemNode node = changeableItemHead.next;node!=null;node=node.next) {
			i++;
		}
		return i;
	}

	//��������Ӧ�ķ����
	public int getRoomNumber() {
		return roomNumber;
	}
	
	//����������ĵ�ͼ��locationMessage
	public void changeLocationMessage(int[][] map) {
		for(ChangeableItemNode node = changeableItemHead.next;node!=null;node=node.next) {
			map[node.x][node.y]=node.locationMessage;
		}
	}
	
	//��ð�װ��Ϣ������
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
	
	//ChangeableItemNode�ڲ���: �����пɸı�Item�ĸı������
	class ChangeableItemNode {
		//��Ʒ����Ŷ�Ӧ�����ŵ�λ��
		private int x;	
		private int y;
		private int locationMessage;
		private ChangeableItemNode next;
		
		//���캯�������ĳ�ʼ��
		public ChangeableItemNode(int x,int y,int locationMessage){
			this.x=x;
			this.y=y;
			this.locationMessage=locationMessage;
			next=null;
		}
		
		//���캯�������ĳ�ʼ��
		public ChangeableItemNode(int x,int y,int locationMessage,ChangeableItemNode next){
			this.x=x;
			this.y=y;
			this.locationMessage=locationMessage;
			this.next=next;
		}
		
		//���캯��������ͷ�ڵ�Ĺ���
		public ChangeableItemNode(){
			next = null;
		}
	}
	
}
