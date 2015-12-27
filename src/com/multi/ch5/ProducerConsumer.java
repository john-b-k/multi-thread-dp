package com.multi.ch5;

/**
 * 
 * @author doyoon
 * Producer들은 Data 생성 주체
 * Consumer들은 Data 생성 주체
 * Channel은 Data 통신로(버퍼),Producer Consumer속도 매워줌
 * 
 * -케이크(Data)를 만들어서 Table(Channel)에 놓음
 * -Table에서 케이크 가져감
 * 
 */
public class ProducerConsumer {
	public static void main(String[] args){
		Table table = new Table(3); // buffer size 3
		new MakerThread("*MakerThread-1 ",table).start();
		new MakerThread("*MakerThread-2 ",table).start();
		new MakerThread("*MakerThread-3 ",table).start();
		
		new EaterThread("#EaterThread-1 ",table).start();
		new EaterThread("#EaterThread-2 ",table).start();
		new EaterThread("#EaterThread-3 ",table).start();
	}

}
//Channel 배타제어함
class Table {
	private final String[] buffer;
	private int tail; //다음에 put할 장소
	private int head; //다음에 take할 장소
	private int count;    //buffer안 케익수
	
	public Table(int count){
		this.buffer = new String[count];
		this.head = head;
		this.tail = tail;
		this.count = count;
	}
	
	public synchronized void put(String cake) throws InterruptedException {
		System.out.println(Thread.currentThread().getName()+" puts "+cake);
		while(count >= buffer.length){
			wait();
		}
		buffer[tail] = cake;
		tail = (tail+1) % buffer.length;
		count++;
		notifyAll();
	}
	
	public synchronized String take() throws InterruptedException {
		while(count <= 0){
			wait();
		}
		String cake = buffer[head];
		head = (head+1) % buffer.length;
		count--;
		notifyAll();
		System.out.println(Thread.currentThread().getName()+" takes "+ cake);
		return cake;
	}
}
//Producer
class MakerThread extends Thread{
	private final Table table;
	private static int id = 0; //케이크 안내 번호
	public MakerThread(String name, Table table){
		super(name);
		this.table = table;
	}
	
	public void run(){
		try{
			while(true){
				Thread.sleep((int)Math.random()*1000);
				//Data생산
				String cake = " [Cake No."+nextId()+ " by "+getName()+ " ]";
				table.put(cake);
			}
		}catch(InterruptedException e){
		}
	}
	
	private static synchronized int nextId(){
		return id++;
	}
}

class EaterThread extends Thread {
	private final Table table;
	
	public EaterThread(String name, Table table){
		super(name);
		this.table = table;
	}
	
	public void run(){
		try{
			while(true){
				String cake = table.take();
				Thread.currentThread().sleep((int)Math.random()*1000);
			}
		}catch(InterruptedException e){
		}
	}
}