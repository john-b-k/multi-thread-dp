package com.multi.ch2;

public class ImmutableExam {
	public static void main(String[] args){
		ImmutablePerson alice = new ImmutablePerson("Alice","Aliska");
		new PrintPersonThread(alice).start();
		new PrintPersonThread(alice).start();
		new PrintPersonThread(alice).start();
	}
}

class ImmutablePerson {
	private final String name;
	private final String address;
	public ImmutablePerson(String name, String address){
		this.name = name;
		this.address = address;
	}
	
	public String getName(){
		return name;
	}
	public String getAddress(){
		return address;
	}
	public String toString(){
		return "[ Person : name = "+name+" , address = "+address+" ]";
	}
}

class PrintPersonThread extends Thread{
	private ImmutablePerson person;
	public PrintPersonThread (ImmutablePerson person){
		this.person = person;
	}
	public void run(){
		while(true){
			System.out.println(Thread.currentThread().getName()+" prints "+person.toString());
		}
	}
}