package calculate;

import java.util.Arrays;

//计算回复体力值的方案
public class FoodCalculator{
	private int[] num;  //消耗品的数量数组
	private int[] cost;  //消耗品回复体力数组
	private final int BMAX = Constant.B_MAX;
	private FoodDictionary dictionary = new FoodDictionary(); //初始化词典
	private int max;  //需要补充的体力
	private Bag bag; //背包

	//构造函数：根据B类数组初始化num数组,根据词典初始化cost数组
	public FoodCalculator(Bag bag) { //TODO:为了测试删去了bag，还需添加一个bag进去
		num = new int[BMAX]; 
		cost = new int[BMAX]; 
		this.bag = bag;
		Arrays.fill(num, 0);
	}
	
	//计算不超过max的消耗品组合
	public int[] Calculate(int strength) {
		Stack stack = new Stack(); //初始化一个栈
		int nowMax = -1; 
		max = 100 - strength;
		for(int i = 0; i < BMAX ; i++) {
			cost[i] = dictionary.getFoodPower(i + 200);
			if(bag.checkItem(i + 200)) {
				num[i] = bag.getThisItemQuantity(i + 200); 
			}
		}  //num和cost数组的初始化
		
		Node nowNode = new Node(500, 0, num);  //树根的初始化
		Node maxNode = nowNode;    //记录当前的恢复体力最大的树节点
		nowNode.setFatherNode(null);  //设置根的父节点为空
		nowNode.setCostAdd(0);   //初始化树根的累计回复体力值
		
		//第一次初始化树
		for(int i = 0; i < num.length; i++) {
			if(num[i] > 0) {
				Node treeNode = new Node(i + 200,cost[i],num);
				treeNode.setFatherNode(nowNode);
				stack.push(treeNode);
			}
		}
		
		//当栈空时退出搜索
		while(!stack.isEmpty()) {
			nowNode = stack.pop();
				nowNode.setCostAdd(nowNode.fatherNode.costAdd + nowNode.cost); //设置累积回复体力值
				
				//判断是否找到最大值，同时更新最大值
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
				
				//扩展树
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
		
		int[] newnum =changeNum(num,cost,nowMax); //通过贪心算法改变解的结构
//		//实例测试
//		System.out.println("测试实例");
//		System.out.println("一共恢复了"+nowMax+"点体力");
//		for(int i = 0; i < BMAX; i++) {
//			if(newnum[i] < num[i]) {
//				System.out.println("使用了"+(num[i]-newnum[i])+"个"+(200+i)+"B类物品"+","+"并且回复了"+
//						(num[i] - newnum[i])*dictionary.getFoodPower(200+i)+"点体力");
//			}
//		}
		return newnum;
	}
	
	//利用贪心算法改变num数组的组成
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
	
	//判断背包内是否有物品
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
	
	//数据结构:数组，栈的底层数据结构
	class Array{
		private int length;  //数组的长度
		private int num;  //数组中元素的个数
		private Node[] array;  //数组
		
		public Array(){
			this.length = 500;
			this.num = 0;
			array = new Node[length];
		}
		
		//向数组末尾添加元素,node是添加的元素
		public void add(Node node) {
			array[num] = node;
			num++;
		}
		
		//判断数组是否为空
		public boolean isEmpty() {
			return 0 == num;
		}
		
		//删除数组末尾的元素
		public Node delete() {
			if(true == isEmpty()) {
	    		try {
	    			throw new Exception("数组为空");
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
	
	//数据结构栈，底层数据结构为数组
	class Stack{
		int top; //栈顶
		int maxSize; //栈的最大长度
		Array arr; //数组
		
		//构造函数：初始化栈
		public Stack(){
			top = -1;
			arr = new Array();
			maxSize = arr.length;
		}
		
		//压栈,压入的元素为node
		public void push(Node node) {
			arr.add(node);
			top++;
		}
		
		//出栈，弹出的元素为node
		public Node pop() {
			top--;
			return arr.delete();
		}
		
		//判断栈是否为空
		public boolean isEmpty() {
			return arr.isEmpty();
		}
	}
	
	//内部类，存储食品回复值、种类以及该节点的父节点
	class Node{
		private int cost; //恢复体力值
		private int code; //物品的种类
		private int costAdd; //到该节点已经恢复的体力值
		private Node fatherNode; //父亲节点
		private int[] num = new int[BMAX];
		//构造函数:初始化cost、code
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
		
		//设置父节点
		public void setFatherNode(Node fatherNode) {
			this.fatherNode = fatherNode;
		}
		
		//设置costAdd
		public void setCostAdd(int costAdd) {
			this.costAdd = costAdd;
		}
	}
}
