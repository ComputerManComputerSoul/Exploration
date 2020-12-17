package calculate;

public class Door {
	//Door类：存储门数据
	
	private int doorNumber;
	private boolean canEnter;
	private int targetRoom;
	private int targetX;
	private int targetY;
	
	//构造函数：创建门
	public Door(int doorNumber,int canEnterCode) {
		this.doorNumber=doorNumber;
		canEnter=false;
		if(canEnterCode==1) {
			canEnter=true;
			createDoor();
		}
		else if(canEnterCode==0){
			
		}
	}
	
	//初始化一个门
	public void createDoor() {
		switch(doorNumber) {
			case 50200:
				
				break;
			default:
				
				break;
		}
	}
	
	//获取是否能进入
	public boolean getCanEnter() {
		 return canEnter;
	}
	
	//获取目标房间
	public int getTargetRoom() {
		if(canEnter) return targetRoom;
		else return -1;
	}
		
	//获取目标x坐标
	public int getTargetX() {
		if(canEnter) return targetX;
		else return -1;
	}
	
	//获取目标y坐标
	public int getTargetY() {
		if(canEnter) return targetY;
		else return -1;
	}
		
	
		
}
