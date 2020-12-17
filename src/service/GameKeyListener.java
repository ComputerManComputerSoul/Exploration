package service;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import calculate.Constant;
import calculate.MyData;
import view.BagDialog;
import view.GameFrame;
import view.TextDialog;

public class GameKeyListener extends KeyAdapter{
	//GameKeyListener��:��Ϸ���̼�����  (�̳�KeyAdapter)
	
	private GameFrame gameFrame;
	private MoveThread moveThread;
	private MyData myData;
	
	//���캯��:����gameFrame
	public GameKeyListener(GameFrame gameFrame) {
		this.gameFrame=gameFrame;
		this.moveThread=gameFrame.getMoveThread();
		myData=gameFrame.getMyData();
	}
	
	//���̰���ʱ
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		//System.out.println("keypressed"+code);
		switch (code) {
			case 38:	//w �ϼ�ͷ  �ƶ�:��
			case 87:
				moveThread.setDirection(0,true);
				break;
			case 40:	//s �¼�ͷ  �ƶ�:��
			case 83:	
				moveThread.setDirection(1,true);
				break;
			case 37:	//a ���ͷ  �ƶ�:��
			case 65:
				moveThread.setDirection(2,true);
				break;
			case 39:	//d �Ҽ�ͷ  �ƶ�:��
			case 68:
				moveThread.setDirection(3,true);
				break;
			case 66:	//b ����
				gameFrame.bagDialogSetVisible(true);
				break;
			case 16:	//shift ������
				moveThread.setShift(true);
				break;
			case 70: 	//f ����
				myData.visit();
				break;
			case 69:	//e ��ʳ(�����ָ�)
				myData.getBag().quickEat();
				break;
			case 32:	//�ո� �������
				gameFrame.turnMiddleMessage();
				break;
			case 27:	//esc �˳���Ϸ
				gameFrame.whetherExit();
				break;
			case 72:	//H ����
				new TextDialog(gameFrame,"����",900,500,Constant.HELP_TEXT);
				break;
			case 86:	//V ��������
				gameFrame.changeVolume();
				break;
			case 79:	//O ��������
				if(myData.getPlayerStatus()==0||myData.getPlayerStatus()==5) {
					gameFrame.whetherCreateRoom();
				}
				break;
			case 10:	//Enter ��������
				if(myData.getPlayerStatus()>0&&myData.getPlayerStatus()<5) {
					gameFrame.showChatDialog();
				}
				break;
			case 80:	//P �������
				gameFrame.whetherSaveProgress();
				break;
			default:
			
				break;
		}
	}
	
	//�����ͷ�ʱ
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
			case 38:	//w �ϼ�ͷ  �ƶ�:��
			case 87:
				moveThread.setDirection(0,false);
				break;
			case 40:	//s �¼�ͷ  �ƶ�:��
			case 83:	
				moveThread.setDirection(1,false);
				break;
			case 37:	//a ���ͷ  �ƶ�:��
			case 65:
				moveThread.setDirection(2,false);
				break;
			case 39:	//d �Ҽ�ͷ  �ƶ�:��
			case 68:
				moveThread.setDirection(3,false);
				break;
			case 16:	//shift ������
				moveThread.setShift(false);
				break;
			default:
			
				break;
		}
	}
}
