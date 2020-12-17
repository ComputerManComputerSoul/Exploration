package calculate;

public class Constant{
	//Constant类:保存常量
	public final static int MAX_X=33;	//二维数组x范围
	public final static int MAX_Y=20;	//二维数组y范围
	public final static int INITIAL_ROOM=502;	//出生房间
	public final static int INITIAL_X=10;	//出生点x
	public final static int INITIAL_Y=22;	//出生点y
	public final static double FULL_POWER=100.0;		//满体力值
	public final static int INITIAL_SPEED=3;		//初始速度
	public final static int SHIFT_SPEED=3;		//加速跑速度倍数
	public final static double POWER_COST=0.05;		//加速移动一格需要花费的体力
	public final static int UNIT_LENGTH=30;		//单位距离的边长像素数
	public final static int FRAME_RATE=60;		//帧率
	public final static int VOLUMN_PERCENT=80;	//音量初始值
	public final static int INIT_SEX=0;			//初始性别
	public final static int A_MAX=20;			//A类最大物品数量
	public final static int B_MAX=20;			//B类最大物品数量
	public final static int ROOM_MAX_IMAGE=50;			//房间中物品的最大数量
	public final static int MAX_ROOM=5;			//最大房间数量
	public final static int FRIEND_ACCURACY=100000;	//好友移动优先级
	public final static int WRITER_SLEEP=1;	//发送信息线程的休眠时间
	public final static String HELP_TEXT="<html><br/>欢迎来到Exploration的世界！本游戏由Exploration项目组进行制作、维护与更新。<br/>"
			+ "鼠标左键点击地图可以进行自动寻路移动<br/>"
			+ "WASD/上下左右箭头:&#12288移动&#12288&#12288&#12288"
			+ "Shift:&#12288加速跑&#12288&#12288&#12288"
			+ "空格:&#12288语句跳过<br/>"
			+ "F:find&#12288交互&#12288&#12288&#12288"
			+ "E:eat&#12288饮食&#12288&#12288&#12288"
			+ "B:bag&#12288背包&#12288&#12288&#12288"
			+ "V:volume&#12288音量<br/>"
			+ "ESC:&#12288退出游戏&#12288&#12288&#12288"
			+ "H:help&#12288帮助&#12288&#12288&#12288"
			+ "O:online&#12288联机<br/>Enter:&#12288联机聊天&#12288&#12288&#12288"
			+ "P:progress&#12288保存进度&#12288&#12288&#12288"
			+ "<br/><br/><br/><br/>"
			+ "<br/>中文输入法会对键盘读取产生干扰,"
			+ "<br/>强烈建议将输入法改成ENG英语以获得流畅的游戏体验!"
			+"<br/><br/>如果有其他任何问题，请联系制作人: QQ 1731019653，"
			+ "Tel 18646393118<br/>感谢您体验Exploration！祝您玩的愉快！</html>";
	
	public final static String DEVELOPER_TEXT="<html>本游戏由Exploration项目组进行开发、维护与更新。<br/>"
			+ "Exploration项目组成立于哈尔滨工业大学（深圳）软A任务。<br/>Exploration项目组成员:<br/>&#12288董天泽"
			+ "<br/>&#12288周雨琦<br/><br/>联系我们:<br/>&#12288QQ:1731019653&#12288Tel:18646393118<br/><br/>"
			+ "测试者信息: <br/><br/>感谢测试者对游戏开发的大力支持！<br/>"
			+ "本游戏中部分素材来自于网络或实拍，仅可用作体验或学习交流，"
			+ "<br/>不会用作商业用途。如有侵权请联系开发者！<br/><br/><br/></html>";
	
	public final static String UPDATELOG_TEXT="<html><br/><br/>当前版本为V3.6.0	Version:V3.6.0<br/><br/>"
			+ "V3.6.0:游戏进度存取存储和读取功能开放  2020.10.29<br/>"
			+ "V3.5.1:信息发送和处理延迟降低  2020.10.28<br/>"
			+ "V3.5.0:聊天系统开放 好友角色移动朝向bug修复  2020.10.28<br/>"
			+ "V3.4.0:快速饮食开放 鼠标点击自动寻路移动开放  2020.10.27<br/>"
			+ "V3.3.0:广域网联机开放  2020.10.27<br/>"
			+ "V3.2.0:局域网联机开放  2020.10.26<br/>"
			+ "V3.1.2:背包系统逻辑优化  2020.10.10<br/>"
			+ "V3.1.1:更新背包系统  2020.10.10<br/>"
			+ "V3.1.0:增加背包系统,增加初始地图  2020.10.10<br/>"
			+ "V3.0.0:游戏大幅度重做,人机交互优化,界面重做 2020.9.29<br/>"
			+ "V2.0.0:游戏大幅度重做,新功能添加,人机交互优化 2020.8.24<br/>"
			+ "<br/>V1.2.0 beta:对音效文件与提示信息进行了更换 2020.7.12<br/>V1.2.0:"
			+ "添加了帮助和更新日志功能 2020.7.11<br/>"
+ "V1.1.2:修复了关闭提示窗口时音效无法停止的bug 2020.7.11<br/>V1.1.1:修复了关闭主程序时音效无法停止的bug 2020.7.11<br/>"
+ "V1.1.0:添加了音效功能 2020.7.11 <br/>V1.0.0:添加了成就功能 初代版本发布2020.7.10<br/>V0.0.0:开始开发本游戏 2020.7.9<br/><br/>开发者信息:本游戏由Exploration项目组"
+ "进行开发、维护与更新。<br/>QQ:1731019653 Tel:18646393118<br/>感谢您体验Exploration！祝您玩的愉快！<br/><br/><br/><br/></html>";
}
