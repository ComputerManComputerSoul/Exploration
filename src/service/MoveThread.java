package service;


import calculate.Constant;
import calculate.LinkQueue;
import calculate.MyData;
import calculate.PathSearcher;
import view.GameFrame;

public class MoveThread extends Thread {
	//MoveThread��:�ƶ��߳�
	
	private GameFrame gameFrame;
    private int direction = 4;	//4:���ƶ� 0:�ϣ�1:�£�2:��3:��
    private MyData myData;
    private boolean pressShift=false;	//�Ƿ���shift
    private boolean isContinue=true;	//�߳��Ƿ����
    private PathSearcher pathSearcher;
    private LinkQueue moveOrdersQueue = new LinkQueue();
    
    private final int originSpeed=Constant.INITIAL_SPEED;		//��ʼ�ٶ�
    private final double powerCost=Constant.POWER_COST;		//�����ƶ�һ������ֵ����
    private final int refreshTime=1000/Constant.FRAME_RATE;		//ˢ�¼��ʱ��
    private final int shiftSpeed=Constant.SHIFT_SPEED;		//�������ٶȱ���
    private final double fullPower=Constant.FULL_POWER;		//������ֵ
    private final int friendAccuracy=Constant.FRIEND_ACCURACY;	//�����ƶ��ٶȾ�ȷ��
    
	//���캯��:����myFrame
	public MoveThread(GameFrame gameFrame) {
		this.gameFrame=gameFrame;
		myData=gameFrame.getMyData();
		pathSearcher= new PathSearcher(myData);
	}
	
	//�߳̿�ʼ
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
			double originX=(double)myData.getX();	//�������x����
			double originY=(double)myData.getY();	//�������y����
			boolean canMove=myData.move(singleDirection);
//			System.out.println("�ƶ���"+myData.getX()+","+myData.getY());
			if(canMove) {		//�����ƶ�
				double changeX=(double)myData.getX()-originX; //x����ı�����
				double changeY=(double)myData.getY()-originY; //y����ı�����
				double speedRate=getSpeedRate();
				if(myData.getPlayerStatus()==2||myData.getPlayerStatus()==4) {
					double doubleSpeed = speedRate*originSpeed;
					int intSpeed = (int)doubleSpeed;
					int speedAfterPoint=(int)((doubleSpeed-intSpeed)*friendAccuracy);
					myData.sendMessage("5,1,"+myData.getRoom().getRoomNumber()+","+
					singleDirection+","+myData.getX()+","+myData.getY()+","+intSpeed+","+speedAfterPoint);
				}
//				System.out.println("�ٶȱ���"+speedRate);
				
				try {	//�����ٶ��趨����ʱ��
					int moveTimeCost=(int) (1000/speedRate/originSpeed);	//ÿ���ƶ�ʱ������
					double targetX;
					double targetY;
					for(int usedTime=0;usedTime<moveTimeCost;usedTime+=refreshTime) {
						targetX=originX+changeX*usedTime/moveTimeCost;
						targetY=originY+changeY*usedTime/moveTimeCost;
						gameFrame.refreshRunLocation(targetX,targetY,singleDirection);
//						System.out.println("ʱ���趨 :"+usedTime+","+moveTimeCost);
//						System.out.println(" "+changeX*usedTime+","+changeX*usedTime);
//						System.out.println("λ���趨 :"+targetX+","+targetY);
						sleep(refreshTime);
					}
					gameFrame.refreshStopLocation(singleDirection);
				} catch (InterruptedException e) {}
				if(speedRate>1) {
					myData.changePower(-powerCost);
				}
			}
			else {		//�������ƶ�
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
	
	//�趨�ƶ�����
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
	
	//�趨�Ƿ���shift
	public void setShift(boolean targetState) {
//		System.out.println("setShift: "+targetState);
		pressShift=targetState;
	}
	
	//����ٶȱ���
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
	
	//Ѱ·
	public void pathSearch(int x, int y) {	
		moveOrdersQueue = new LinkQueue();
//		System.out.println("���Ŀ������: "+x+","+y);
		int moveOrders[] = pathSearcher.search(x,y);
		for(int i=0;i<moveOrders.length&&moveOrders[i]!=-1;i++) {		
			moveOrdersQueue.EnQueue(moveOrders[i]);
		}
	}	
	
	//Queue ����
	class LinkQueue{
	    private QueueNode front;	//����
	    private QueueNode rear;		//��β
	    private int size;
	    public LinkQueue() {
	    	front=new QueueNode(-1);
	    	rear=front;
		    size=0;
	    }

	    // �ж϶����Ƿ�Ϊ�� 
	    public boolean QueueEmpty(){
		    return size==0;
		}
	    
	    //���
		public void EnQueue(int message){
		    QueueNode node = new QueueNode(message);
		    rear.next=node;
		    rear=node;
		    size++;
		}
		
		//����
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
		
		//���ض�ͷ 
		public int GetHead(){
		    if(QueueEmpty()) return -1;
		    return front.next.message;
		}
		
		//���ض�β 
		public int GetRear(){
		    if(QueueEmpty()) return -1;
		    return rear.message;
		}
		
		//QueueNode �ڲ��� ���н��
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
