package service;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import calculate.Constant;
import calculate.MyData;
import view.BagDialog;
import view.GameFrame;
import view.TextDialog;

public class GameKeyListener extends KeyAdapter{
	//GameKeyListener类:游戏键盘监听器  (继承KeyAdapter)
	
	private GameFrame gameFrame;
	private MoveThread moveThread;
	private MyData myData;
	
	//构造函数:导入gameFrame
	public GameKeyListener(GameFrame gameFrame) {
		this.gameFrame=gameFrame;
		this.moveThread=gameFrame.getMoveThread();
		myData=gameFrame.getMyData();
	}
	
	//键盘按下时
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		//System.out.println("keypressed"+code);
		switch (code) {
			case 38:	//w 上箭头  移动:上
			case 87:
				moveThread.setDirection(0,true);
				break;
			case 40:	//s 下箭头  移动:下
			case 83:	
				moveThread.setDirection(1,true);
				break;
			case 37:	//a 左箭头  移动:左
			case 65:
				moveThread.setDirection(2,true);
				break;
			case 39:	//d 右箭头  移动:右
			case 68:
				moveThread.setDirection(3,true);
				break;
			case 66:	//b 背包
				gameFrame.bagDialogSetVisible(true);
				break;
			case 16:	//shift 加速跑
				moveThread.setShift(true);
				break;
			case 70: 	//f 交互
				myData.visit();
				break;
			case 69:	//e 饮食(体力恢复)
				myData.getBag().quickEat();
				break;
			case 32:	//空格 语句跳过
				gameFrame.turnMiddleMessage();
				break;
			case 27:	//esc 退出游戏
				gameFrame.whetherExit();
				break;
			case 72:	//H 帮助
				new TextDialog(gameFrame,"帮助",900,500,Constant.HELP_TEXT);
				break;
			case 86:	//V 音量调整
				gameFrame.changeVolume();
				break;
			case 79:	//O 创建房间
				if(myData.getPlayerStatus()==0||myData.getPlayerStatus()==5) {
					gameFrame.whetherCreateRoom();
				}
				break;
			case 10:	//Enter 联机聊天
				if(myData.getPlayerStatus()>0&&myData.getPlayerStatus()<5) {
					gameFrame.showChatDialog();
				}
				break;
			case 80:	//P 保存进度
				gameFrame.whetherSaveProgress();
				break;
			default:
			
				break;
		}
	}
	
	//键盘释放时
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
			case 38:	//w 上箭头  移动:上
			case 87:
				moveThread.setDirection(0,false);
				break;
			case 40:	//s 下箭头  移动:下
			case 83:	
				moveThread.setDirection(1,false);
				break;
			case 37:	//a 左箭头  移动:左
			case 65:
				moveThread.setDirection(2,false);
				break;
			case 39:	//d 右箭头  移动:右
			case 68:
				moveThread.setDirection(3,false);
				break;
			case 16:	//shift 加速跑
				moveThread.setShift(false);
				break;
			default:
			
				break;
		}
	}
}
