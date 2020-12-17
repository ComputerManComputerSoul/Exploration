package calculate;

//pathSearcher类:利用AStar算法进行寻路
public class PathSearcher {

	private int dest_row;      //终点行坐标
	private int dest_column;   //终点列坐标
	private MyData myData;
	private int[][] map;  //地图
	private static S[][] enableFlag; //用来模拟open list和closed list的标记数组
	private static enum S{           //对应三个状态，初始、开放、关闭
		INITIAL,OPEN,CLOSE
	}
	private static int PRICE_HW = 1;  //水平、垂直移动1的单位代价
	
	//构造函数：初始化起点终点坐标以及地图
	public PathSearcher(MyData myData) {
		//起始坐标与终点坐标的初始化
		this.dest_row = 0;
		this.dest_column = 0;
		this.myData=myData;
	}

	//利用A算法进行寻路，并返回执行动作数组
	public int[] search(int x,int y) {
		//判断输入是否合法，若不合法返回一个数组其第一个值为-1
		int start_column = myData.getX();
		int start_row = myData.getY();
		int dest_column = x;
		int dest_row = y;
		map = myData.getRoom().getTurnMap();
		if(!isLegal(start_row,start_column) && !isLegal(dest_row,dest_column)){
			int[] actionArr = new int[1];
			return actionArr;
		}
		
		//重新设置起始与终点坐标
		initial(dest_row,dest_column);
		
		int [] actionArr;
		Node currentNode;
		
		Node[][] nodes = new Node[map.length][map[0].length];
		enableFlag = new S[map.length][map[0].length];
		for(int i = 0;i < map.length; i++) {
			for(int j = 0;j < map[0].length; j++) {
				nodes[i][j] = new Node(i ,j ,this.map[i][j]);
				enableFlag[i][j] = S.INITIAL;  //将所有的节点设置为初始状态
			}
		}
		
		//对起点坐标的初始化操作
		PriorityQueue openQueue = new PriorityQueue();
		openQueue.enqueue(nodes[start_row][start_column]);  //将起点放到开放列表中
		enableFlag[start_row][start_column] = S.OPEN;  //将起点放到开放列表中
		nodes[start_row][start_column].setG(0); //标记起点g值为0
		nodes[start_row][start_column].setFatherNode(null); //标记起点父节点为空
		
		//进入路径搜索，当开放列表为空且终点或者终点已经
		while(!openQueue.isEmpty() && enableFlag[dest_row][dest_column] != S.CLOSE) {
			
			currentNode = openQueue.dequeue();
			enableFlag[currentNode.row][currentNode.column] =S.CLOSE; //进入关闭列表
			
			for(int i = currentNode.row - 1; i < currentNode.row + 2;i++) {
				for(int j = currentNode.column - calculate(i - currentNode.row); j < currentNode.column + 2;j = j + 2) {
					if(i >= 0 && i < map.length && j >= 0 && j < map[0].length && this.map[i][j] == 1) {
						if(enableFlag[i][j] == S.OPEN) {
							if(nodes[i][j].getCost() > currentNode.g + PRICE_HW + nodes[i][j].getCost() - nodes[i][j].g) {
								
								nodes[i][j].setFatherNode(currentNode);   //新的路径更好，重新计算g值，改写父节点
								nodes[i][j].setG(currentNode.getG() + PRICE_HW);
								openQueue.nodeEnqueue(nodes[i][j]);
								openQueue.enqueue(nodes[i][j]);
							}
						}else if(enableFlag[i][j] == S.CLOSE) {
							
							continue;//如果该节点进入CLOSE表则进入下一次循环
						}else {
							
							nodes[i][j].setFatherNode(currentNode);  //如果该节点未进入open，则将其放入open，计算g值，设置父节点
							nodes[i][j].setG(currentNode.getG() + PRICE_HW);
							openQueue.enqueue(nodes[i][j]);
							enableFlag[i][j] = S.OPEN;
						}
					}
				}
			}
			}
		actionArr = calculateActionArr(nodes);  //计算执行动作数组
//		System.out.println("寻路:"+start_column+","+start_row+"->"+dest_column+","+dest_row);
//		for(int i = 0; i < actionArr.length; i++) {
//			System.out.print(actionArr[i]);
//		}
//		System.out.println();
		return actionArr;
	}
	
	//将i转换为0或1
	private int calculate(int i) {
		if(-1 == i || 1 == i ) {
			return 0;
		}else {
		return 1;
		}
	}
	
	//判断坐标的输入是否合法
	public boolean isLegal(int row,int column) {
		if(row < 0 || row > map.length) {
			return false;
		}else if(column < 0 || column > map[0].length) {
			return false;
		}else if(1 != map[row][column]) {
			return false;
		}else {
			return true;
		}
	}
	
	//计算出如何执行行走动作的数组
	private int[] calculateActionArr(Node[][] nodes) {
		int length = 0;  //记录路径长度
		Node node = nodes[dest_row][dest_column];
		Node fatherNode = node.fathernode;
		while(node.fathernode != null) {
			length++;
			node = node.fathernode;
		}  //计算出路径的长度
		node = nodes[dest_row][dest_column];
		
		int[] actionArr = new int[length];
		for(int i = length - 1; i >= 0; i--) {
			if(node.row == fatherNode.row) {
				if(node.column - fatherNode.column == 1) {
					actionArr[i] = 3;
				}else {
					actionArr[i] = 2;
				}
			}else {
				if(node.row - fatherNode.row == 1) {
					actionArr[i] = 1;
				}else {
					actionArr[i] = 0;
				}
			}
			node = node.fathernode;
			fatherNode = fatherNode.fathernode;
		}
		return actionArr;
	}
	
	//初始化坐标
	private void initial(int dest_row,int dest_column) {
		this.dest_column = dest_column;
		this.dest_row = dest_row;
	}
	
	//动态数组：堆的底层数据结构
	class Arr {
		private Node[] data; //创建一个数组
		private int num;  //数组中元素的个数
		
		//自定义构造器初始化数组 capacity数组的长度
		public Arr(int capacity) {
			data = new Node[capacity];
			num = 0;
		}
		
		//默认构造方法 capacity 数组的长度等于10
		public Arr() {
			this(10);
		}
		
		//根据传来的的数组生成动态数组
		public Arr(Node[] arr) {
			data = new Node[arr.length];
			for(int i = 0;i < arr.length;i++) {
				data[i] = arr[i];
			}
			num = arr.length;
		}
		
		//获取数组中元素的个数
		public int getNum() {
			return this.num;
		}
		
		//获取数组的长度
		public int getLength() {
			return data.length;
		}
		
		//判断数组是否为空
		public boolean isEmpty() {
			return num == 0;
		}
		
		//数组中添加元素 index是添加的元素的位置 node是插入的元素
		public void add(int index ,Node node) {
			//扩容数组
			if(num == data.length) {
					resize(2 * data.length); //扩容数组两倍
			}
			
			for(int i = num - 1; i >= index ;i--) {
				data[i + 1] = data[i]; 
			}
			data[index] = node;
			num++;
		}
		
		//数组扩容或者缩容
		private void resize(int newcapacity) {
			Node []newData = new Node[newcapacity];
			for(int i = 0;i < num; i++)
			{
				newData[i] = data[i];
			}
			data = newData;
		}
		
		//在数组的末尾添加元素，添加的元素为node
		public void addLast(Node node) {
			add(num,node);
		}
		
		//在数组的首部添加元素，添加的元素为node
		public void addFirst(Node node) {
			add(0,node);
		}
			
		//获取数组中指定位置元素的值
		Node get(int index) {
			return data[index];
		}
		
		//获取数组最后一个元素的值
		public Node getLast() {
			return get(num - 1);
		}
		
		//获取数组第一个元素的值
		public Node getFirst() {
			return get(0);
		}
		
		//修改数组指定位置的值,修改的位置为index，修改的后的值为node
	    void set(int index, Node node) {
	    	data[index] = node;
	    }
	    
	    //查询是否包含元素node
	    public boolean contains(Node node) {
	    	for(int i =0; i < num; i++)
	    	{
	    		if(node == data[i]) {
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	    
	    //查找元素node若找到返回node的位置
	    public int find(Node node) {
	    	for( int i = 0; i < num; i++) {
	    		if(node == data[i]) {
	    			return i;//返回元素node的位置
	    		}
	    	}
	    	return -1;//如果找不到元素node，则返回-1
	    }
	    
	    //删除指定位置的index的数组元素
	    public Node remove(int index) {
	    	 //缩容
	        if(num == data.length/2) {
	        	resize(data.length/2);
	        }
	    	Node ret = data[index];
	    	for(int i = index + 1; i < num; i++) {
	    		data[i - 1] = data[i];
	    	}
	    	num--;
			data[num] = null;
			return ret;
	    }
	    
	    //删除数组的最后一个元素
	    public Node removeLast() {
	    	return remove(num - 1);
	    }
	    
	    //删除数组的首个元素
	    public Node removeFirst() {
	    	return remove(0);
	    }
	    
	    //删除数组元素node
	    public void removeElement(Node node) {
	    	int index = find(node);
	    	if(index != -1) {
	    		remove(index);
	    	}
	    	else {
	    		System.out.println("数组中不存在元素node");
	    	}
	    }
	    
	    //元素交换
	    public void swap(int i, int j) {
	    	Node t = data[i];
			data[i] = data[j];
			data[j] = t;
	    }
	}
	
	//最小堆 以动态数组为底层数据结构
	class MinHeap  {
		private Arr data;
		
		
		//构造函数：创建一个大小为capacity的动态数组
		public MinHeap(int capacity) {
			data = new Arr(capacity);
		}
		
		//构造函数：创建一个大小为10的动态数组
		public MinHeap() {
			data = new Arr();
		}
		
		//构造函数 将任意数组构造成最大堆的形状
		public MinHeap(Node[] arr) {
			data = new Arr(arr);//根据传来的数组生成动态数组；
			for(int i =parent(arr.length - 1); i >= 0; i--) {
				siftDown(i); //从最后一个非叶子节点进行下沉，一直到根节点
			}
		}
		
		//返回最大堆的元素个数
		public int num() {
			return data.getNum();
		}
		
		//查询最大堆是否为空
		public boolean isEmpty() {
			return data.isEmpty();
		}
		
		//返回一个数组中一个索引所表示元素的父亲节点的索引，如果没有父节点返回-1
		private int parent(int index) {
			if(0 == index) {
				return -1;
			}
			return (index - 1)/2;
		}
		
		//获取左孩子节点的索引
		private int leftChild(int index) {
			return 2*index + 1;
		}
		
		//获取右孩子节点的索引
		private int rightChild(int index) {
			return 2*index + 2;
		}
		
		//向堆中添加元素
		public void add(Node node) {
			data.addLast(node);
			siftUp(data.getNum() - 1); //元素上浮
		}
		
		//堆中的元素上浮
		private void siftUp(int k) {
			while(k > 0 && data.get(parent(k)).getCost() > data.get(k).getCost()) {
				data.swap(k, parent(k)); //与父节点交换
				k = parent(k); //k变为其父节点的索引，准备下一次循环
			}
		}
		
		//堆中元素的下沉
		private void siftDown(int k) {
			while(leftChild(k) < data.getNum()) {
				int j = leftChild(k);
				if(j + 1 < data.getNum()) {
					if(data.get(j + 1).getCost() < data.get(j).getCost()) {
					j = rightChild(k);  //j左右孩子中最小值的索引
					}
					if(data.get(k).getCost() < data.get(j).getCost()) {
						break;   //如果无法下沉则不做任何处理
					}
				}else {
					if(data.get(k).getCost() < data.get(j).getCost()) {
						break;
					}		
				}
				data.swap(k, j);   //否则将元素与较小的那个孩子节点交换
				k = j;  //将索引k变为j，准备下一次循环
			}
		}
		
		//获得堆中最小元素的值，如果堆空则返回空
		public Node findMin() {
			if(data.getNum() == 0) {
				return null;
			}
			return data.get(0);
		}
		
		//取出堆中最小元素的值
		public Node extractMin() {
			Node ret = findMin();
			data.swap(0,data.getNum() - 1);
			data.removeLast();
			siftDown(0);
			return ret;
		}
		
		//取出堆中的最小元素，并且替换成元素node
		public Node replace(Node node) {
			Node ret = findMin();
			data.set(0, node);
			siftDown(0);
			return ret;
		}
		
		//删除堆中的Node
		public void removeNode(Node node) {
			data.swap(data.find(node),data.getNum() - 1);
			data.removeLast();
			siftDown(0);
		}
	}
	
	//优先队列，底层结构为最大堆  实现队列接口
	class PriorityQueue{

		private MinHeap minHeap;  //建立一个最大堆
		
		//构造函数：建立一个优先队列
		public PriorityQueue() {
			minHeap = new MinHeap();
		}
		
		//元素node入队
		public void enqueue(Node node) {
			minHeap.add(node);
		}
		
		//最小的元素出队
		public Node dequeue() {
			return minHeap.extractMin();
		}
		
		//获得优先队列队头元素的值
		public Node getFront() {
			return minHeap.findMin();
		}
		
		//获取队列的长度
		public int getNum() {
			return minHeap.num();
		}
		
		//判断队列是否为空
		public boolean isEmpty() {
			return minHeap.isEmpty();
		}
		
		//删除元素node
		public void nodeEnqueue(Node node) {
			minHeap.removeNode(node);
		}
	}
	
	//节点:存放地图节点的信息
	class Node {
		private int g;   //搜索节点Node已花费的代价
		private int row,column;  //节点的坐标
		private int type;  //当前节点的类型
		private Node fathernode; //父节点
		
		//构造函数：初始化节点的信息
		public Node(int row,int column,int type) {
			this.row =row;
			this.column = column;
			this.type = type;
		}

		//设置父节点
		public void setFatherNode(Node fathernode) {
			this.fathernode = fathernode;
		}
		
		//返回搜索Node已花费的代价
		public int getG() {
			return g;
		}
		
		//设置搜索Node已花费的代价
		public void setG(int g) {
			this.g =g;
		}
		
		//获取对于节点Node总的代价估计
		public int getCost() {
			return getHValue() + g;
		}
		
		//启发式函数
		private int getHValue() {
			return (Math.abs(row - dest_row) + Math.abs(column - dest_column));
		}
		
		//设置类型
		public void setType(int type) {
			this.type = type;
		}
		
		//返回类型
		public int getType() {
			return type;
		}
	}
	
	
}
