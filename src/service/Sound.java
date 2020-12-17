package service;

import java.io.FileNotFoundException;

public class Sound {
	//Sound��:�ҵ���Ҫ���ŵ����ֽ��в���
	
    final String DIR = "music/";
    MusicPlayer player;
    
    //��������
    public void play(String fileName,boolean circulate) {
    	if(fileName.indexOf(".wav")<0) {
    		play(DIR+fileName+".wav",FileDataHandler.getBGMPercent(),circulate);
    	}
    	else {
    		play(DIR+fileName,FileDataHandler.getBGMPercent(),circulate);
    	}
    }
    
    //��ʱ��������
    public void temporaryChangeVolumn(float volumePercent) {
    	player.temporaryChangeVolumn(volumePercent);
    }
    
    //����
    private void play(String file,float volumn,boolean circulate) {
        try {
            player = new MusicPlayer(file,volumn,circulate); // ����������
            player.play();// ��������ʼ����
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void stopPlay() {
    	if(player!=null) player.stop();
    }
    
}
