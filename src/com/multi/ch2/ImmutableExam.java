package com.multi.ch2;
/**
 * 
 * @author doyoon
 *  Immutable객체는 배타제어을 할 필요가 없다
 *  - 상태 변경 메서드가 없는 객체
 *  - 자유로이 엑세스 가능함을 보여주는 예제
 */
public class ImmutableExam {
	public static void main(String[] args){
		ImmutablePerson alice = new ImmutablePerson("Alice","Aliska");
		new PrintPersonThread(alice).start();
		new PrintPersonThread(alice).start();
		new PrintPersonThread(alice).start();
	}
}

class ImmutablePerson {
	private final String name; //frozen field
	private final String address; //frozen field
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