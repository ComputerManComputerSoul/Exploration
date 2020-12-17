package service;

import java.io.*;
import java.util.Arrays;

import calculate.Constant;
import calculate.LinkQueue;


public class FileDataHandler {
	private static final String SETTINGFILE = "data/Setting";// ������¼�ļ�
	private static final String PROGERSSFILE = "data/Progress";// ��Ϸ���ȼ�¼�ļ�
	private static int[] volumes=new int[3];	
	private static int sex;	//0:�� 1:Ů
	//MasterVolume,BGMVolume,SoundVolume 0-100��Χ
    private static final int initVolumnPercent=Constant.VOLUMN_PERCENT;
    private static final int initSex=Constant.INIT_SEX;
	
	//��ʼ������
	public static void initSettings() {
		 File f = new File(SETTINGFILE);// ������¼�ļ�
		 if (!f.exists()) {// ����ļ�������
			 try {
	                f.createNewFile();// �������ļ�
	                Arrays.fill(volumes, initVolumnPercent);// �������
	                sex=initSex;              
	                saveSettings();
	            } catch (IOException e) {}
	            return;// ֹͣ����
	        }
	        FileInputStream fis = null;
	        InputStreamReader isr = null;
	        BufferedReader br = null;
	        try {
	            fis = new FileInputStream(f);// �ļ��ֽ�������
	            isr = new InputStreamReader(fis);// �ֽ���ת�ַ���
	            br = new BufferedReader(isr);// �����ַ���
	            String value = br.readLine();// ��ȡһ��
	            if (!(value == null || "".equals(value))) {// �����Ϊ��ֵ
	                String vs[] = value.split(",");// �ָ��ַ���
	                if (vs.length != 4) {
	                    Arrays.fill(volumes,initVolumnPercent);// �������70
	                    sex=initSex;
	                    saveSettings();
	                } else {
	                    for (int i=0;i<3;i++) {
	                        // ����¼�ļ��е�ֵ������ǰ��������
	                    	 volumes[i]=Integer.parseInt(vs[i]);
	                    }
	                    sex=Integer.parseInt(vs[3]);
	                }
	            }
	        } catch (FileNotFoundException e) {
	        } catch (IOException e) {
	        } finally {// ���ιر���
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

	//�洢����ֵ
	public static void saveSettings() {
        String settings = volumes[0] + "," + volumes[1] + "," + volumes[2]+","+sex;
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(SETTINGFILE);// �ļ��ֽ������
            osw = new OutputStreamWriter(fos);// �ֽ���ת�ַ���
            bw = new BufferedWriter(osw);// �����ַ���
            bw.write(settings);// д��ƴ�Ӻ���ַ���
            bw.flush();// �ַ���ˢ��
        } catch (FileNotFoundException e) {
        	initSettings();
        	saveSettings();
        } catch (IOException e) {
        } finally {// ���ιر���
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
	
	//�ı���������ֵ
	public static void changeVolulmes(int receiveVolumes[]) {
		for(int i=0;i<3;i++) {
			volumes[i]=receiveVolumes[i];
		}
	}
	
	//��ȡ��������ֵ
	public static int[] getVolumes() {
		return volumes;
	}
	
	//��ȡ���������ٷ���
	public static float getBGMPercent() {
		float BGMPercent= ((float)(volumes[0]*volumes[1]))/10000;
		return BGMPercent;
	}
	
	//��ȡ��Ч�����ٷ���
	public static float getSoundEffectPercent() {
		float BGMPercent= ((float)(volumes[0]*volumes[2]))/10000;
		return BGMPercent;
	}
	
	//�ı��Ա�
	public static void changeSex() {
		sex=1-sex;
	}
	
	//�ı��Ա�
	public static void changeSex(int targetSex) {
		sex=targetSex%2;
	}
	
	//��ȡ�Ա�
	public static int getSex() {
		return sex;
	}
	
	//�洢��Ϸ����
	public static void saveProgress(LinkQueue progressQueue) {
		 File f = new File(PROGERSSFILE);// ������¼�ļ�
		 if (!f.exists()) {// ����ļ�������
			 try {
	                f.createNewFile();// �������ļ�
	            } catch (IOException e) {}
	    }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(PROGERSSFILE);// �ļ��ֽ������
            osw = new OutputStreamWriter(fos);// �ֽ���ת�ַ���
            bw = new BufferedWriter(osw);// �����ַ���
            while(!progressQueue.QueueEmpty()) {
            	bw.write(progressQueue.DeQueue());// д��ƴ�Ӻ���ַ���
            	bw.newLine();
			}
            bw.flush();// �ַ���ˢ��
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {// ���ιر���
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
	
	//�ж��ļ��Ƿ����
	public static boolean progressFileExist() {
		File f = new File(PROGERSSFILE);// ������¼�ļ�
		if (f.exists()) return true;
		else return false;
	}
	
	//��ȡ��Ϸ����
	public static LinkQueue loadProgress() {
		LinkQueue progressQueue = new LinkQueue();
		File f = new File(PROGERSSFILE);// ������¼�ļ�
		if (!f.exists()) {// ����ļ�������
			try {
				f.createNewFile();
	        } catch (IOException e) {}
	        return progressQueue;// ֹͣ����
	    } 
		FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(f);// �ļ��ֽ�������
            isr = new InputStreamReader(fis);// �ֽ���ת�ַ���
            br = new BufferedReader(isr);// �����ַ���
            String s=null;
            while((s=br.readLine())!=null) {
            	progressQueue.EnQueue(s);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {// ���ιر���
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
