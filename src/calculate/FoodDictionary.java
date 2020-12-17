package calculate;

import service.NumberProcesser;

//FoodDictionary��:ʳ��������
public class FoodDictionary {
	private final int foodKinds = Constant.B_MAX;	//B���鳤��
	private int foodPowers[] = new int [foodKinds];
	private NumberProcesser numberProcesser = new NumberProcesser();
	
	//���췽�� ��ʼ��ʳ��������
	public FoodDictionary(){
		initialFoodDictionary();
	}

	//���ʳ�����
	public void addFoodItem(int code,int power) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==2){
			foodPowers[itemNumber]=power;
		}
		
	}
	
	//��ѯʳ������
	public int getFoodPower(int code) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==2){
			return foodPowers[itemNumber];
		}
		else return -1;
	}
	
	//��ʼ��ʳ��������
	public void initialFoodDictionary() {
///////////////////////////////////////////////////////////////////////////////////////////////////
		addFoodItem(200,10);
		
	}
}
