package com.multi.ch4.guardedtimed;

import java.util.concurrent.TimeoutException;

/**
 * 
 * @author doyoon
 * Balking과 Guared Suspension중간
 * - 가드조건 만족할때까지 일정시간 기다림, 정해진 시간 지나도 가드조건 불만족이면  Balk함
 * 
 * - 이 예에서는 결국 doExecute()수행 안됨(Balk)
 */
public class GuaredTimedExam {
	public static void main(String args[]) throws Exception{
		Host host = new Host(10000);
		System.out.println("execute BEGIN");
		host.execute();
	}
}

class Host{
	private final long timeout;
	private boolean ready = false;
	
	public Host(long timeout){
		this.timeout = timeout;
	}
	
	//stateChagingMethod
	public synchronized void setExecutable(){
		this.ready = true;
		notifyAll();
	}
	
	//guardedMethod
	public synchronized void execute() throws Exception{
		long start = System.currentTimeMillis();
		while(ready == false){//Guard 조건
			long now = System.currentTimeMillis();
			long rest = timeout-(now - start);
			if(rest<0){
				throw new TimeoutException("Timeout Exception");
			}
			try {
				wait(rest); // 루프를 돌수록 기다리는 시간을 점점 짧게 세팅
			} catch (InterruptedException e) {
			}
		}
		doExecute();
	}
	private void doExecute(){
		System.out.println(Thread.currentThread().getName()+" calls execute");
	}
}
