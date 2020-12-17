package calculate;

//Queue ����
public class LinkQueue{
    private QueueNode front;	//����
    private QueueNode rear;		//��β
    private int size;
    public LinkQueue() {
    	front=new QueueNode(null);
    	rear=front;
	    size=0;
    }

    // �ж϶����Ƿ�Ϊ�� 
    public boolean QueueEmpty(){
	    return size==0;
	}
    
    //���
	public void EnQueue(String message){
	    QueueNode node = new QueueNode(message);
	    rear.next=node;
	    rear=node;
	    size++;
	}
	
	//����
	public String DeQueue(){
	    if(QueueEmpty()) return null;
	    String tempMessage;
	    if(front.next==rear) rear=front;
	    QueueNode p=front.next;
	    tempMessage=p.message;
	    front.next=p.next;
	    size--;
	    return tempMessage;
	}
	
	//���ض�ͷ 
	public String GetHead(){
	    if(QueueEmpty()) return null;
	    return front.next.message;
	}
	
	//���ض�β 
	public String GetRear(){
	    if(QueueEmpty()) return null;
	    return rear.message;
	}
	
	//QueueNode �ڲ��� ���н��
	class QueueNode{
	    private String message;
	    private QueueNode next;
	    public QueueNode(String message,QueueNode next) {
	    	this.message=message;
	    	this.next=next;
	    }
	    public QueueNode(String message) {
	    	this.message=message;
	    	next=null;
	    }
	}
}
