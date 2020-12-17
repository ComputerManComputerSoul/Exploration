package service;


import calculate.Constant;
import calculate.LinkQueue;
import calculate.MyData;
import calculate.PathSearcher;
import view.GameFrame;

public class MoveThread extends Thread {
	//MoveThread类:移动线程
	
	private GameFrame gameFrame;
    private int direction = 4;	//4:不移动 0:上，1:下，2:左，3:右
    private MyData myData;
    private boolean pressShift=false;	//是否按下shift
    private boolean isContinue=true;	//线程是否继续
    private PathSearcher pathSearcher;
    private LinkQueue moveOrdersQueue = new LinkQueue();
    
    private final int originSpeed=Constant.INITIAL_SPEED;		//初始速度
    private final double powerCost=Constant.POWER_COST;		//加速移动一格体力值消耗
    private final int refreshTime=1000/Constant.FRAME_RATE;		//刷新间隔时间
    private final int shiftSpeed=Constant.SHIFT_SPEED;		//加速跑速度倍数
    private final double fullPower=Constant.FULL_POWER;		//满体力值
    private final int friendAccuracy=Constant.FRIEND_ACCURACY;	//好友移动速度精确度
    
	//构造函数:传入myFrame
	public MoveThread(GameFrame gameFrame) {
		this.gameFrame=gameFrame;
		myData=gameFrame.getMyData();
		pathSearcher= new PathSearcher(myData);
	}
	
	//线程开始
	@Override
	public void run() {
		int singleDirection;
		while(isContinue) {
			singleDirection=direction;
			if(singleDirection==4) {
				if(moveOrdersQueue.QueueEmpty()) {
					try {
						sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				else {
					singleDirection=moveOrdersQueue.DeQueue();
				}
			}
			else{
				if(!moveOrdersQueue.QueueEmpty()) moveOrdersQueue = new LinkQueue();
			}
			myData.setDirection(singleDirection);
			gameFrame.refreshStopLocation(singleDirection);
//			System.out.println("run:"+singleDirection);
			double originX=(double)myData.getX();	//保存起点x坐标
			double originY=(double)myData.getY();	//保存起点y坐标
			boolean canMove=myData.move(singleDirection);
//			System.out.println("移动至"+myData.getX()+","+myData.getY());
			if(canMove) {		//若能移动
				double changeX=(double)myData.getX()-originX; //x坐标改变总量
				double changeY=(double)myData.getY()-originY; //y坐标改变总量
				double speedRate=getSpeedRate();
				if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) {
					double doubleSpeed = speedRate*originSpeed;
					int intSpeed = (int)doubleSpeed;
					int speedAfterPoint=(int)((doubleSpeed-intSpeed)*friendAccuracy);
					myData.sendMessage("5,1,"+myData.getRoom().getRoomNumber()+","+
					singleDirection+","+myData.getX()+","+myData.getY()+","+intSpeed+","+speedAfterPoint);
				}
//				System.out.println("速度倍率"+speedRate);
				
				try {	//根据速度设定休眠时间
					int moveTimeCost=(int) (1000/speedRate/originSpeed);	//每次移动时间消耗
					double targetX;
					double targetY;
					for(int usedTime=0;usedTime<moveTimeCost;usedTime+=refreshTime) {
						targetX=originX+changeX*usedTime/moveTimeCost;
						targetY=originY+changeY*usedTime/moveTimeCost;
						gameFrame.refreshRunLocation(targetX,targetY,singleDirection);
//						System.out.println("时间设定 :"+usedTime+","+moveTimeCost);
//						System.out.println(" "+changeX*usedTime+","+changeX*usedTime);
//						System.out.println("位置设定 :"+targetX+","+targetY);
						sleep(refreshTime);
					}
					gameFrame.refreshStopLocation(singleDirection);
				} catch (InterruptedException e) {}
				if(speedRate>1) {
					myData.changePower(-powerCost);
				}
			}
			else {		//若不能移动
				if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) {
					myData.sendMessage("5,0,"+direction);
				}
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//设定移动方向
	public void setDirection(int stateNumber,boolean targetState) {
		if(stateNumber>=0&&stateNumber<4){
			if(targetState) {
//				System.out.println("setDirection"+stateNumber);
				if(direction==4) direction=stateNumber;
			}
			else {
				if(direction==stateNumber) {
					direction=4;
				}
			}
		}
	}
	
	//设定是否按下shift
	public void setShift(boolean targetState) {
//		System.out.println("setShift: "+targetState);
		pressShift=targetState;
	}
	
	//获得速度倍率
	public double getSpeedRate() {
		if(myData.getPower()>fullPower/2) {
			if(pressShift) return shiftSpeed;
			else return 1;
		}
		else {
			if(pressShift) return myData.getPower()*shiftSpeed/fullPower+1;
			else return 1;
		}
	}
	
	//寻路
	public void pathSearch(int x, int y) {	
		moveOrdersQueue = new LinkQueue();
//		System.out.println("点击目标坐标: "+x+","+y);
		int moveOrders[] = pathSearcher.search(x,y);
		for(int i=0;i<moveOrders.length&&moveOrders[i]!=-1;i++) {		
			moveOrdersQueue.EnQueue(moveOrders[i]);
		}
	}	
	
	//Queue 队列
	class LinkQueue{
	    private QueueNode front;	//队首
	    private QueueNode rear;		//队尾
	    private int size;
	    public LinkQueue() {
	    	front=new QueueNode(-1);
	    	rear=front;
		    size=0;
	    }

	    // 判断队列是否为空 
	    public boolean QueueEmpty(){
		    return size==0;
		}
	    
	    //入队
		public void EnQueue(int message){
		    QueueNode node = new QueueNode(message);
		    rear.next=node;
		    rear=node;
		    size++;
		}
		
		//出队
		public int DeQueue(){
		    if(QueueEmpty()) return -1;
		    int tempMessage;
		    if(front.next==rear) rear=front;
		    QueueNode p=front.next;
		    tempMessage=p.message;
		    front.next=p.next;
		    size--;
		    return tempMessage;
		}
		
		//返回队头 
		public int GetHead(){
		    if(QueueEmpty()) return -1;
		    return front.next.message;
		}
		
		//返回队尾 
		public int GetRear(){
		    if(QueueEmpty()) return -1;
		    return rear.message;
		}
		
		//QueueNode 内部类 队列结点
		class QueueNode{
		    private int message;
		    private QueueNode next;
		    public QueueNode(int message,QueueNode next) {
		    	this.message=message;
		    	this.next=next;
		    }
		    public QueueNode(int message) {
		    	this.message=message;
		    	next=null;
		    }
		}
	}
}
