package calculate;

public class Door {
	//Door�ࣺ�洢������
	
	private int doorNumber;
	private boolean canEnter;
	private int targetRoom;
	private int targetX;
	private int targetY;
	
	//���캯����������
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
	
	//��ʼ��һ����
	public void createDoor() {
		switch(doorNumber) {
			case 50200:
				
				break;
			default:
				
				break;
		}
	}
	
	//��ȡ�Ƿ��ܽ���
	public boolean getCanEnter() {
		 return canEnter;
	}
	
	//��ȡĿ�귿��
	public int getTargetRoom() {
		if(canEnter) return targetRoom;
		else return -1;
	}
		
	//��ȡĿ��x����
	public int getTargetX() {
		if(canEnter) return targetX;
		else return -1;
	}
	
	//��ȡĿ��y����
	public int getTargetY() {
		if(canEnter) return targetY;
		else return -1;
	}
		
	
		
}
