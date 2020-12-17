package calculate;

import service.NumberProcesser;

//FoodDictionary类:食物体力表
public class FoodDictionary {
	private final int foodKinds = Constant.B_MAX;	//B数组长度
	private int foodPowers[] = new int [foodKinds];
	private NumberProcesser numberProcesser = new NumberProcesser();
	
	//构造方法 初始化食物体力表
	public FoodDictionary(){
		initialFoodDictionary();
	}

	//添加食物词条
	public void addFoodItem(int code,int power) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==2){
			foodPowers[itemNumber]=power;
		}
		
	}
	
	//查询食物体力
	public int getFoodPower(int code) {
		int itemKind=numberProcesser.headSplit(code);
		int itemNumber=numberProcesser.endSplit(code);
		if(itemKind==2){
			return foodPowers[itemNumber];
		}
		else return -1;
	}
	
	//初始化食物体力表
	public void initialFoodDictionary() {
///////////////////////////////////////////////////////////////////////////////////////////////////
		addFoodItem(200,10);
		
	}
}
