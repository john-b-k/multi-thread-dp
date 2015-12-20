package com.multi.ch3.v2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @author doyoon
 *  - ReuqestQueue는 데이터 저장소(Shared Object로 배타제어 필요)
 *  - ClientThread는 RequestQueue에 데이터 계속 저장
 *  - ServerThread는 RequestQueue에 데이터 계속 조회 (데이터없으면 wait(blocking))
 *  - 배타제어를 concurruent 패키지 이용해서 간결히함
 */
public class GuardedSuspensionV2Exam {
	public static void main(String [] args){
		RequestQueueV2 queue = new RequestQueueV2();
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

class RequestQueueV2{
	//Thread Safe Queue : LinkedBlockingQueue 내부적으로 Guarded Suspension구현
	private final BlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();
	
	public Request getRequest(){
		Request request = null;
		try {
			request = queue.take();  //take() : 배타제어 고려
			System.out.println("Q Size : " +queue.size());
		} catch (InterruptedException e) {
		}
		return request;
	}
	
	public void putRequest(Request request){
		try {
			queue.put(request);		//put() : 배타제어 고려
		} catch (InterruptedException e) {
		}
	}
}

class ClientThread extends Thread{
	private final RequestQueueV2 queue;
	public ClientThread(RequestQueueV2 queue,String name){
		super(name);
		this.queue = queue;
	}
	public void run(){
		for(int i=0; true; i++){
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
	private final RequestQueueV2 queue;
	public ServerThread(RequestQueueV2 queue, String name){
		super(name);
		this.queue = queue;
	}
	public void run(){
		for(int i=0; true; i++){
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