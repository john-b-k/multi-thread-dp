package com.multi.ch1;
/**
 * 
 * @author doyoon
 * 시나리오
 * - 사용자(UserThread)들이  Gate에 들어감
 * - Gate는 들어오는 사용자 이름, 주소를 확인해서 ,Data Sync확인
 * - Gate에는 1개의 사람만 통과가능
 */
public class SingleThreadExecExam {
	public static void main(String[] args){
		System.out.println("Testing Gate ...");
		ThreadSafeGate gate = new ThreadSafeGate();
//		NonThreadSafeGate gate = new NonThreadSafeGate();
		new UserThread(gate,"Alice","Alaska").start();
		new UserThread(gate,"Bobby","Brazil").start();
		new UserThread(gate,"Chris","Canada").start();
	}
}

class UserThread extends Thread {
	private final Gate gate;
	private final String myname;
	private final String myaddress;
	public UserThread(Gate gate, String myname, String myaddress){
		this.gate = gate;
		this.myname = myname;
		this.myaddress = myaddress;
	}
	
	public void run(){
		System.out.println(myname+" BEGIN");
		while(true){
			gate.pass(myname, myaddress);
		}
	}
}

class ThreadSafeGate implements Gate{
	private int counter = 0;
	private String name = "NoBody";
	private String address = "Nowhere";
	
	public synchronized void pass(String name, String address){
		this.counter++;
		this.name = name;
		this.address = address;
		check();
	}
	
	public synchronized String toString(){
		return "No. "+counter+" : "+name+", "+address;
	}
	
	private void check(){
		if(name.charAt(0) != address.charAt(0)){
			System.out.println("****** Broken Data Sync"+toString());
		}
	}
}

class NonThreadSafeGate implements Gate {
	private int counter = 0;
	private String name = "NoBody";
	private String address = "Nowhere";
	
	public void pass(String name, String address){
		this.counter++;
		this.name = name;
		this.address = address;
		check();
	}
	
	public String toString(){
		return "No. "+counter+" : "+name+", "+address;
	}
	
	private void check(){
		if(name.charAt(0) != address.charAt(0)){
			System.out.println("****** Broken Data Sync"+toString());
		}
	}
}

interface Gate {
	public void pass(String name, String address);
}