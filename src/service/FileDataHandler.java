package service;

import java.io.*;
import java.util.Arrays;

import calculate.Constant;
import calculate.LinkQueue;


public class FileDataHandler {
	private static final String SETTINGFILE = "data/Setting";// 音量记录文件
	private static final String PROGERSSFILE = "data/Progress";// 游戏进度记录文件
	private static int[] volumes=new int[3];	
	private static int sex;	//0:男 1:女
	//MasterVolume,BGMVolume,SoundVolume 0-100范围
    private static final int initVolumnPercent=Constant.VOLUMN_PERCENT;
    private static final int initSex=Constant.INIT_SEX;
	
	//初始化音量
	public static void initSettings() {
		 File f = new File(SETTINGFILE);// 创建记录文件
		 if (!f.exists()) {// 如果文件不存在
			 try {
	                f.createNewFile();// 创建新文件
	                Arrays.fill(volumes, initVolumnPercent);// 数组填充
	                sex=initSex;              
	                saveSettings();
	            } catch (IOException e) {}
	            return;// 停止方法
	        }
	        FileInputStream fis = null;
	        InputStreamReader isr = null;
	        BufferedReader br = null;
	        try {
	            fis = new FileInputStream(f);// 文件字节输入流
	            isr = new InputStreamReader(fis);// 字节流转字符流
	            br = new BufferedReader(isr);// 缓冲字符流
	            String value = br.readLine();// 读取一行
	            if (!(value == null || "".equals(value))) {// 如果不为空值
	                String vs[] = value.split(",");// 分割字符串
	                if (vs.length != 4) {
	                    Arrays.fill(volumes,initVolumnPercent);// 数组填充70
	                    sex=initSex;
	                    saveSettings();
	                } else {
	                    for (int i=0;i<3;i++) {
	                        // 将记录文件中的值赋给当前分数数组
	                    	 volumes[i]=Integer.parseInt(vs[i]);
	                    }
	                    sex=Integer.parseInt(vs[3]);
	                }
	            }
	        } catch (FileNotFoundException e) {
	        } catch (IOException e) {
	        } finally {// 依次关闭流
	            try {
	                br.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            try {
	                isr.close();
	            } catch (IOException e) {
	            }
	            try {
	                fis.close();
	            } catch (IOException e) {
	            }

	        }
	}

	//存储音量值
	public static void saveSettings() {
        String settings = volumes[0] + "," + volumes[1] + "," + volumes[2]+","+sex;
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(SETTINGFILE);// 文件字节输出流
            osw = new OutputStreamWriter(fos);// 字节流转字符流
            bw = new BufferedWriter(osw);// 缓冲字符流
            bw.write(settings);// 写入拼接后的字符串
            bw.flush();// 字符流刷新
        } catch (FileNotFoundException e) {
        	initSettings();
        	saveSettings();
        } catch (IOException e) {
        } finally {// 依次关闭流
            try {
                bw.close();
            } catch (IOException e) {
            }
            try {
                osw.close();
            } catch (IOException e) {
            }
            try {
                fos.close();
            } catch (IOException e) {
            }
        }
		
	}
	
	//改变三个音量值
	public static void changeVolulmes(int receiveVolumes[]) {
		for(int i=0;i<3;i++) {
			volumes[i]=receiveVolumes[i];
		}
	}
	
	//获取三个音量值
	public static int[] getVolumes() {
		return volumes;
	}
	
	//获取音乐音量百分数
	public static float getBGMPercent() {
		float BGMPercent= ((float)(volumes[0]*volumes[1]))/10000;
		return BGMPercent;
	}
	
	//获取音效音量百分数
	public static float getSoundEffectPercent() {
		float BGMPercent= ((float)(volumes[0]*volumes[2]))/10000;
		return BGMPercent;
	}
	
	//改变性别
	public static void changeSex() {
		sex=1-sex;
	}
	
	//改变性别
	public static void changeSex(int targetSex) {
		sex=targetSex%2;
	}
	
	//获取性别
	public static int getSex() {
		return sex;
	}
	
	//存储游戏进度
	public static void saveProgress(LinkQueue progressQueue) {
		 File f = new File(PROGERSSFILE);// 创建记录文件
		 if (!f.exists()) {// 如果文件不存在
			 try {
	                f.createNewFile();// 创建新文件
	            } catch (IOException e) {}
	    }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(PROGERSSFILE);// 文件字节输出流
            osw = new OutputStreamWriter(fos);// 字节流转字符流
            bw = new BufferedWriter(osw);// 缓冲字符流
            while(!progressQueue.QueueEmpty()) {
            	bw.write(progressQueue.DeQueue());// 写入拼接后的字符串
            	bw.newLine();
			}
            bw.flush();// 字符流刷新
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {// 依次关闭流
            try {
                bw.close();
            } catch (IOException e) {
            }
            try {
                osw.close();
            } catch (IOException e) {
            }
            try {
                fos.close();
            } catch (IOException e) {
            }
        }
	}
	
	//判断文件是否存在
	public static boolean progressFileExist() {
		File f = new File(PROGERSSFILE);// 创建记录文件
		if (f.exists()) return true;
		else return false;
	}
	
	//读取游戏进度
	public static LinkQueue loadProgress() {
		LinkQueue progressQueue = new LinkQueue();
		File f = new File(PROGERSSFILE);// 创建记录文件
		if (!f.exists()) {// 如果文件不存在
			try {
				f.createNewFile();
	        } catch (IOException e) {}
	        return progressQueue;// 停止方法
	    } 
		FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(f);// 文件字节输入流
            isr = new InputStreamReader(fis);// 字节流转字符流
            br = new BufferedReader(isr);// 缓冲字符流
            String s=null;
            while((s=br.readLine())!=null) {
            	progressQueue.EnQueue(s);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {// 依次关闭流
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                isr.close();
            } catch (IOException e) {
            }
            try {
                fis.close();
            } catch (IOException e) {
            }

        }
		return progressQueue;
	}
}
