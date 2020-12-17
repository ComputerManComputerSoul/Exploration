package calculate;

//Queue 队列
public class LinkQueue{
    private QueueNode front;	//队首
    private QueueNode rear;		//队尾
    private int size;
    public LinkQueue() {
    	front=new QueueNode(null);
    	rear=front;
	    size=0;
    }

    // 判断队列是否为空 
    public boolean QueueEmpty(){
	    return size==0;
	}
    
    //入队
	public void EnQueue(String message){
	    QueueNode node = new QueueNode(message);
	    rear.next=node;
	    rear=node;
	    size++;
	}
	
	//出队
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
	
	//返回队头 
	public String GetHead(){
	    if(QueueEmpty()) return null;
	    return front.next.message;
	}
	
	//返回队尾 
	public String GetRear(){
	    if(QueueEmpty()) return null;
	    return rear.message;
	}
	
	//QueueNode 内部类 队列结点
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
