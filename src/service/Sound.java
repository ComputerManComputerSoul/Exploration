package service;

import java.io.FileNotFoundException;

public class Sound {
	//Sound类:找到需要播放的音乐进行播放
	
    final String DIR = "music/";
    MusicPlayer player;
    
    //播放音乐
    public void play(String fileName,boolean circulate) {
    	if(fileName.indexOf(".wav")<0) {
    		play(DIR+fileName+".wav",FileDataHandler.getBGMPercent(),circulate);
    	}
    	else {
    		play(DIR+fileName,FileDataHandler.getBGMPercent(),circulate);
    	}
    }
    
    //临时更改音量
    public void temporaryChangeVolumn(float volumePercent) {
    	player.temporaryChangeVolumn(volumePercent);
    }
    
    //播放
    private void play(String file,float volumn,boolean circulate) {
        try {
            player = new MusicPlayer(file,volumn,circulate); // 创建播放器
            player.play();// 播放器开始播放
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void stopPlay() {
    	if(player!=null) player.stop();
    }
    
}
