package calculate;

public class Room {
	//Room类：当前房间数据
	
	private final int roomMaxImage=Constant.ROOM_MAX_IMAGE;
	private int roomNumber;
	private String roomName;
	private int xLength;
	private int yLength;
	private int[][] map;
	private RoomImage[] images = new RoomImage[roomMaxImage];
	private int beginX;		//人物起始像素X
	private int beginY;		//人物起始像素Y
	private int imageX;		//图片起始像素X
	private int imageY;		//图片起始像素Y
	
	//构造函数
	public Room(int roomNumber) {
		this.roomNumber=roomNumber;
		switch(roomNumber) {
			case 502:
				roomName="T3502";
				xLength=34;
				yLength=27;
				int tempMap[][]=new int[xLength][yLength];
				for(int i=0;i<xLength;i++) {
					for(int j=0;j<yLength;j++) {
						tempMap[i][j]=1;
						if((i>1&&i<8)||(i>9&&i<24)||(i>25&&i<32)) {
							if(j>4&&j<22&&j%2==1) {
								tempMap[i][j]=203;
							}
							if(j==23) {
								tempMap[i][j]=202;
							}
						}
						if(i==0||j==0||i==33||j==26) {
							tempMap[i][j]=6;
						}
					}
				}
				tempMap[6][0]=5150;
				tempMap[27][0]=5151;
				tempMap[9][2]=401;
				tempMap[2][21]=402;
				tempMap[10][2]=201;
				map=tempMap;
				break;
			default:
				break;
		}
		initialEmptyRoom();
	}
	
	//初始化空房间
	public void initialEmptyRoom() {
		switch(roomNumber) {
			case 502:
				beginX=240;
				beginY=10;
				imageX=240;
				imageY=10;
				
				images[0]=new RoomImage("floor.png",960,810,imageX+30,10,200);
				images[1]=new RoomImage("blackboard.jpg",1300,60,imageX+30,0,960,60,250);
				images[2]=new RoomImage("door.png",30,60,imageX+180,0,300);
				images[3]=new RoomImage("door.png",30,60,imageX+810,0,300);
				images[4]=new RoomImage("teacherDesk.png",83,52,imageX+257,imageY+52,300);
				for(int i=0;i<27;i++) {
					images[5+i]=new RoomImage("shortDesk.png",200,40,imageX+50,imageY+145+20*i,300);
					i++;
					images[5+i]=new RoomImage("longDesk.png",438,47,imageX+292,imageY+120+20*i,300);
					i++;
					images[5+i]=new RoomImage("shortDesk.png",200,40,imageX+770,imageY+105+20*i,300);
				}
				images[32]=new RoomImage("shortSeat.png",200,40,imageX+50,imageY+685,300);
				images[33]=new RoomImage("longSeat.png",438,47,imageX+292,imageY+682,300);
				images[34]=new RoomImage("shortSeat.png",200,40,imageX+770,imageY+685,300);
				break;
			default:
				break;
		}
	}
	
	//初始化房间物品image
	public void initialItemImage() {
		switch(roomNumber) {
			case 502:
				images[35]=new RoomImage("math.png",20,10,imageX+279,imageY+60,800,50,map[9][2]==401);
				images[36]=new RoomImage("paperParcel.png",20,10,imageX+60,imageY+642,800,50,map[2][21]==402);
				System.out.println("initialItemImage():2,21:"+map[2][21]);
				break;
			default:
				break;
		}
	}
	
	//获取房间名
	public String getRoomName() {
		return roomName;
	}
		
	//image是否为空
	public boolean imageIsNull(int imageNumber) {
		if(images[imageNumber]==null) return true;
		else return false;
	}
		
	//获取起始像素x
	public int getBeginX() {
		return beginX;
	}
	
	//获取起始像素y
	public int getBeginY() {
		return beginY;
	}
	
	//获取房间号
	public int getRoomNumber() {
		return roomNumber;
	}
	
	//查看map数组在(x,y)坐标的值
	public int seeLocation(int x,int y) {
		return map[x][y];
	}
		
	//将map数组在(x,y)坐标改变
	public void changeLocation(int x,int y,int targetNumber) {
		map[x][y]=targetNumber;
	}
	
	//获取图片文件名
	public String getFileName(int number) {
		return roomName+"/"+images[number].getFileName();
	}
			
	//获取图片边界位置
	public int[] getBounds(int number) {
		return images[number].getBounds();
	}
	
	//获得数组
	public int[][] getMap() {
		return map;
	}
	
	//获得翻转数组
	public int[][] getTurnMap() {
		int[][] turnMap = new int[yLength][xLength];
		for(int i=0;i<yLength;i++) {
			for(int j=0;j<xLength;j++) {
				turnMap[i][j]=map[j][i];
			}
		}
		return turnMap;
	}
			
	//获取图片边放置层数
	public int getLayer(int number) {
		return images[number].getLayer();
	}
	
	//RoomImage内部类:存储图片显示方法
	class RoomImage{
		private String fileRame;
		private int[] bounds=new int[6];
		private int layer;
		
		//构造方法：初始化变量
		public RoomImage(String fileRame,int imageWidth,int imageHeight,int x,int y,int labelWidth,int labelHeight,int layer){
			this.fileRame=fileRame;
			bounds[0]=imageWidth;
			bounds[1]=imageHeight;
			bounds[2]=x;
			bounds[3]=y;
			bounds[4]=labelWidth;
			bounds[5]=labelHeight;
			this.layer=layer;
		}
		
		//构造方法：初始化变量
		public RoomImage(String fileRame,int imageWidth,int imageHeight,int x,int y,int layer){
			this.fileRame=fileRame;
			bounds[0]=imageWidth;
			bounds[1]=imageHeight;
			bounds[2]=x;
			bounds[3]=y;
			bounds[4]=imageWidth;
			bounds[5]=imageHeight;
			this.layer=layer;
		}
		
		public RoomImage(String fileRame,int imageWidth,int imageHeight,int x,int y,int trueLayer,int falseLayer,boolean itemExist){
			this.fileRame=fileRame;
			bounds[0]=imageWidth;
			bounds[1]=imageHeight;
			bounds[2]=x;
			bounds[3]=y;
			bounds[4]=imageWidth;
			bounds[5]=imageHeight;
			if(itemExist) this.layer=trueLayer;
			else this.layer=falseLayer;
		}
		
		//获取图片文件名
		public String getFileName() {
			return fileRame;
		}
		
		//获取图片边界位置
		public int[] getBounds() {
			return bounds;
		}
		
		//获取图片边放置层数
		public int getLayer() {
			return layer;
		}
	}

	
	


}
