package calculate;

public class Constant{
	//Constant��:���泣��
	public final static int MAX_X=33;	//��ά����x��Χ
	public final static int MAX_Y=20;	//��ά����y��Χ
	public final static int INITIAL_ROOM=502;	//��������
	public final static int INITIAL_X=10;	//������x
	public final static int INITIAL_Y=22;	//������y
	public final static double FULL_POWER=100.0;		//������ֵ
	public final static int INITIAL_SPEED=3;		//��ʼ�ٶ�
	public final static int SHIFT_SPEED=3;		//�������ٶȱ���
	public final static double POWER_COST=0.05;		//�����ƶ�һ����Ҫ���ѵ�����
	public final static int UNIT_LENGTH=30;		//��λ����ı߳�������
	public final static int FRAME_RATE=60;		//֡��
	public final static int VOLUMN_PERCENT=80;	//������ʼֵ
	public final static int INIT_SEX=0;			//��ʼ�Ա�
	public final static int A_MAX=20;			//A�������Ʒ����
	public final static int B_MAX=20;			//B�������Ʒ����
	public final static int ROOM_MAX_IMAGE=50;			//��������Ʒ���������
	public final static int MAX_ROOM=5;			//��󷿼�����
	public final static int FRIEND_ACCURACY=100000;	//�����ƶ����ȼ�
	public final static int WRITER_SLEEP=1;	//������Ϣ�̵߳�����ʱ��
	public final static String HELP_TEXT="<html><br/>��ӭ����Exploration�����磡����Ϸ��Exploration��Ŀ�����������ά������¡�<br/>"
			+ "�����������ͼ���Խ����Զ�Ѱ·�ƶ�<br/>"
			+ "WASD/�������Ҽ�ͷ:&#12288�ƶ�&#12288&#12288&#12288"
			+ "Shift:&#12288������&#12288&#12288&#12288"
			+ "�ո�:&#12288�������<br/>"
			+ "F:find&#12288����&#12288&#12288&#12288"
			+ "E:eat&#12288��ʳ&#12288&#12288&#12288"
			+ "B:bag&#12288����&#12288&#12288&#12288"
			+ "V:volume&#12288����<br/>"
			+ "ESC:&#12288�˳���Ϸ&#12288&#12288&#12288"
			+ "H:help&#12288����&#12288&#12288&#12288"
			+ "O:online&#12288����<br/>Enter:&#12288��������&#12288&#12288&#12288"
			+ "P:progress&#12288�������&#12288&#12288&#12288"
			+ "<br/><br/><br/><br/>"
			+ "<br/>�������뷨��Լ��̶�ȡ��������,"
			+ "<br/>ǿ�ҽ��齫���뷨�ĳ�ENGӢ���Ի����������Ϸ����!"
			+"<br/><br/>����������κ����⣬����ϵ������: QQ 1731019653��"
			+ "Tel 18646393118<br/>��л������Exploration��ף�������죡</html>";
	
	public final static String DEVELOPER_TEXT="<html>����Ϸ��Exploration��Ŀ����п�����ά������¡�<br/>"
			+ "Exploration��Ŀ������ڹ�������ҵ��ѧ�����ڣ���A����<br/>Exploration��Ŀ���Ա:<br/>&#12288������"
			+ "<br/>&#12288������<br/><br/>��ϵ����:<br/>&#12288QQ:1731019653&#12288Tel:18646393118<br/><br/>"
			+ "��������Ϣ: <br/><br/>��л�����߶���Ϸ�����Ĵ���֧�֣�<br/>"
			+ "����Ϸ�в����ز������������ʵ�ģ��������������ѧϰ������"
			+ "<br/>����������ҵ��;��������Ȩ����ϵ�����ߣ�<br/><br/><br/></html>";
	
	public final static String UPDATELOG_TEXT="<html><br/><br/>��ǰ�汾ΪV3.6.0	Version:V3.6.0<br/><br/>"
			+ "V3.6.0:��Ϸ���ȴ�ȡ�洢�Ͷ�ȡ���ܿ���  2020.10.29<br/>"
			+ "V3.5.1:��Ϣ���ͺʹ����ӳٽ���  2020.10.28<br/>"
			+ "V3.5.0:����ϵͳ���� ���ѽ�ɫ�ƶ�����bug�޸�  2020.10.28<br/>"
			+ "V3.4.0:������ʳ���� ������Զ�Ѱ·�ƶ�����  2020.10.27<br/>"
			+ "V3.3.0:��������������  2020.10.27<br/>"
			+ "V3.2.0:��������������  2020.10.26<br/>"
			+ "V3.1.2:����ϵͳ�߼��Ż�  2020.10.10<br/>"
			+ "V3.1.1:���±���ϵͳ  2020.10.10<br/>"
			+ "V3.1.0:���ӱ���ϵͳ,���ӳ�ʼ��ͼ  2020.10.10<br/>"
			+ "V3.0.0:��Ϸ���������,�˻������Ż�,�������� 2020.9.29<br/>"
			+ "V2.0.0:��Ϸ���������,�¹������,�˻������Ż� 2020.8.24<br/>"
			+ "<br/>V1.2.0 beta:����Ч�ļ�����ʾ��Ϣ�����˸��� 2020.7.12<br/>V1.2.0:"
			+ "����˰����͸�����־���� 2020.7.11<br/>"
+ "V1.1.2:�޸��˹ر���ʾ����ʱ��Ч�޷�ֹͣ��bug 2020.7.11<br/>V1.1.1:�޸��˹ر�������ʱ��Ч�޷�ֹͣ��bug 2020.7.11<br/>"
+ "V1.1.0:�������Ч���� 2020.7.11 <br/>V1.0.0:����˳ɾ͹��� �����汾����2020.7.10<br/>V0.0.0:��ʼ��������Ϸ 2020.7.9<br/><br/>��������Ϣ:����Ϸ��Exploration��Ŀ��"
+ "���п�����ά������¡�<br/>QQ:1731019653 Tel:18646393118<br/>��л������Exploration��ף�������죡<br/><br/><br/><br/></html>";
}
