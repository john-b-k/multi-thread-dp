package com.multi.ch3;

import java.util.LinkedList;
import java.util.Queue;
/**
 * 
 * @author doyoon
 *  - ReuqestQueue는 데이터 저장소(Shared Object로 배타제어 필요)
 *  - ClientThread는 RequestQueue에 데이터 계속 저장
 *  - ServerThread는 RequestQueue에 데이터 계속 조회 (데이터없으면 wait(blocking))
 *
 */
public class GuardedSuspensionExam {
	public static void main(String [] args){
		RequestQueue queue = new RequestQueue();
		new ClientThread(queue,"Alice").start();
		new ServerThread(queue,"Bobby").start();
	}
}

class Request {
	private final String name;
	
	public Request(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public String toString(){
		return "[ Request "+name+" ]";
	}
}

class RequestQueue{
	private final Queue<Request> queue = new LinkedList<Request>();
	
	public synchronized Request getRequest(){
		while(queue.peek()==null){  //Guared Condition
			try {
				wait();  //wait 수행하는 Thread는 락해제 후 Wait Set에 들어간다
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return queue.remove();
	}
	
	public void putRequest(Request request){
		synchronized(this){
			queue.offer(request);
			notifyAll();  //Wait Set에 위치한 Thread을 깨운다
		}
	}
}

class ClientThread extends Thread{
	private final RequestQueue queue;
	public ClientThread(RequestQueue queue,String name){
		super(name);
		this.queue = queue;
	}
	public void run(){
		for(int i=0; i < 10000; i++){
			Request request = new Request("No."+i);
			System.out.println(Thread.currentThread().getName()+" requests "+request);
			queue.putRequest(request);
			
			try {
				sleep((int)(Math.random())*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class ServerThread extends Thread{
	private final RequestQueue queue;
	public ServerThread(RequestQueue queue, String name){
		super(name);
		this.queue = queue;
	}
	public void run(){
		for(int i=0; i< 10000; i++){
			Request request = queue.getRequest();
			System.out.println(Thread.currentThread().getName()+" handles"+request);
			
			try {
				sleep((int)(Math.random())*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}