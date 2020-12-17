package calculate;

//pathSearcher��:����AStar�㷨����Ѱ·
public class PathSearcher {

	private int dest_row;      //�յ�������
	private int dest_column;   //�յ�������
	private MyData myData;
	private int[][] map;  //��ͼ
	private static S[][] enableFlag; //����ģ��open list��closed list�ı������
	private static enum S{           //��Ӧ����״̬����ʼ�����š��ر�
		INITIAL,OPEN,CLOSE
	}
	private static int PRICE_HW = 1;  //ˮƽ����ֱ�ƶ�1�ĵ�λ����
	
	//���캯������ʼ������յ������Լ���ͼ
	public PathSearcher(MyData myData) {
		//��ʼ�������յ�����ĳ�ʼ��
		this.dest_row = 0;
		this.dest_column = 0;
		this.myData=myData;
	}

	//����A�㷨����Ѱ·��������ִ�ж�������
	public int[] search(int x,int y) {
		//�ж������Ƿ�Ϸ��������Ϸ�����һ���������һ��ֵΪ-1
		int start_column = myData.getX();
		int start_row = myData.getY();
		int dest_column = x;
		int dest_row = y;
		map = myData.getRoom().getTurnMap();
		if(!isLegal(start_row,start_column) && !isLegal(dest_row,dest_column)){
			int[] actionArr = new int[1];
			return actionArr;
		}
		
		//����������ʼ���յ�����
		initial(dest_row,dest_column);
		
		int [] actionArr;
		Node currentNode;
		
		Node[][] nodes = new Node[map.length][map[0].length];
		enableFlag = new S[map.length][map[0].length];
		for(int i = 0;i < map.length; i++) {
			for(int j = 0;j < map[0].length; j++) {
				nodes[i][j] = new Node(i ,j ,this.map[i][j]);
				enableFlag[i][j] = S.INITIAL;  //�����еĽڵ�����Ϊ��ʼ״̬
			}
		}
		
		//���������ĳ�ʼ������
		PriorityQueue openQueue = new PriorityQueue();
		openQueue.enqueue(nodes[start_row][start_column]);  //�����ŵ������б���
		enableFlag[start_row][start_column] = S.OPEN;  //�����ŵ������б���
		nodes[start_row][start_column].setG(0); //������gֵΪ0
		nodes[start_row][start_column].setFatherNode(null); //�����㸸�ڵ�Ϊ��
		
		//����·���������������б�Ϊ�����յ�����յ��Ѿ�
		while(!openQueue.isEmpty() && enableFlag[dest_row][dest_column] != S.CLOSE) {
			
			currentNode = openQueue.dequeue();
			enableFlag[currentNode.row][currentNode.column] =S.CLOSE; //����ر��б�
			
			for(int i = currentNode.row - 1; i < currentNode.row + 2;i++) {
				for(int j = currentNode.column - calculate(i - currentNode.row); j < currentNode.column + 2;j = j + 2) {
					if(i >= 0 && i < map.length && j >= 0 && j < map[0].length && this.map[i][j] == 1) {
						if(enableFlag[i][j] == S.OPEN) {
							if(nodes[i][j].getCost() > currentNode.g + PRICE_HW + nodes[i][j].getCost() - nodes[i][j].g) {
								
								nodes[i][j].setFatherNode(currentNode);   //�µ�·�����ã����¼���gֵ����д���ڵ�
								nodes[i][j].setG(currentNode.getG() + PRICE_HW);
								openQueue.nodeEnqueue(nodes[i][j]);
								openQueue.enqueue(nodes[i][j]);
							}
						}else if(enableFlag[i][j] == S.CLOSE) {
							
							continue;//����ýڵ����CLOSE���������һ��ѭ��
						}else {
							
							nodes[i][j].setFatherNode(currentNode);  //����ýڵ�δ����open���������open������gֵ�����ø��ڵ�
							nodes[i][j].setG(currentNode.getG() + PRICE_HW);
							openQueue.enqueue(nodes[i][j]);
							enableFlag[i][j] = S.OPEN;
						}
					}
				}
			}
			}
		actionArr = calculateActionArr(nodes);  //����ִ�ж�������
//		System.out.println("Ѱ·:"+start_column+","+start_row+"->"+dest_column+","+dest_row);
//		for(int i = 0; i < actionArr.length; i++) {
//			System.out.print(actionArr[i]);
//		}
//		System.out.println();
		return actionArr;
	}
	
	//��iת��Ϊ0��1
	private int calculate(int i) {
		if(-1 == i || 1 == i ) {
			return 0;
		}else {
		return 1;
		}
	}
	
	//�ж�����������Ƿ�Ϸ�
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
	
	//��������ִ�����߶���������
	private int[] calculateActionArr(Node[][] nodes) {
		int length = 0;  //��¼·������
		Node node = nodes[dest_row][dest_column];
		Node fatherNode = node.fathernode;
		while(node.fathernode != null) {
			length++;
			node = node.fathernode;
		}  //�����·���ĳ���
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
	
	//��ʼ������
	private void initial(int dest_row,int dest_column) {
		this.dest_column = dest_column;
		this.dest_row = dest_row;
	}
	
	//��̬���飺�ѵĵײ����ݽṹ
	class Arr {
		private Node[] data; //����һ������
		private int num;  //������Ԫ�صĸ���
		
		//�Զ��幹������ʼ������ capacity����ĳ���
		public Arr(int capacity) {
			data = new Node[capacity];
			num = 0;
		}
		
		//Ĭ�Ϲ��췽�� capacity ����ĳ��ȵ���10
		public Arr() {
			this(10);
		}
		
		//���ݴ����ĵ��������ɶ�̬����
		public Arr(Node[] arr) {
			data = new Node[arr.length];
			for(int i = 0;i < arr.length;i++) {
				data[i] = arr[i];
			}
			num = arr.length;
		}
		
		//��ȡ������Ԫ�صĸ���
		public int getNum() {
			return this.num;
		}
		
		//��ȡ����ĳ���
		public int getLength() {
			return data.length;
		}
		
		//�ж������Ƿ�Ϊ��
		public boolean isEmpty() {
			return num == 0;
		}
		
		//���������Ԫ�� index����ӵ�Ԫ�ص�λ�� node�ǲ����Ԫ��
		public void add(int index ,Node node) {
			//��������
			if(num == data.length) {
					resize(2 * data.length); //������������
			}
			
			for(int i = num - 1; i >= index ;i--) {
				data[i + 1] = data[i]; 
			}
			data[index] = node;
			num++;
		}
		
		//�������ݻ�������
		private void resize(int newcapacity) {
			Node []newData = new Node[newcapacity];
			for(int i = 0;i < num; i++)
			{
				newData[i] = data[i];
			}
			data = newData;
		}
		
		//�������ĩβ���Ԫ�أ���ӵ�Ԫ��Ϊnode
		public void addLast(Node node) {
			add(num,node);
		}
		
		//��������ײ����Ԫ�أ���ӵ�Ԫ��Ϊnode
		public void addFirst(Node node) {
			add(0,node);
		}
			
		//��ȡ������ָ��λ��Ԫ�ص�ֵ
		Node get(int index) {
			return data[index];
		}
		
		//��ȡ�������һ��Ԫ�ص�ֵ
		public Node getLast() {
			return get(num - 1);
		}
		
		//��ȡ�����һ��Ԫ�ص�ֵ
		public Node getFirst() {
			return get(0);
		}
		
		//�޸�����ָ��λ�õ�ֵ,�޸ĵ�λ��Ϊindex���޸ĵĺ��ֵΪnode
	    void set(int index, Node node) {
	    	data[index] = node;
	    }
	    
	    //��ѯ�Ƿ����Ԫ��node
	    public boolean contains(Node node) {
	    	for(int i =0; i < num; i++)
	    	{
	    		if(node == data[i]) {
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	    
	    //����Ԫ��node���ҵ�����node��λ��
	    public int find(Node node) {
	    	for( int i = 0; i < num; i++) {
	    		if(node == data[i]) {
	    			return i;//����Ԫ��node��λ��
	    		}
	    	}
	    	return -1;//����Ҳ���Ԫ��node���򷵻�-1
	    }
	    
	    //ɾ��ָ��λ�õ�index������Ԫ��
	    public Node remove(int index) {
	    	 //����
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
	    
	    //ɾ����������һ��Ԫ��
	    public Node removeLast() {
	    	return remove(num - 1);
	    }
	    
	    //ɾ��������׸�Ԫ��
	    public Node removeFirst() {
	    	return remove(0);
	    }
	    
	    //ɾ������Ԫ��node
	    public void removeElement(Node node) {
	    	int index = find(node);
	    	if(index != -1) {
	    		remove(index);
	    	}
	    	else {
	    		System.out.println("�����в�����Ԫ��node");
	    	}
	    }
	    
	    //Ԫ�ؽ���
	    public void swap(int i, int j) {
	    	Node t = data[i];
			data[i] = data[j];
			data[j] = t;
	    }
	}
	
	//��С�� �Զ�̬����Ϊ�ײ����ݽṹ
	class MinHeap  {
		private Arr data;
		
		
		//���캯��������һ����СΪcapacity�Ķ�̬����
		public MinHeap(int capacity) {
			data = new Arr(capacity);
		}
		
		//���캯��������һ����СΪ10�Ķ�̬����
		public MinHeap() {
			data = new Arr();
		}
		
		//���캯�� ���������鹹������ѵ���״
		public MinHeap(Node[] arr) {
			data = new Arr(arr);//���ݴ������������ɶ�̬���飻
			for(int i =parent(arr.length - 1); i >= 0; i--) {
				siftDown(i); //�����һ����Ҷ�ӽڵ�����³���һֱ�����ڵ�
			}
		}
		
		//�������ѵ�Ԫ�ظ���
		public int num() {
			return data.getNum();
		}
		
		//��ѯ�����Ƿ�Ϊ��
		public boolean isEmpty() {
			return data.isEmpty();
		}
		
		//����һ��������һ����������ʾԪ�صĸ��׽ڵ�����������û�и��ڵ㷵��-1
		private int parent(int index) {
			if(0 == index) {
				return -1;
			}
			return (index - 1)/2;
		}
		
		//��ȡ���ӽڵ������
		private int leftChild(int index) {
			return 2*index + 1;
		}
		
		//��ȡ�Һ��ӽڵ������
		private int rightChild(int index) {
			return 2*index + 2;
		}
		
		//��������Ԫ��
		public void add(Node node) {
			data.addLast(node);
			siftUp(data.getNum() - 1); //Ԫ���ϸ�
		}
		
		//���е�Ԫ���ϸ�
		private void siftUp(int k) {
			while(k > 0 && data.get(parent(k)).getCost() > data.get(k).getCost()) {
				data.swap(k, parent(k)); //�븸�ڵ㽻��
				k = parent(k); //k��Ϊ�丸�ڵ��������׼����һ��ѭ��
			}
		}
		
		//����Ԫ�ص��³�
		private void siftDown(int k) {
			while(leftChild(k) < data.getNum()) {
				int j = leftChild(k);
				if(j + 1 < data.getNum()) {
					if(data.get(j + 1).getCost() < data.get(j).getCost()) {
					j = rightChild(k);  //j���Һ�������Сֵ������
					}
					if(data.get(k).getCost() < data.get(j).getCost()) {
						break;   //����޷��³������κδ���
					}
				}else {
					if(data.get(k).getCost() < data.get(j).getCost()) {
						break;
					}		
				}
				data.swap(k, j);   //����Ԫ�����С���Ǹ����ӽڵ㽻��
				k = j;  //������k��Ϊj��׼����һ��ѭ��
			}
		}
		
		//��ö�����СԪ�ص�ֵ������ѿ��򷵻ؿ�
		public Node findMin() {
			if(data.getNum() == 0) {
				return null;
			}
			return data.get(0);
		}
		
		//ȡ��������СԪ�ص�ֵ
		public Node extractMin() {
			Node ret = findMin();
			data.swap(0,data.getNum() - 1);
			data.removeLast();
			siftDown(0);
			return ret;
		}
		
		//ȡ�����е���СԪ�أ������滻��Ԫ��node
		public Node replace(Node node) {
			Node ret = findMin();
			data.set(0, node);
			siftDown(0);
			return ret;
		}
		
		//ɾ�����е�Node
		public void removeNode(Node node) {
			data.swap(data.find(node),data.getNum() - 1);
			data.removeLast();
			siftDown(0);
		}
	}
	
	//���ȶ��У��ײ�ṹΪ����  ʵ�ֶ��нӿ�
	class PriorityQueue{

		private MinHeap minHeap;  //����һ������
		
		//���캯��������һ�����ȶ���
		public PriorityQueue() {
			minHeap = new MinHeap();
		}
		
		//Ԫ��node���
		public void enqueue(Node node) {
			minHeap.add(node);
		}
		
		//��С��Ԫ�س���
		public Node dequeue() {
			return minHeap.extractMin();
		}
		
		//������ȶ��ж�ͷԪ�ص�ֵ
		public Node getFront() {
			return minHeap.findMin();
		}
		
		//��ȡ���еĳ���
		public int getNum() {
			return minHeap.num();
		}
		
		//�ж϶����Ƿ�Ϊ��
		public boolean isEmpty() {
			return minHeap.isEmpty();
		}
		
		//ɾ��Ԫ��node
		public void nodeEnqueue(Node node) {
			minHeap.removeNode(node);
		}
	}
	
	//�ڵ�:��ŵ�ͼ�ڵ����Ϣ
	class Node {
		private int g;   //�����ڵ�Node�ѻ��ѵĴ���
		private int row,column;  //�ڵ������
		private int type;  //��ǰ�ڵ������
		private Node fathernode; //���ڵ�
		
		//���캯������ʼ���ڵ����Ϣ
		public Node(int row,int column,int type) {
			this.row =row;
			this.column = column;
			this.type = type;
		}

		//���ø��ڵ�
		public void setFatherNode(Node fathernode) {
			this.fathernode = fathernode;
		}
		
		//��������Node�ѻ��ѵĴ���
		public int getG() {
			return g;
		}
		
		//��������Node�ѻ��ѵĴ���
		public void setG(int g) {
			this.g =g;
		}
		
		//��ȡ���ڽڵ�Node�ܵĴ��۹���
		public int getCost() {
			return getHValue() + g;
		}
		
		//����ʽ����
		private int getHValue() {
			return (Math.abs(row - dest_row) + Math.abs(column - dest_column));
		}
		
		//��������
		public void setType(int type) {
			this.type = type;
		}
		
		//��������
		public int getType() {
			return type;
		}
	}
	
	
}
