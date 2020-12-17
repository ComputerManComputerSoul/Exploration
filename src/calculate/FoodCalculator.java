package calculate;

import java.util.Arrays;

//����ظ�����ֵ�ķ���
public class FoodCalculator{
	private int[] num;  //����Ʒ����������
	private int[] cost;  //����Ʒ�ظ���������
	private final int BMAX = Constant.B_MAX;
	private FoodDictionary dictionary = new FoodDictionary(); //��ʼ���ʵ�
	private int max;  //��Ҫ���������
	private Bag bag; //����

	//���캯��������B�������ʼ��num����,���ݴʵ��ʼ��cost����
	public FoodCalculator(Bag bag) { //TODO:Ϊ�˲���ɾȥ��bag���������һ��bag��ȥ
		num = new int[BMAX]; 
		cost = new int[BMAX]; 
		this.bag = bag;
		Arrays.fill(num, 0);
	}
	
	//���㲻����max������Ʒ���
	public int[] Calculate(int strength) {
		Stack stack = new Stack(); //��ʼ��һ��ջ
		int nowMax = -1; 
		max = 100 - strength;
		for(int i = 0; i < BMAX ; i++) {
			cost[i] = dictionary.getFoodPower(i + 200);
			if(bag.checkItem(i + 200)) {
				num[i] = bag.getThisItemQuantity(i + 200); 
			}
		}  //num��cost����ĳ�ʼ��
		
		Node nowNode = new Node(500, 0, num);  //�����ĳ�ʼ��
		Node maxNode = nowNode;    //��¼��ǰ�Ļָ������������ڵ�
		nowNode.setFatherNode(null);  //���ø��ĸ��ڵ�Ϊ��
		nowNode.setCostAdd(0);   //��ʼ���������ۼƻظ�����ֵ
		
		//��һ�γ�ʼ����
		for(int i = 0; i < num.length; i++) {
			if(num[i] > 0) {
				Node treeNode = new Node(i + 200,cost[i],num);
				treeNode.setFatherNode(nowNode);
				stack.push(treeNode);
			}
		}
		
		//��ջ��ʱ�˳�����
		while(!stack.isEmpty()) {
			nowNode = stack.pop();
				nowNode.setCostAdd(nowNode.fatherNode.costAdd + nowNode.cost); //�����ۻ��ظ�����ֵ
				
				//�ж��Ƿ��ҵ����ֵ��ͬʱ�������ֵ
				if(nowNode.costAdd > max) {
					continue;
				}else if (nowNode.costAdd == max) {
					maxNode = nowNode;
					nowMax = max;
					break;
				}else {
					if(nowNode.costAdd > nowMax) {
						maxNode = nowNode;
						nowMax = nowNode.costAdd;
					}
				}
				
				//��չ��
				for(int i = 0; i < num.length; i++) {
					if(!isBagEmpty(nowNode.num)) {
						if(nowNode.num[i] > 0) {
							Node treeNode = new Node(i + 200, cost[i], nowNode.num);
							treeNode.setFatherNode(nowNode);
							stack.push(treeNode);
						}
						}
			}
		}
		
		int[] newnum =changeNum(num,cost,nowMax); //ͨ��̰���㷨�ı��Ľṹ
//		//ʵ������
//		System.out.println("����ʵ��");
//		System.out.println("һ���ָ���"+nowMax+"������");
//		for(int i = 0; i < BMAX; i++) {
//			if(newnum[i] < num[i]) {
//				System.out.println("ʹ����"+(num[i]-newnum[i])+"��"+(200+i)+"B����Ʒ"+","+"���һظ���"+
//						(num[i] - newnum[i])*dictionary.getFoodPower(200+i)+"������");
//			}
//		}
		return newnum;
	}
	
	//����̰���㷨�ı�num��������
	private int[] changeNum(int[] num,int[]cost,int max) {
		int[] newnum = new int[BMAX];
		int j = BMAX-1;
		for(int i = 0; i < BMAX; i++) {
			newnum[i] = num[i];
		}
		while(j >= 0){
			if(max - cost[j] < 0 || newnum[j] - 1 < 0) {
				j--;
			}else {
				max = max - cost[j];
				newnum[j]--;
			}
			if(max == 0) {
				break;
			}
		}
		return newnum;
	}
	
	//�жϱ������Ƿ�����Ʒ
	private boolean isBagEmpty(int[] num) {
		int sum = 0;
		for(int i = 0; i < BMAX; i++) {
			sum = sum + num[i];
		}
		if(0 == sum) {
			return true;
		}else {
			return false;
		}
	}
	
	//���ݽṹ:���飬ջ�ĵײ����ݽṹ
	class Array{
		private int length;  //����ĳ���
		private int num;  //������Ԫ�صĸ���
		private Node[] array;  //����
		
		public Array(){
			this.length = 500;
			this.num = 0;
			array = new Node[length];
		}
		
		//������ĩβ���Ԫ��,node����ӵ�Ԫ��
		public void add(Node node) {
			array[num] = node;
			num++;
		}
		
		//�ж������Ƿ�Ϊ��
		public boolean isEmpty() {
			return 0 == num;
		}
		
		//ɾ������ĩβ��Ԫ��
		public Node delete() {
			if(true == isEmpty()) {
	    		try {
	    			throw new Exception("����Ϊ��");
	    		}catch(Exception e3) {
	    			e3.printStackTrace();
	    		}
	    	}
			Node node = array[num - 1];
			array[num - 1] = null;
			num--;
			return node;
		}
	}
	
	//���ݽṹջ���ײ����ݽṹΪ����
	class Stack{
		int top; //ջ��
		int maxSize; //ջ����󳤶�
		Array arr; //����
		
		//���캯������ʼ��ջ
		public Stack(){
			top = -1;
			arr = new Array();
			maxSize = arr.length;
		}
		
		//ѹջ,ѹ���Ԫ��Ϊnode
		public void push(Node node) {
			arr.add(node);
			top++;
		}
		
		//��ջ��������Ԫ��Ϊnode
		public Node pop() {
			top--;
			return arr.delete();
		}
		
		//�ж�ջ�Ƿ�Ϊ��
		public boolean isEmpty() {
			return arr.isEmpty();
		}
	}
	
	//�ڲ��࣬�洢ʳƷ�ظ�ֵ�������Լ��ýڵ�ĸ��ڵ�
	class Node{
		private int cost; //�ָ�����ֵ
		private int code; //��Ʒ������
		private int costAdd; //���ýڵ��Ѿ��ָ�������ֵ
		private Node fatherNode; //���׽ڵ�
		private int[] num = new int[BMAX];
		//���캯��:��ʼ��cost��code
		public Node(int code, int cost,int[] num) {
			this.code = code;
			this.cost = cost;
			for(int i = 0; i < num.length; i++) {
				if(i != code - 200) {
					this.num[i] = num[i];
				}else {
					this.num[i] = num[i] - 1;
				}
			}
		}
		
		//���ø��ڵ�
		public void setFatherNode(Node fatherNode) {
			this.fatherNode = fatherNode;
		}
		
		//����costAdd
		public void setCostAdd(int costAdd) {
			this.costAdd = costAdd;
		}
	}
}
