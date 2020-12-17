package service;

import java.io.*;
import javax.sound.sampled.*;

//音乐播放器
public class MusicPlayer implements Runnable {
    File soundFile; // 音乐文件
    Thread thread;// 父线程
    boolean circulate;// 是否循环播放
    boolean continuePlay=true;
    float volumePercent;	//0到1的float
    boolean volumeNeedTemperoryChange=false;
    
    //构造方法: 传入音乐路径，播放音乐
    public MusicPlayer(String filepath,float volumePercent) throws FileNotFoundException {
        this(filepath,volumePercent,false);
    }
    
    public MusicPlayer(String filepath,float volumePercent,boolean circulate)
            throws FileNotFoundException {
    	this.volumePercent=volumePercent;
        this.circulate = circulate;
        soundFile = new File(filepath);
        if (!soundFile.exists()) {}
    }

    //播放
    public void play() {
        thread = new Thread(this);// 创建线程对象
        thread.start();// 开启线程
    }
    
    //停止播放
    public void stop(){
    	continuePlay=false;
    }
    
    //临时改变音量
    public void temporaryChangeVolumn(float volumePercent) {
    	this.volumePercent=volumePercent;
    	volumeNeedTemperoryChange=true;
    }
    
    //重写线程执行方法
    public void run() {
        //byte[] auBuffer = new byte[1024 * 128];// 创建128k缓冲区
    	byte[] auBuffer = new byte[1024 * 16];// 创建16k缓冲区
        do {
            AudioInputStream audioInputStream = null; // 创建音频输入流对象
            SourceDataLine auline = null; // 混频器源数据行
            try {
                // 从音乐文件中获取音频输入流
                audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                AudioFormat format = audioInputStream.getFormat(); // 获取音频格式
                // 按照源数据行类型和指定音频格式创建数据行对象
                DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                        format);
                // 利用音频系统类获得与指定 Line.Info 对象中的描述匹配的行，并转换为源数据行对象
                auline = (SourceDataLine) AudioSystem.getLine(info);
                auline.open(format);// 按照指定格式打开源数据行
                
                FloatControl control = (FloatControl) auline.getControl(FloatControl.Type.MASTER_GAIN);
                control.setValue(volumePercent*(control.getMaximum()-control.getMinimum())+control.getMinimum());
                 
                auline.start();// 源数据行开启读写活动
                int byteCount = 0;// 记录音频输入流读出的字节数
                while (byteCount != -1&&continuePlay) {// 如果音频输入流中读取的字节数不为-1
                	if(volumeNeedTemperoryChange) {
                		control.setValue(volumePercent*(control.getMaximum()-control.getMinimum())+control.getMinimum());
                		volumeNeedTemperoryChange=false;
                	}
                	// 从音频数据流中读出数据
                    byteCount = audioInputStream.read(auBuffer, 0,
                            auBuffer.length);
                    if (byteCount >= 0) {// 如果读出有效数据
                        auline.write(auBuffer, 0, byteCount);// 将有效数据写入数据行中
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } finally {
                auline.drain();// 清空数据行
                auline.close();// 关闭数据行
            }
        } while (circulate);// 根据循环标志判断是否循环播放
    }
}