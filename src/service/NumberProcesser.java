package service;

//numberProcesser�ࣺ���ִ�����
public class NumberProcesser {
	
	//��ñ������λ
	public int headSplit(int originCode) {
		String numberString=""+originCode;
		return Integer.parseInt(numberString.substring(0,1));
	}
			
	//��ñ����ʣ��λ
	public int endSplit(int originCode) {
		String numberString=""+originCode;
		numberString=numberString.substring(1);
		if(numberString.length()>0) return Integer.parseInt(numberString);
		else return -1;
	}
	
	//�ڱ����ǰ���������
	public int addHeadNumber(int originCode,int headNumber) {
		String numberString=""+headNumber+originCode;	
		return Integer.parseInt(numberString);
	}
}
