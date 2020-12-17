package service;

//numberProcesser类：数字处理器
public class NumberProcesser {
	
	//获得编码的首位
	public int headSplit(int originCode) {
		String numberString=""+originCode;
		return Integer.parseInt(numberString.substring(0,1));
	}
			
	//获得编码的剩余位
	public int endSplit(int originCode) {
		String numberString=""+originCode;
		numberString=numberString.substring(1);
		if(numberString.length()>0) return Integer.parseInt(numberString);
		else return -1;
	}
	
	//在编码的前面添加数字
	public int addHeadNumber(int originCode,int headNumber) {
		String numberString=""+headNumber+originCode;	
		return Integer.parseInt(numberString);
	}
}
